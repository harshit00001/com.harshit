package com.harshit.jobhunter.scheduler;

import com.harshit.jobhunter.service.JobIngestionService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class JobFetchScheduler implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(JobFetchScheduler.class);

    private final JobIngestionService ingestionService;
    private final com.harshit.jobhunter.config.JobHunterProperties properties;

    public JobFetchScheduler(JobIngestionService ingestionService,
                             com.harshit.jobhunter.config.JobHunterProperties properties) {
        this.ingestionService = ingestionService;
        this.properties = properties;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (properties.isFetchOnStartup()) {
            log.info("Fetching jobs on startup...");
            ingestionService.fetchAll();
        }
    }

    @Scheduled(cron = "${job-hunter.fetch-cron:0 0 6,18 * * *}")
    public void scheduledFetch() {
        log.info("Scheduled job fetch...");
        ingestionService.fetchAll();
    }
}
