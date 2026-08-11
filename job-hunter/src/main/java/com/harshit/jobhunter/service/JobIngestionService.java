package com.harshit.jobhunter.service;

import com.harshit.jobhunter.config.JobHunterProperties;
import com.harshit.jobhunter.model.JobPosting;
import com.harshit.jobhunter.repository.JobPostingRepository;
import com.harshit.jobhunter.source.JobSourceAdapter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
public class JobIngestionService {

    private static final Logger log = LoggerFactory.getLogger(JobIngestionService.class);

    private final JobHunterProperties properties;
    private final JobPostingRepository repository;
    private final List<JobSourceAdapter> adapters;

    public JobIngestionService(JobHunterProperties properties,
                               JobPostingRepository repository,
                               List<JobSourceAdapter> adapters) {
        this.properties = properties;
        this.repository = repository;
        this.adapters = adapters;
    }

    @Transactional
    public int fetchAll() {
        int saved = 0;
        for (JobHunterProperties.Source source : properties.getSources()) {
            if (!source.isEnabled()) {
                continue;
            }
            JobSourceAdapter adapter = adapters.stream()
                    .filter(a -> a.supports(source))
                    .findFirst()
                    .orElse(null);
            if (adapter == null) {
                log.warn("No adapter for source type: {}", source.getType());
                continue;
            }
            List<JobPosting> fetched = adapter.fetch(source);
            log.info("Source {} returned {} jobs", source.getName(), fetched.size());
            for (JobPosting job : fetched) {
                if (saveOrUpdate(job)) {
                    saved++;
                }
            }
        }
        return saved;
    }

    private boolean saveOrUpdate(JobPosting job) {
        var existing = repository.findBySourceAndExternalId(job.getSource(), job.getExternalId());
        if (existing.isPresent()) {
            JobPosting row = existing.get();
            row.setFetchedAt(Instant.now());
            repository.save(row);
            return false;
        }
        repository.save(job);
        return true;
    }
}
