package com.harshit.docqa.rag;

import java.util.List;

/**
 * The API response. It reports not just the answer but how it was produced — scores, providers and
 * timings — because a RAG answer nobody can audit is a liability.
 */
public record AnswerResult(
        String question,
        String answer,
        boolean answered,
        String reason,
        double topScore,
        List<Citation> citations,
        boolean injectionSuspected,
        long latencyMillis,
        String embeddingProvider,
        String answerProvider) {

    public record Citation(String source, String heading, double similarity, String excerpt) {
    }
}
