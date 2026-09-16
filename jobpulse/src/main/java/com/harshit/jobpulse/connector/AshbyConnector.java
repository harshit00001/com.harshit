package com.harshit.jobpulse.connector;

import com.fasterxml.jackson.databind.JsonNode;
import com.harshit.jobpulse.config.CompanyConfig;
import com.harshit.jobpulse.config.ConnectorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Ashby public job board API — used by Tekion.
 *
 * <p>One request returns every posting with its full description and a structured
 * postal address, so no per-job detail calls and no HTML scraping are needed.
 */
@Component
public class AshbyConnector implements JobConnector {

    private static final Logger log = LoggerFactory.getLogger(AshbyConnector.class);
    private static final String DEFAULT_ENDPOINT = "https://api.ashbyhq.com/posting-api/job-board/";

    private final HttpFetcher httpFetcher;

    public AshbyConnector(HttpFetcher httpFetcher) {
        this.httpFetcher = httpFetcher;
    }

    @Override
    public ConnectorType type() {
        return ConnectorType.ASHBY;
    }

    @Override
    public List<RawJob> fetch(CompanyConfig company) {
        String board = company.requiredOption("board");
        String url = company.option("endpoint", DEFAULT_ENDPOINT + board);

        JsonNode response = httpFetcher.getJson(url);
        JsonNode postings = response.path("jobs");
        List<RawJob> jobs = new ArrayList<>();

        for (JsonNode posting : postings) {
            if (jobs.size() >= company.getMaxJobs()) {
                break;
            }
            if (!posting.path("isListed").asBoolean(true)) {
                continue;
            }
            jobs.add(toRawJob(posting));
        }
        log.info("Ashby {}: {} listed postings", company.getId(), jobs.size());
        return jobs;
    }

    private RawJob toRawJob(JsonNode posting) {
        JsonNode address = posting.path("address").path("postalAddress");

        RawJob job = new RawJob();
        job.setExternalId(posting.path("id").asText(null));
        job.setTitle(posting.path("title").asText(null));
        job.setLocationText(posting.path("location").asText(null));
        job.setCity(nullSafe(address.path("addressLocality").asText(null)));
        job.setCountry(nullSafe(address.path("addressCountry").asText(null)));
        job.setDepartment(posting.path("department").asText(null));
        job.setEmploymentType(posting.path("employmentType").asText(null));
        job.setRemote(posting.path("isRemote").asBoolean(false));
        job.setUrl(posting.path("jobUrl").asText(null));
        job.setApplyUrl(posting.path("applyUrl").asText(null));
        job.setDescription(posting.path("descriptionPlain").asText(""));
        job.setPublishedAt(parseInstant(posting.path("publishedAt").asText(null)));
        return job;
    }

    private String nullSafe(String value) {
        return value == null || value.isBlank() ? null : value;
    }

    private Instant parseInstant(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return Instant.parse(value);
        } catch (DateTimeParseException exception) {
            return null;
        }
    }
}
