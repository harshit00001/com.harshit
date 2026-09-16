package com.harshit.jobpulse.config;

import java.util.ArrayList;
import java.util.List;

/**
 * Geography gate applied before scoring. Defaults are tuned for India-only search:
 * postings that resolve to another country are dropped even if the skills match.
 */
public class LocationFilterConfig {

    private boolean enabled = true;

    /** Country names accepted as-is (case-insensitive). */
    private List<String> countries = new ArrayList<>();

    /** Cities/regions treated as India when the upstream feed omits a country. */
    private List<String> cities = new ArrayList<>();

    /** Country names that immediately disqualify a posting. */
    private List<String> blockedCountries = new ArrayList<>();

    /**
     * When a posting has no usable country or city, keep it (true) or drop it (false).
     * Dropping keeps the index clean; keeping is useful while onboarding a new connector.
     */
    private boolean keepUnknownLocations = false;

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public List<String> getCountries() {
        return countries;
    }

    public void setCountries(List<String> countries) {
        this.countries = countries;
    }

    public List<String> getCities() {
        return cities;
    }

    public void setCities(List<String> cities) {
        this.cities = cities;
    }

    public List<String> getBlockedCountries() {
        return blockedCountries;
    }

    public void setBlockedCountries(List<String> blockedCountries) {
        this.blockedCountries = blockedCountries;
    }

    public boolean isKeepUnknownLocations() {
        return keepUnknownLocations;
    }

    public void setKeepUnknownLocations(boolean keepUnknownLocations) {
        this.keepUnknownLocations = keepUnknownLocations;
    }
}
