package com.harshit.jobpulse.connector;

import com.fasterxml.jackson.databind.JsonNode;
import com.harshit.jobpulse.config.CompanyConfig;
import com.harshit.jobpulse.config.ConnectorType;
import org.jsoup.Jsoup;
import org.jsoup.parser.Parser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/** Greenhouse public boards API: one call returns postings plus HTML-escaped content. */
@Component
public class GreenhouseConnector implements JobConnector {

    private static final Logger log = LoggerFactory.getLogger(GreenhouseConnector.class);
    private static final String DEFAULT_ENDPOINT = "https://boards-api.greenhouse.io/v1/boards/";

    private final HttpFetcher httpFetcher;

    public GreenhouseConnector(HttpFetcher httpFetcher) {
        this.httpFetcher = httpFetcher;
    }

    @Override
    public ConnectorType type() {
        return ConnectorType.GREENHOUSE;
    }

    @Override
    public List<RawJob> fetch(CompanyConfig company) {
        String token = company.requiredOption("boardToken");
        String url = company.option("endpoint",
                DEFAULT_ENDPOINT + token + "/jobs?content=true");

        JsonNode postings = httpFetcher.getJson(url).path("jobs");
        List<RawJob> jobs = new ArrayList<>();

        for (JsonNode posting : postings) {
            if (jobs.size() >= company.getMaxJobs()) {
                break;
            }
            RawJob job = new RawJob();
            job.setExternalId(posting.path("id").asText(null));
            job.setTitle(posting.path("title").asText(null));
            job.setLocationText(posting.path("location").path("name").asText(null));
            job.setCity(job.getLocationText());
            job.setUrl(posting.path("absolute_url").asText(null));
            job.setApplyUrl(job.getUrl());
            job.setDescription(decodeContent(posting.path("content").asText("")));
            job.setPublishedAt(parseInstant(posting.path("updated_at").asText(null)));
            jobs.add(job);
        }
        log.info("Greenhouse {}: {} postings", company.getId(), jobs.size());
        return jobs;
    }

    /** Greenhouse double-encodes the description, so unescape before stripping tags. */
    private String decodeContent(String content) {
        if (content == null || content.isBlank()) {
            return "";
        }
        String unescaped = Parser.unescapeEntities(content, true);
        return Jsoup.parse(unescaped).text();
    }

    private Instant parseInstant(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return OffsetDateTime.parse(value).toInstant();
        } catch (DateTimeParseException exception) {
            return null;
        }
    }
}
