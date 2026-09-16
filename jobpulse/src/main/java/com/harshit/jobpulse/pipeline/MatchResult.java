package com.harshit.jobpulse.pipeline;

import java.util.List;

/** Outcome of scoring one posting against the configured skill profile. */
public record MatchResult(
        int score,
        boolean matched,
        List<String> matchedSkills,
        List<String> missingMustHaveSkills,
        String breakdown) {
}
