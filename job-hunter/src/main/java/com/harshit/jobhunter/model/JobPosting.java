package com.harshit.jobhunter.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import java.time.Instant;

@Entity
@Table(name = "job_postings", uniqueConstraints = @UniqueConstraint(columnNames = {"source", "externalId"}))
public class JobPosting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 64)
    private String source;

    @Column(nullable = false, length = 128)
    private String externalId;

    @Column(nullable = false, length = 256)
    private String company;

    @Column(nullable = false, length = 512)
    private String title;

    @Lob
    private String description;

    @Column(length = 256)
    private String location;

    @Column(nullable = false, length = 1024)
    private String applyUrl;

    private Instant postedAt;

    @Column(nullable = false)
    private Instant fetchedAt;

    protected JobPosting() {
    }

    public JobPosting(String source, String externalId, String company, String title,
                      String description, String location, String applyUrl, Instant postedAt) {
        this.source = source;
        this.externalId = externalId;
        this.company = company;
        this.title = title;
        this.description = description;
        this.location = location;
        this.applyUrl = applyUrl;
        this.postedAt = postedAt;
        this.fetchedAt = Instant.now();
    }

    public Long getId() {
        return id;
    }

    public String getSource() {
        return source;
    }

    public String getExternalId() {
        return externalId;
    }

    public String getCompany() {
        return company;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getLocation() {
        return location;
    }

    public String getApplyUrl() {
        return applyUrl;
    }

    public Instant getPostedAt() {
        return postedAt;
    }

    public Instant getFetchedAt() {
        return fetchedAt;
    }

    public void setFetchedAt(Instant fetchedAt) {
        this.fetchedAt = fetchedAt;
    }
}
