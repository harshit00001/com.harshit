package com.harshit.jobpulse.pipeline;

import com.harshit.jobpulse.config.CompanyConfig;
import com.harshit.jobpulse.connector.RawJob;
import com.harshit.jobpulse.domain.JobPosting;
import com.harshit.jobpulse.repo.JobPostingRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

/**
 * Applies the India gate, scores what survives, and upserts it.
 *
 * <p>Re-crawling an unchanged posting refreshes {@code lastSeenAt} instead of creating a
 * duplicate, keyed on company plus the vendor's own job id.
 */
@Service
public class JobIngestService {

    private static final Logger log = LoggerFactory.getLogger(JobIngestService.class);
    private static final int DESCRIPTION_LIMIT = JobPosting.DESCRIPTION_LENGTH;

    private final JobPostingRepository repository;
    private final LocationFilter locationFilter;
    private final MatchScorer matchScorer;

    public JobIngestService(JobPostingRepository repository,
                            LocationFilter locationFilter,
                            MatchScorer matchScorer) {
        this.repository = repository;
        this.locationFilter = locationFilter;
        this.matchScorer = matchScorer;
    }

    @Transactional
    public IngestSummary ingest(CompanyConfig company, List<RawJob> rawJobs) {
        int indiaJobs = 0;
        int stored = 0;
        int matched = 0;

        for (RawJob rawJob : rawJobs) {
            if (rawJob.getTitle() == null || rawJob.getExternalId() == null) {
                continue;
            }
            LocationFilter.Decision decision = locationFilter.evaluate(rawJob);
            if (!decision.accepted()) {
                log.debug("Dropped '{}' at {} — {}", rawJob.getTitle(), rawJob.getLocationText(),
                        decision.reason());
                continue;
            }
            indiaJobs++;

            MatchResult result = matchScorer.score(rawJob);
            upsert(company, rawJob, result);
            stored++;
            if (result.matched()) {
                matched++;
            }
        }
        return new IngestSummary(indiaJobs, stored, matched);
    }

    private void upsert(CompanyConfig company, RawJob rawJob, MatchResult result) {
        JobPosting posting = repository
                .findByCompanyIdAndExternalId(company.getId(), rawJob.getExternalId())
                .orElseGet(JobPosting::new);

        Instant now = Instant.now();
        if (posting.getId() == null) {
            posting.setFirstSeenAt(now);
        }
        posting.setLastSeenAt(now);

        posting.setCompanyId(company.getId());
        posting.setCompanyName(company.getName());
        posting.setExternalId(rawJob.getExternalId());
        posting.setTitle(truncate(rawJob.getTitle(), 500));
        posting.setLocationText(truncate(rawJob.getLocationText(), 500));
        posting.setCity(truncate(rawJob.getCity(), 120));
        posting.setCountry(truncate(rawJob.getCountry(), 120));
        posting.setDepartment(truncate(rawJob.getDepartment(), 120));
        posting.setEmploymentType(truncate(rawJob.getEmploymentType(), 60));
        posting.setRemote(rawJob.isRemote());
        posting.setUrl(truncate(rawJob.getUrl(), 1000));
        posting.setApplyUrl(truncate(rawJob.getApplyUrl(), 1000));
        posting.setDescriptionText(truncate(rawJob.getDescription(), DESCRIPTION_LIMIT));
        posting.setPostedOnText(truncate(rawJob.getPostedOnText(), 120));
        posting.setPublishedAt(rawJob.getPublishedAt());

        posting.setMatchScore(result.score());
        posting.setMatched(result.matched());
        posting.setMatchedSkills(truncate(String.join(", ", result.matchedSkills()), 1000));
        posting.setMissingSkills(truncate(String.join(", ", result.missingMustHaveSkills()), 1000));
        posting.setScoreBreakdown(truncate(result.breakdown(), 1000));

        repository.save(posting);
    }

    private String truncate(String value, int limit) {
        if (value == null) {
            return null;
        }
        String trimmed = value.trim();
        return trimmed.length() <= limit ? trimmed : trimmed.substring(0, limit);
    }

    public record IngestSummary(int indiaJobs, int stored, int matched) {
    }
}
