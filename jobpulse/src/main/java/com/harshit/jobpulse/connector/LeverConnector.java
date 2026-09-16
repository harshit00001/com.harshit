package com.harshit.jobpulse.connector;

import com.fasterxml.jackson.databind.JsonNode;
import com.harshit.jobpulse.config.CompanyConfig;
import com.harshit.jobpulse.config.ConnectorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

/** Lever public postings API. */
@Component
public class LeverConnector implements JobConnector {

    private static final Logger log = LoggerFactory.getLogger(LeverConnector.class);
    private static final String DEFAULT_ENDPOINT = "https://api.lever.co/v0/postings/";

    private final HttpFetcher httpFetcher;

    public LeverConnector(HttpFetcher httpFetcher) {
        this.httpFetcher = httpFetcher;
    }

    @Override
    public ConnectorType type() {
        return ConnectorType.LEVER;
    }

    @Override
    public List<RawJob> fetch(CompanyConfig company) {
        String account = company.requiredOption("account");
        String url = company.option("endpoint", DEFAULT_ENDPOINT + account + "?mode=json");

        JsonNode postings = httpFetcher.getJson(url);
        List<RawJob> jobs = new ArrayList<>();

        for (JsonNode posting : postings) {
            if (jobs.size() >= company.getMaxJobs()) {
                break;
            }
            RawJob job = new RawJob();
            job.setExternalId(posting.path("id").asText(null));
            job.setTitle(posting.path("text").asText(null));
            job.setLocationText(posting.path("categories").path("location").asText(null));
            job.setCity(job.getLocationText());
            job.setDepartment(posting.path("categories").path("team").asText(null));
            job.setEmploymentType(posting.path("categories").path("commitment").asText(null));
            job.setUrl(posting.path("hostedUrl").asText(null));
            job.setApplyUrl(posting.path("applyUrl").asText(job.getUrl()));
            job.setDescription(posting.path("descriptionPlain").asText(""));
            long createdAt = posting.path("createdAt").asLong(0L);
            job.setPublishedAt(createdAt > 0 ? Instant.ofEpochMilli(createdAt) : null);
            jobs.add(job);
        }
        log.info("Lever {}: {} postings", company.getId(), jobs.size());
        return jobs;
    }
}
