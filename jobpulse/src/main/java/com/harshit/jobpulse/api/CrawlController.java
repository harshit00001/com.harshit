package com.harshit.jobpulse.api;

import com.harshit.jobpulse.config.JobPulseProperties;
import com.harshit.jobpulse.pipeline.CrawlOrchestrator;
import com.harshit.jobpulse.pipeline.CrawlReport;
import com.harshit.jobpulse.repo.JobPostingRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.LinkedHashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/crawl")
public class CrawlController {

    private final CrawlOrchestrator orchestrator;
    private final JobPulseProperties properties;
    private final JobPostingRepository repository;

    public CrawlController(CrawlOrchestrator orchestrator,
                           JobPulseProperties properties,
                           JobPostingRepository repository) {
        this.orchestrator = orchestrator;
        this.properties = properties;
        this.repository = repository;
    }

    @PostMapping("/run")
    public CrawlReport runAll() {
        return orchestrator.crawlAll();
    }

    @PostMapping("/run/{companyId}")
    public ResponseEntity<CrawlReport> runOne(@PathVariable String companyId) {
        return properties.findCompany(companyId)
                .map(company -> ResponseEntity.ok(orchestrator.crawlOne(company)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/last")
    public ResponseEntity<CrawlReport> lastReport() {
        CrawlReport report = orchestrator.lastReport();
        return report == null ? ResponseEntity.noContent().build() : ResponseEntity.ok(report);
    }

    @GetMapping("/stats")
    public Map<String, Object> stats() {
        Map<String, Object> byCompany = new LinkedHashMap<>();
        repository.countByCompany()
                .forEach(row -> byCompany.put(String.valueOf(row[0]), row[1]));

        Map<String, Object> stats = new LinkedHashMap<>();
        stats.put("totalIndexed", repository.count());
        stats.put("totalMatched", repository.countByMatchedTrue());
        stats.put("configuredCompanies", properties.getCompanies().size());
        stats.put("enabledCompanies", properties.enabledCompanies().size());
        stats.put("byCompany", byCompany);
        return stats;
    }
}
