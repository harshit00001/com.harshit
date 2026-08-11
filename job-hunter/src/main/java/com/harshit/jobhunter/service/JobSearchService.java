package com.harshit.jobhunter.service;

import com.harshit.jobhunter.config.JobHunterProperties;
import com.harshit.jobhunter.model.JobMatchResult;
import com.harshit.jobhunter.model.JobPosting;
import com.harshit.jobhunter.repository.JobPostingRepository;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class JobSearchService {

    private final JobPostingRepository repository;
    private final JobMatcherService matcher;
    private final JobHunterProperties properties;

    public JobSearchService(JobPostingRepository repository,
                            JobMatcherService matcher,
                            JobHunterProperties properties) {
        this.repository = repository;
        this.matcher = matcher;
        this.properties = properties;
    }

    public List<JobMatchResult> recommended(int days, int minScore, int limit) {
        Instant since = Instant.now().minus(days, ChronoUnit.DAYS);
        List<JobPosting> recent = repository.findAll().stream()
                .filter(j -> j.getPostedAt() == null || !j.getPostedAt().isBefore(since))
                .toList();
        return matcher.rank(recent).stream()
                .filter(r -> r.getScore() >= minScore)
                .limit(limit)
                .toList();
    }

    /**
     * Simple natural-language style filter on top of scored jobs.
     */
    public List<JobMatchResult> searchByQuery(String query, int days, int limit) {
        String q = query == null ? "" : query.toLowerCase(Locale.ROOT);
        List<String> extraTerms = extractTerms(q);
        int minScore = q.contains("strong") ? 8 : 3;

        return recommended(days, minScore, 200).stream()
                .filter(r -> matchesQuery(r, q, extraTerms))
                .limit(limit)
                .toList();
    }

    private List<String> extractTerms(String q) {
        List<String> terms = new ArrayList<>();
        String[] hints = {"java", "spring", "microservice", "hibernate", "jpa", "jwt", "kafka", "remote", "india", "backend"};
        for (String h : hints) {
            if (q.contains(h)) {
                terms.add(h);
            }
        }
        return terms;
    }

    private boolean matchesQuery(JobMatchResult job, String fullQuery, List<String> extraTerms) {
        if (fullQuery.isBlank()) {
            return true;
        }
        String blob = (job.getTitle() + " " + job.getCompany() + " " + job.getSummary()).toLowerCase(Locale.ROOT);
        if (extraTerms.isEmpty()) {
            return blob.contains("java") || fullQuery.contains("job") || fullQuery.contains("opening");
        }
        return extraTerms.stream().allMatch(blob::contains);
    }

    public JobHunterProperties getProperties() {
        return properties;
    }
}
