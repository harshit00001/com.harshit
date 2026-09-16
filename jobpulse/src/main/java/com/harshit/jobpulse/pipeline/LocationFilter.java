package com.harshit.jobpulse.pipeline;

import com.harshit.jobpulse.config.JobPulseProperties;
import com.harshit.jobpulse.config.LocationFilterConfig;
import com.harshit.jobpulse.connector.RawJob;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Locale;
import java.util.Set;

/**
 * Geography gate. Runs before scoring so non-India postings never reach the index,
 * regardless of how well their skills match.
 *
 * <p>Decision order:
 * <ol>
 *   <li>Explicit country from the feed wins — matched against the allow list, then the block list.</li>
 *   <li>Otherwise the free-text location is checked against the configured India city list.</li>
 *   <li>If neither resolves, {@code keepUnknownLocations} decides.</li>
 * </ol>
 */
@Component
public class LocationFilter {

    private final JobPulseProperties properties;

    public LocationFilter(JobPulseProperties properties) {
        this.properties = properties;
    }

    public Decision evaluate(RawJob job) {
        LocationFilterConfig config = properties.getLocationFilter();
        if (!config.isEnabled()) {
            return Decision.accepted("location filter disabled");
        }

        Set<String> allowedCountries = lowercase(config.getCountries());
        Set<String> blockedCountries = lowercase(config.getBlockedCountries());
        Set<String> cities = lowercase(config.getCities());

        String country = normalise(job.getCountry());
        if (country != null) {
            if (allowedCountries.stream().anyMatch(country::contains)) {
                return Decision.accepted("country=" + job.getCountry());
            }
            if (blockedCountries.stream().anyMatch(country::contains)) {
                return Decision.rejected("country=" + job.getCountry());
            }
        }

        String haystack = joinLocationText(job);
        if (haystack != null) {
            if (allowedCountries.stream().anyMatch(haystack::contains)) {
                return Decision.accepted("location mentions allowed country");
            }
            String matchedCity = cities.stream().filter(haystack::contains).findFirst().orElse(null);
            if (matchedCity != null) {
                return Decision.accepted("city=" + matchedCity);
            }
            if (blockedCountries.stream().anyMatch(haystack::contains)) {
                return Decision.rejected("location mentions blocked country");
            }
        }

        return config.isKeepUnknownLocations()
                ? Decision.accepted("unknown location kept by config")
                : Decision.rejected("location could not be resolved to India");
    }

    private String joinLocationText(RawJob job) {
        StringBuilder text = new StringBuilder();
        if (job.getLocationText() != null) {
            text.append(job.getLocationText()).append(' ');
        }
        if (job.getCity() != null) {
            text.append(job.getCity()).append(' ');
        }
        return text.isEmpty() ? null : text.toString().toLowerCase(Locale.ROOT);
    }

    private String normalise(String value) {
        return value == null || value.isBlank() ? null : value.toLowerCase(Locale.ROOT).trim();
    }

    private Set<String> lowercase(Iterable<String> values) {
        Set<String> result = new LinkedHashSet<>();
        for (String value : values) {
            if (value != null && !value.isBlank()) {
                result.add(value.toLowerCase(Locale.ROOT).trim());
            }
        }
        return result;
    }

    public record Decision(boolean accepted, String reason) {

        static Decision accepted(String reason) {
            return new Decision(true, reason);
        }

        static Decision rejected(String reason) {
            return new Decision(false, reason);
        }
    }
}
