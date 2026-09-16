package com.harshit.jobpulse.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@ConfigurationProperties(prefix = "jobpulse")
public class JobPulseProperties {

    private List<CompanyConfig> companies = new ArrayList<>();
    private SkillProfile profile = new SkillProfile();
    private LocationFilterConfig locationFilter = new LocationFilterConfig();
    private CrawlConfig crawl = new CrawlConfig();

    public List<CompanyConfig> getCompanies() {
        return companies;
    }

    public void setCompanies(List<CompanyConfig> companies) {
        this.companies = companies;
    }

    public SkillProfile getProfile() {
        return profile;
    }

    public void setProfile(SkillProfile profile) {
        this.profile = profile;
    }

    public LocationFilterConfig getLocationFilter() {
        return locationFilter;
    }

    public void setLocationFilter(LocationFilterConfig locationFilter) {
        this.locationFilter = locationFilter;
    }

    public CrawlConfig getCrawl() {
        return crawl;
    }

    public void setCrawl(CrawlConfig crawl) {
        this.crawl = crawl;
    }

    public List<CompanyConfig> enabledCompanies() {
        return companies.stream().filter(CompanyConfig::isEnabled).toList();
    }

    public Optional<CompanyConfig> findCompany(String id) {
        return companies.stream().filter(company -> company.getId().equalsIgnoreCase(id)).findFirst();
    }
}
