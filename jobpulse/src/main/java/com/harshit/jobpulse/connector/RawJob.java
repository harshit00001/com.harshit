package com.harshit.jobpulse.connector;

import java.time.Instant;

/**
 * Connector output, normalised across every career-page vendor.
 * Downstream filtering and scoring only ever sees this shape.
 */
public class RawJob {

    private String externalId;
    private String title;
    private String locationText;
    private String city;
    private String country;
    private String url;
    private String applyUrl;
    private String description;
    private String employmentType;
    private String department;
    private boolean remote;
    private Instant publishedAt;
    private String postedOnText;

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocationText() {
        return locationText;
    }

    public void setLocationText(String locationText) {
        this.locationText = locationText;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getApplyUrl() {
        return applyUrl;
    }

    public void setApplyUrl(String applyUrl) {
        this.applyUrl = applyUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEmploymentType() {
        return employmentType;
    }

    public void setEmploymentType(String employmentType) {
        this.employmentType = employmentType;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public boolean isRemote() {
        return remote;
    }

    public void setRemote(boolean remote) {
        this.remote = remote;
    }

    public Instant getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(Instant publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getPostedOnText() {
        return postedOnText;
    }

    public void setPostedOnText(String postedOnText) {
        this.postedOnText = postedOnText;
    }

    /** Everything searchable about a posting, used for skill extraction. */
    public String searchableText() {
        StringBuilder text = new StringBuilder();
        append(text, title);
        append(text, department);
        append(text, locationText);
        append(text, description);
        return text.toString();
    }

    private void append(StringBuilder target, String value) {
        if (value != null && !value.isBlank()) {
            target.append(value).append('\n');
        }
    }
}
