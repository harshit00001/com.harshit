package com.harshit.jobpulse.pipeline;

import com.harshit.jobpulse.config.JobPulseProperties;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Detects configured skills in free text.
 *
 * <p>Matching is alias-aware and word-boundary aware, which matters for short tokens:
 * a naive {@code contains("java")} also fires on "JavaScript", and {@code contains("go")}
 * fires on "algorithms".
 */
@Component
public class SkillExtractor {

    private final JobPulseProperties properties;

    public SkillExtractor(JobPulseProperties properties) {
        this.properties = properties;
    }

    public Set<String> extract(String text, List<String> skills) {
        Set<String> found = new LinkedHashSet<>();
        if (text == null || text.isBlank()) {
            return found;
        }
        String haystack = text.toLowerCase(Locale.ROOT);

        for (String skill : skills) {
            if (matches(haystack, skill)) {
                found.add(skill);
            }
        }
        return found;
    }

    private boolean matches(String haystack, String skill) {
        for (String alias : aliasesFor(skill)) {
            if (containsWord(haystack, alias)) {
                return true;
            }
        }
        return false;
    }

    private List<String> aliasesFor(String skill) {
        Map<String, List<String>> aliases = properties.getProfile().getAliases();
        List<String> configured = aliases.get(skill);
        if (configured != null && !configured.isEmpty()) {
            return configured;
        }
        return List.of(skill);
    }

    private boolean containsWord(String haystack, String needle) {
        String token = needle.toLowerCase(Locale.ROOT).trim();
        if (token.isEmpty()) {
            return false;
        }
        // Trailing digits stay allowed so "java8" matches Java, while a trailing letter
        // does not, keeping "javascript" out of the Java bucket.
        Pattern pattern = Pattern.compile(
                "(?<![a-z0-9+#.])" + Pattern.quote(token) + "(?![a-z+#])");
        Matcher matcher = pattern.matcher(haystack);
        return matcher.find();
    }
}
