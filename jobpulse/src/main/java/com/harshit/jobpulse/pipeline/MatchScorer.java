package com.harshit.jobpulse.pipeline;

import com.harshit.jobpulse.config.JobPulseProperties;
import com.harshit.jobpulse.config.SkillProfile;
import com.harshit.jobpulse.connector.RawJob;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Transparent 0-100 relevance score. Every component is reported in the breakdown so a
 * result can be explained rather than just ranked.
 *
 * <pre>
 *   title relevance      0-30
 *   must-have coverage   0-45
 *   nice-to-have depth   0-20
 *   freshness            0-5
 *   exclusion penalty    up to -40
 * </pre>
 */
@Component
public class MatchScorer {

    private static final int TITLE_MAX = 30;
    private static final int MUST_HAVE_MAX = 45;
    private static final int NICE_TO_HAVE_MAX = 20;
    private static final int FRESHNESS_MAX = 5;
    private static final int TITLE_EXCLUSION_PENALTY = 40;

    private final JobPulseProperties properties;
    private final SkillExtractor skillExtractor;

    public MatchScorer(JobPulseProperties properties, SkillExtractor skillExtractor) {
        this.properties = properties;
        this.skillExtractor = skillExtractor;
    }

    public MatchResult score(RawJob job) {
        SkillProfile profile = properties.getProfile();
        String text = job.searchableText();
        String title = job.getTitle() == null ? "" : job.getTitle().toLowerCase(Locale.ROOT);

        Set<String> mustHaveFound = skillExtractor.extract(text, profile.getMustHaveSkills());
        Set<String> niceToHaveFound = skillExtractor.extract(text, profile.getNiceToHaveSkills());

        int titleScore = scoreTitle(title, profile);
        int mustHaveScore = profile.getMustHaveSkills().isEmpty()
                ? MUST_HAVE_MAX
                : Math.round((float) MUST_HAVE_MAX * mustHaveFound.size()
                        / profile.getMustHaveSkills().size());
        int niceToHaveScore = Math.min(NICE_TO_HAVE_MAX, niceToHaveFound.size() * 4);
        int freshnessScore = scoreFreshness(job.getPublishedAt());
        int penalty = titleExclusionPenalty(title, profile);

        int total = clamp(titleScore + mustHaveScore + niceToHaveScore + freshnessScore - penalty);

        List<String> missingMustHave = new ArrayList<>(profile.getMustHaveSkills());
        missingMustHave.removeAll(mustHaveFound);

        boolean matched = total >= profile.getMinScore()
                && mustHaveFound.size() >= profile.getMinMustHaveMatches()
                && penalty == 0;

        String breakdown = String.format(
                "title=%d/%d, mustHave=%d/%d (%d of %d), niceToHave=%d/%d (%d), freshness=%d/%d, penalty=-%d",
                titleScore, TITLE_MAX,
                mustHaveScore, MUST_HAVE_MAX, mustHaveFound.size(), profile.getMustHaveSkills().size(),
                niceToHaveScore, NICE_TO_HAVE_MAX, niceToHaveFound.size(),
                freshnessScore, FRESHNESS_MAX,
                penalty);

        Set<String> allMatched = new LinkedHashSet<>(mustHaveFound);
        allMatched.addAll(niceToHaveFound);

        return new MatchResult(total, matched, new ArrayList<>(allMatched), missingMustHave, breakdown);
    }

    private int scoreTitle(String title, SkillProfile profile) {
        if (title.isBlank()) {
            return 0;
        }
        for (String role : profile.getTargetRoles()) {
            if (title.contains(role.toLowerCase(Locale.ROOT))) {
                return TITLE_MAX;
            }
        }
        // A generic engineering title still deserves partial credit; the description
        // decides whether it is actually a Java backend role.
        if (title.contains("engineer") || title.contains("developer") || title.contains("architect")) {
            return TITLE_MAX / 2;
        }
        return 5;
    }

    private int scoreFreshness(Instant publishedAt) {
        if (publishedAt == null) {
            return 1;
        }
        long days = Duration.between(publishedAt, Instant.now()).toDays();
        if (days <= 7) {
            return FRESHNESS_MAX;
        }
        if (days <= 30) {
            return 3;
        }
        return 1;
    }

    private int titleExclusionPenalty(String title, SkillProfile profile) {
        for (String keyword : profile.getExcludeTitleKeywords()) {
            if (title.contains(keyword.toLowerCase(Locale.ROOT))) {
                return TITLE_EXCLUSION_PENALTY;
            }
        }
        return 0;
    }

    private int clamp(int value) {
        return Math.max(0, Math.min(100, value));
    }
}
