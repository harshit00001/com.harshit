package com.harshit.jobpulse.connector;

import com.fasterxml.jackson.databind.JsonNode;
import com.harshit.jobpulse.config.CompanyConfig;
import com.harshit.jobpulse.config.ConnectorType;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Workday "CXS" job board API — used by Accenture and a large share of enterprises.
 *
 * <p>Search is a POST that accepts facets, which is how the India-only restriction is
 * pushed upstream instead of being filtered after download. Descriptions require a
 * second call per posting, so those are capped by
 * {@link CompanyConfig#getMaxDetailFetches()}.
 */
@Component
public class WorkdayConnector implements JobConnector {

    private static final Logger log = LoggerFactory.getLogger(WorkdayConnector.class);
    private static final int PAGE_SIZE = 20;

    private final HttpFetcher httpFetcher;

    public WorkdayConnector(HttpFetcher httpFetcher) {
        this.httpFetcher = httpFetcher;
    }

    @Override
    public ConnectorType type() {
        return ConnectorType.WORKDAY;
    }

    @Override
    public List<RawJob> fetch(CompanyConfig company) {
        String cxsBase = trimTrailingSlash(company.requiredOption("cxsBase"));
        String externalBase = trimTrailingSlash(company.option("externalBase", cxsBase));
        String countryFacetId = company.option("countryFacetId", null);

        Map<String, JsonNode> byPath = new LinkedHashMap<>();
        for (String term : searchTerms(company)) {
            collectForTerm(cxsBase, countryFacetId, term, company.getMaxJobs(), byPath);
            if (byPath.size() >= company.getMaxJobs()) {
                break;
            }
        }

        log.info("Workday {}: {} unique postings from {} search term(s)",
                company.getId(), byPath.size(), searchTerms(company).size());

        List<RawJob> jobs = new ArrayList<>();
        int detailBudget = company.getMaxDetailFetches();

        for (Map.Entry<String, JsonNode> entry : byPath.entrySet()) {
            if (detailBudget <= 0) {
                break;
            }
            detailBudget--;
            try {
                jobs.add(toRawJob(cxsBase, externalBase, entry.getKey(), entry.getValue()));
            } catch (RuntimeException exception) {
                log.warn("Workday {}: skipping {} ({})", company.getId(), entry.getKey(),
                        exception.getMessage());
            }
        }
        return jobs;
    }

    private void collectForTerm(String cxsBase, String countryFacetId, String term, int maxJobs,
                                Map<String, JsonNode> target) {
        int offset = 0;
        while (target.size() < maxJobs) {
            JsonNode response = httpFetcher.postJson(cxsBase + "/jobs",
                    buildSearchBody(term, countryFacetId, offset));
            JsonNode postings = response.path("jobPostings");
            if (!postings.isArray() || postings.isEmpty()) {
                return;
            }
            for (JsonNode posting : postings) {
                String path = posting.path("externalPath").asText(null);
                if (path != null && !target.containsKey(path)) {
                    target.put(path, posting);
                }
            }
            if (postings.size() < PAGE_SIZE) {
                return;
            }
            offset += PAGE_SIZE;
        }
    }

    private Map<String, Object> buildSearchBody(String term, String countryFacetId, int offset) {
        Map<String, Object> facets = new LinkedHashMap<>();
        if (countryFacetId != null && !countryFacetId.isBlank()) {
            facets.put("locationCountry", List.of(countryFacetId.split("\\s*,\\s*")));
        }
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("appliedFacets", facets);
        body.put("limit", PAGE_SIZE);
        body.put("offset", offset);
        body.put("searchText", term);
        return body;
    }

    private RawJob toRawJob(String cxsBase, String externalBase, String externalPath,
                            JsonNode summary) {
        JsonNode detail = httpFetcher.getJson(cxsBase + externalPath).path("jobPostingInfo");

        RawJob job = new RawJob();
        job.setExternalId(firstBulletField(summary, externalPath));
        job.setTitle(text(detail, "title", summary.path("title").asText(null)));
        job.setLocationText(text(detail, "location", cityFromBulletFields(summary)));
        job.setCity(text(detail, "location", cityFromBulletFields(summary)));
        job.setCountry(detail.path("country").path("descriptor").asText(null));
        job.setEmploymentType(text(detail, "timeType", null));
        job.setPostedOnText(text(detail, "postedOn", summary.path("postedOn").asText(null)));
        job.setPublishedAt(parseStartDate(detail.path("startDate").asText(null)));

        String externalUrl = detail.path("externalUrl").asText(null);
        job.setUrl(externalUrl != null && !externalUrl.isBlank()
                ? externalUrl
                : externalBase + externalPath);
        job.setApplyUrl(job.getUrl());
        job.setDescription(htmlToText(detail.path("jobDescription").asText("")));
        job.setRemote(job.getLocationText() != null
                && job.getLocationText().toLowerCase().contains("remote"));
        return job;
    }

    private String firstBulletField(JsonNode summary, String fallback) {
        JsonNode bullets = summary.path("bulletFields");
        if (bullets.isArray() && !bullets.isEmpty()) {
            return bullets.get(0).asText(fallback);
        }
        return fallback;
    }

    private String cityFromBulletFields(JsonNode summary) {
        JsonNode bullets = summary.path("bulletFields");
        if (bullets.isArray() && bullets.size() > 1) {
            return bullets.get(1).asText(null);
        }
        return null;
    }

    private List<String> searchTerms(CompanyConfig company) {
        return company.getSearchTerms().isEmpty() ? List.of("java") : company.getSearchTerms();
    }

    private String text(JsonNode node, String field, String fallback) {
        String value = node.path(field).asText(null);
        return value == null || value.isBlank() ? fallback : value;
    }

    private Instant parseStartDate(String value) {
        if (value == null || value.isBlank()) {
            return null;
        }
        try {
            return LocalDate.parse(value).atStartOfDay(ZoneOffset.UTC).toInstant();
        } catch (DateTimeParseException exception) {
            return null;
        }
    }

    private String htmlToText(String html) {
        if (html == null || html.isBlank()) {
            return "";
        }
        return Jsoup.parse(html).text();
    }

    private String trimTrailingSlash(String value) {
        return value.endsWith("/") ? value.substring(0, value.length() - 1) : value;
    }
}
