package com.harshit.docqa.eval;

import java.util.List;

public record EvalReport(
        int totalCases,
        int inScopeCases,
        int answeredWithCorrectCitation,
        int outOfScopeCases,
        int correctlyRefused,
        long averageLatencyMillis,
        double minScoreThreshold,
        List<CaseResult> cases) {

    public String citationAccuracy() {
        return inScopeCases == 0 ? "n/a"
                : "%d/%d (%.0f%%)".formatted(answeredWithCorrectCitation, inScopeCases,
                answeredWithCorrectCitation * 100.0 / inScopeCases);
    }

    public String refusalAccuracy() {
        return outOfScopeCases == 0 ? "n/a"
                : "%d/%d (%.0f%%)".formatted(correctlyRefused, outOfScopeCases,
                correctlyRefused * 100.0 / outOfScopeCases);
    }

    public record CaseResult(String question, String expected, boolean answered, String topSource,
                             double topScore, boolean pass) {
    }
}
