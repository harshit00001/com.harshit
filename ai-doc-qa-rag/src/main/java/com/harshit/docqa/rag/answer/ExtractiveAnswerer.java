package com.harshit.docqa.rag.answer;

import com.harshit.docqa.rag.Text;
import com.harshit.docqa.rag.store.ScoredChunk;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * The offline answerer: instead of generating prose, it selects the sentences from the retrieved
 * passages that overlap most with the question and quotes them verbatim with citations.
 * <p>
 * Worth understanding as a deliberate trade rather than a stand-in. Extractive answering cannot
 * hallucinate — every word shipped came from a source document — but it cannot synthesise across
 * passages or rephrase either. That is exactly the trade-off an LLM makes in reverse, which makes
 * this a useful baseline to compare a generated answer against.
 */
public class ExtractiveAnswerer implements Answerer {

    private static final int MAX_SENTENCES = 3;

    /**
     * A sentence is kept only if it scores at least this fraction of the best sentence, so a
     * question with one good answer gets one sentence instead of being padded with near-misses.
     */
    private static final double RELATIVE_CUTOFF = 0.6;

    @Override
    public String answer(String question, List<ScoredChunk> context) {
        Set<String> questionTerms = terms(question);
        List<Scored> candidates = new ArrayList<>();

        for (ScoredChunk scored : context) {
            for (String sentence : splitSentences(scored.chunk().text())) {
                if (sentence.length() < 30) {
                    continue;
                }
                double overlap = overlap(questionTerms, terms(sentence));
                if (overlap > 0) {
                    // Passage similarity breaks ties, so sentences from the best passage win.
                    candidates.add(new Scored(sentence, overlap + scored.score() * 0.2,
                            scored.chunk().source()));
                }
            }
        }

        if (candidates.isEmpty()) {
            return PromptBuilder.NOT_FOUND_TOKEN;
        }

        candidates.sort(Comparator.comparingDouble(Scored::score).reversed());
        double cutoff = candidates.get(0).score() * RELATIVE_CUTOFF;
        StringBuilder answer = new StringBuilder();
        Set<String> used = new HashSet<>();
        for (Scored candidate : candidates) {
            if (used.size() >= MAX_SENTENCES || candidate.score() < cutoff) {
                break;
            }
            if (!used.add(candidate.sentence())) {
                continue;
            }
            answer.append(candidate.sentence().strip())
                    .append(" [").append(candidate.source()).append("]\n");
        }
        return answer.toString().strip();
    }

    @Override
    public String describe() {
        return "extractive(no model: quotes the best-matching sentences, cannot hallucinate)";
    }

    private List<String> splitSentences(String text) {
        // Line breaks are collapsed first: markdown is hard-wrapped, so splitting on newlines would
        // cut sentences in half and the answer would start mid-clause.
        String flowing = text.replaceAll("\\R+", " ").replaceAll(" {2,}", " ");
        return Arrays.stream(flowing.split("(?<=[.!?])\\s+"))
                .map(String::strip)
                .filter(s -> !s.isEmpty())
                .toList();
    }

    private Set<String> terms(String text) {
        return Text.uniqueTokens(text);
    }

    private double overlap(Set<String> questionTerms, Set<String> sentenceTerms) {
        if (questionTerms.isEmpty()) {
            return 0;
        }
        int matches = 0;
        for (String term : questionTerms) {
            if (sentenceTerms.contains(term)) {
                matches++;
            }
        }
        return matches / (double) questionTerms.size();
    }

    private record Scored(String sentence, double score, String source) {
    }
}
