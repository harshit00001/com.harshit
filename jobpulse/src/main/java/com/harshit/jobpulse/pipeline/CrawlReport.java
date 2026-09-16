package com.harshit.jobpulse.pipeline;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/** Per-run summary surfaced through {@code /api/crawl} so failures are visible. */
public class CrawlReport {

    private Instant startedAt = Instant.now();
    private Instant finishedAt;
    private final List<CompanyOutcome> companies = new ArrayList<>();

    public void add(CompanyOutcome outcome) {
        companies.add(outcome);
    }

    public void complete() {
        this.finishedAt = Instant.now();
    }

    public Instant getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(Instant startedAt) {
        this.startedAt = startedAt;
    }

    public Instant getFinishedAt() {
        return finishedAt;
    }

    public List<CompanyOutcome> getCompanies() {
        return companies;
    }

    public int getTotalFetched() {
        return companies.stream().mapToInt(CompanyOutcome::fetched).sum();
    }

    public int getTotalIndiaJobs() {
        return companies.stream().mapToInt(CompanyOutcome::indiaJobs).sum();
    }

    public int getTotalMatched() {
        return companies.stream().mapToInt(CompanyOutcome::matched).sum();
    }

    public record CompanyOutcome(
            String companyId,
            String companyName,
            String connector,
            int fetched,
            int indiaJobs,
            int stored,
            int matched,
            String error) {

        public boolean isSuccessful() {
            return error == null;
        }
    }
}
