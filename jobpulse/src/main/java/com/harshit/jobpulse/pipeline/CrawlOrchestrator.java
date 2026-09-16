package com.harshit.jobpulse.pipeline;

import com.harshit.jobpulse.config.CompanyConfig;
import com.harshit.jobpulse.config.JobPulseProperties;
import com.harshit.jobpulse.connector.ConnectorRegistry;
import com.harshit.jobpulse.connector.RawJob;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/** Drives the crawl for every enabled company and records the outcome. */
@Service
public class CrawlOrchestrator {

    private static final Logger log = LoggerFactory.getLogger(CrawlOrchestrator.class);

    private final JobPulseProperties properties;
    private final ConnectorRegistry connectorRegistry;
    private final JobIngestService ingestService;
    private final AtomicReference<CrawlReport> lastReport = new AtomicReference<>();

    public CrawlOrchestrator(JobPulseProperties properties,
                             ConnectorRegistry connectorRegistry,
                             JobIngestService ingestService) {
        this.properties = properties;
        this.connectorRegistry = connectorRegistry;
        this.ingestService = ingestService;
    }

    @EventListener(ApplicationReadyEvent.class)
    @Async
    public void crawlOnStartup() {
        if (properties.getCrawl().isRunOnStartup()) {
            log.info("Running startup crawl for {} enabled company(ies)",
                    properties.enabledCompanies().size());
            crawlAll();
        }
    }

    @Scheduled(cron = "${jobpulse.crawl.cron:0 0 */12 * * *}")
    public void scheduledCrawl() {
        if (properties.getCrawl().isScheduleEnabled()) {
            crawlAll();
        }
    }

    public CrawlReport crawlAll() {
        CrawlReport report = new CrawlReport();
        for (CompanyConfig company : properties.enabledCompanies()) {
            report.add(crawlCompany(company));
        }
        report.complete();
        lastReport.set(report);
        log.info("Crawl finished: fetched={}, india={}, matched={}",
                report.getTotalFetched(), report.getTotalIndiaJobs(), report.getTotalMatched());
        return report;
    }

    public CrawlReport crawlOne(CompanyConfig company) {
        CrawlReport report = new CrawlReport();
        report.add(crawlCompany(company));
        report.complete();
        lastReport.set(report);
        return report;
    }

    private CrawlReport.CompanyOutcome crawlCompany(CompanyConfig company) {
        String connectorName = company.getConnector().name();
        try {
            List<RawJob> rawJobs = connectorRegistry.require(company.getConnector()).fetch(company);
            JobIngestService.IngestSummary summary = ingestService.ingest(company, rawJobs);

            log.info("{} [{}] fetched={} india={} stored={} matched={}",
                    company.getName(), connectorName, rawJobs.size(),
                    summary.indiaJobs(), summary.stored(), summary.matched());

            return new CrawlReport.CompanyOutcome(company.getId(), company.getName(), connectorName,
                    rawJobs.size(), summary.indiaJobs(), summary.stored(), summary.matched(), null);
        } catch (RuntimeException exception) {
            log.error("Crawl failed for {} [{}]: {}", company.getName(), connectorName,
                    exception.getMessage());
            return new CrawlReport.CompanyOutcome(company.getId(), company.getName(), connectorName,
                    0, 0, 0, 0, exception.getMessage());
        }
    }

    public CrawlReport lastReport() {
        return lastReport.get();
    }
}
