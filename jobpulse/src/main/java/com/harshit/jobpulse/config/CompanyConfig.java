package com.harshit.jobpulse.config;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * One entry per company in {@code config/companies.yml}.
 *
 * <p>Connector-specific settings live in {@link #options} so new companies can be
 * onboarded through configuration alone.
 */
public class CompanyConfig {

    private String id;
    private String name;
    private boolean enabled = true;
    private ConnectorType connector = ConnectorType.HTML;

    /** Human-facing career page, shown in the UI and used as a fallback link. */
    private String careerUrl;

    /** Search phrases sent to the upstream API (Workday, Greenhouse, ...). */
    private List<String> searchTerms = new ArrayList<>();

    /** Hard cap on postings pulled per crawl so a single company cannot flood the index. */
    private int maxJobs = 200;

    /** Cap on per-job detail requests, which are the expensive calls. */
    private int maxDetailFetches = 60;

    private Map<String, String> options = new LinkedHashMap<>();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public ConnectorType getConnector() {
        return connector;
    }

    public void setConnector(ConnectorType connector) {
        this.connector = connector;
    }

    public String getCareerUrl() {
        return careerUrl;
    }

    public void setCareerUrl(String careerUrl) {
        this.careerUrl = careerUrl;
    }

    public List<String> getSearchTerms() {
        return searchTerms;
    }

    public void setSearchTerms(List<String> searchTerms) {
        this.searchTerms = searchTerms;
    }

    public int getMaxJobs() {
        return maxJobs;
    }

    public void setMaxJobs(int maxJobs) {
        this.maxJobs = maxJobs;
    }

    public int getMaxDetailFetches() {
        return maxDetailFetches;
    }

    public void setMaxDetailFetches(int maxDetailFetches) {
        this.maxDetailFetches = maxDetailFetches;
    }

    public Map<String, String> getOptions() {
        return options;
    }

    public void setOptions(Map<String, String> options) {
        this.options = options;
    }

    public String option(String key, String defaultValue) {
        String value = options.get(key);
        return value == null || value.isBlank() ? defaultValue : value;
    }

    public String requiredOption(String key) {
        String value = options.get(key);
        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    "Company '" + id + "' is missing required option '" + key + "'");
        }
        return value;
    }
}
