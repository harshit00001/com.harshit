package com.harshit.jobpulse.api;

import com.harshit.jobpulse.domain.JobPosting;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

/** API projection of a stored posting. */
public record JobView(
        Long id,
        String company,
        String title,
        String location,
        String city,
        String country,
        boolean remote,
        String employmentType,
        String url,
        String applyUrl,
        int matchScore,
        boolean matched,
        List<String> matchedSkills,
        List<String> missingSkills,
        String scoreBreakdown,
        String postedOn,
        Instant publishedAt,
        Instant firstSeenAt,
        String descriptionSnippet) {

    public static JobView of(JobPosting posting, int snippetLength) {
        return new JobView(
                posting.getId(),
                posting.getCompanyName(),
                posting.getTitle(),
                posting.getLocationText(),
                posting.getCity(),
                posting.getCountry(),
                posting.isRemote(),
                posting.getEmploymentType(),
                posting.getUrl(),
                posting.getApplyUrl(),
                posting.getMatchScore(),
                posting.isMatched(),
                splitCsv(posting.getMatchedSkills()),
                splitCsv(posting.getMissingSkills()),
                posting.getScoreBreakdown(),
                posting.getPostedOnText(),
                posting.getPublishedAt(),
                posting.getFirstSeenAt(),
                snippet(posting.getDescriptionText(), snippetLength));
    }

    private static List<String> splitCsv(String value) {
        if (value == null || value.isBlank()) {
            return List.of();
        }
        return Arrays.stream(value.split("\\s*,\\s*")).filter(part -> !part.isBlank()).toList();
    }

    private static String snippet(String text, int length) {
        if (text == null || text.isBlank()) {
            return "";
        }
        String collapsed = text.replaceAll("\\s+", " ").trim();
        return collapsed.length() <= length ? collapsed : collapsed.substring(0, length) + "...";
    }
}
