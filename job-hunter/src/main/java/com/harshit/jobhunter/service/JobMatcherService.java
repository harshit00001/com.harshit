package com.harshit.jobhunter.service;

import com.harshit.jobhunter.config.JobHunterProperties;
import com.harshit.jobhunter.model.JobMatchResult;
import com.harshit.jobhunter.model.JobPosting;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class JobMatcherService {

    private static final Pattern SENIOR_YEARS = Pattern.compile("(\\d{1,2})\\+?\\s*(?:years|yrs|yoe)", Pattern.CASE_INSENSITIVE);

    private final JobHunterProperties properties;

    public JobMatcherService(JobHunterProperties properties) {
        this.properties = properties;
    }

    public JobMatchResult score(JobPosting job) {
        JobHunterProperties.Profile profile = properties.getProfile();
        String haystack = (job.getTitle() + " " + job.getDescription() + " " + nullToEmpty(job.getLocation()))
                .toLowerCase(Locale.ROOT);

        if (matchesAny(haystack, profile.getExcludeTitleKeywords()) || titleExcluded(job.getTitle(), profile)) {
            return null;
        }

        boolean titleOk = matchesAny(haystack, profile.getTitleKeywords());
        if (!titleOk) {
            return null;
        }

        List<String> matched = new ArrayList<>();
        for (String skill : profile.getMustHaveSkills()) {
            if (haystack.contains(skill.toLowerCase(Locale.ROOT))) {
                matched.add(skill);
            }
        }
        if (matched.size() < profile.getMustHaveSkills().size()) {
            return null;
        }

        for (String skill : profile.getStrongSkills()) {
            if (haystack.contains(skill.toLowerCase(Locale.ROOT)) && !matched.contains(skill)) {
                matched.add(skill);
            }
        }

        int score = matched.size() * 2;
        if (titleStrongMatch(job.getTitle(), profile)) {
            score += 3;
        }
        if (locationMatch(haystack, profile)) {
            score += 2;
        }
        if (!experienceLooksTooSenior(haystack, profile)) {
            score += 1;
        } else {
            score -= 5;
        }

        JobMatchResult result = new JobMatchResult();
        result.setId(job.getId());
        result.setCompany(job.getCompany());
        result.setTitle(job.getTitle());
        result.setLocation(job.getLocation());
        result.setApplyUrl(job.getApplyUrl());
        result.setPostedAt(job.getPostedAt());
        result.setSource(job.getSource());
        result.setScore(score);
        result.setMatchedSkills(matched);
        result.setSummary(buildSummary(score, matched));
        return result;
    }

    public List<JobMatchResult> rank(List<JobPosting> jobs) {
        return jobs.stream()
                .map(this::score)
                .filter(r -> r != null && r.getScore() > 0)
                .sorted(Comparator.comparingInt(JobMatchResult::getScore).reversed())
                .toList();
    }

    private boolean titleExcluded(String title, JobHunterProperties.Profile profile) {
        String t = title.toLowerCase(Locale.ROOT);
        for (String ex : profile.getExcludeTitleKeywords()) {
            if (t.contains(ex.toLowerCase(Locale.ROOT))) {
                return true;
            }
        }
        return false;
    }

    private boolean titleStrongMatch(String title, JobHunterProperties.Profile profile) {
        String t = title.toLowerCase(Locale.ROOT);
        return profile.getTitleKeywords().stream().anyMatch(k -> t.contains(k.toLowerCase(Locale.ROOT)));
    }

    private boolean locationMatch(String haystack, JobHunterProperties.Profile profile) {
        return profile.getLocationKeywords().stream()
                .anyMatch(k -> haystack.contains(k.toLowerCase(Locale.ROOT)));
    }

    private boolean experienceLooksTooSenior(String haystack, JobHunterProperties.Profile profile) {
        Matcher m = SENIOR_YEARS.matcher(haystack);
        while (m.find()) {
            try {
                int years = Integer.parseInt(m.group(1));
                if (years > profile.getMaxExperienceYears() + 2) {
                    return true;
                }
            } catch (NumberFormatException ignored) {
                // skip
            }
        }
        return haystack.contains("principal") || haystack.contains("staff engineer");
    }

    private boolean matchesAny(String haystack, List<String> keywords) {
        if (keywords == null) {
            return false;
        }
        return keywords.stream().anyMatch(k -> haystack.contains(k.toLowerCase(Locale.ROOT)));
    }

    private String buildSummary(int score, List<String> matched) {
        return "Score " + score + " — matched: " + String.join(", ", matched);
    }

    private String nullToEmpty(String s) {
        return s == null ? "" : s;
    }
}
