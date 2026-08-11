package com.harshit.jobhunter.source;

import com.harshit.jobhunter.config.JobHunterProperties;
import com.harshit.jobhunter.model.JobPosting;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Bookmark sources (IBM careers, etc.) — no public API; stores search link as a reminder row.
 */
@Component
public class BookmarkJobAdapter implements JobSourceAdapter {

    @Override
    public boolean supports(JobHunterProperties.Source source) {
        return "bookmark".equalsIgnoreCase(source.getType());
    }

    @Override
    public List<JobPosting> fetch(JobHunterProperties.Source source) {
        if (source.getSearchUrl() == null || source.getSearchUrl().isBlank()) {
            return Collections.emptyList();
        }
        String keywords = source.getKeywords() != null && !source.getKeywords().isEmpty()
                ? String.join(", ", source.getKeywords())
                : "java, software engineer";
        String title = "Manual search: " + source.getName() + " (" + keywords + ")";
        String description = "Automated fetch is not available for this site. "
                + "Open the apply URL and search with keywords: " + keywords + ". "
                + "Add Greenhouse/Lever board-token in application.yml when you find the ATS.";
        String id = UUID.nameUUIDFromBytes((source.getName() + source.getSearchUrl()).getBytes()).toString();
        JobPosting bookmark = new JobPosting(
                "bookmark",
                id,
                source.getName(),
                title,
                description,
                "See careers site",
                source.getSearchUrl(),
                Instant.now()
        );
        return List.of(bookmark);
    }
}
