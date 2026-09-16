package com.harshit.docqa.rag;

import com.harshit.docqa.config.RagProperties;
import com.harshit.docqa.rag.answer.Answerer;
import com.harshit.docqa.rag.answer.PromptBuilder;
import com.harshit.docqa.rag.embed.EmbeddingClient;
import com.harshit.docqa.rag.store.ScoredChunk;
import com.harshit.docqa.rag.store.VectorStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * The RAG request path: embed the question, retrieve the nearest passages, refuse if they are not
 * close enough, otherwise answer from them and attach citations.
 * <p>
 * The refusal is the part that makes this production-shaped. A retriever always returns its nearest
 * neighbours, however far away they are — so without a similarity floor, an out-of-scope question
 * gets confidently answered from irrelevant passages. That is the failure mode users call
 * hallucination.
 */
@Service
public class RagService {

    private static final Logger log = LoggerFactory.getLogger(RagService.class);
    private static final int EXCERPT_LENGTH = 240;

    private final EmbeddingClient embeddingClient;
    private final VectorStore vectorStore;
    private final Answerer answerer;
    private final PromptBuilder prompts;
    private final RagProperties properties;

    public RagService(EmbeddingClient embeddingClient, VectorStore vectorStore, Answerer answerer,
                      PromptBuilder prompts, RagProperties properties) {
        this.embeddingClient = embeddingClient;
        this.vectorStore = vectorStore;
        this.answerer = answerer;
        this.prompts = prompts;
        this.properties = properties;
    }

    public AnswerResult ask(String question) {
        long start = System.nanoTime();

        if (vectorStore.size() == 0) {
            return refusal(question, "index is empty - POST /api/index to build it", 0, List.of(), start);
        }

        float[] queryEmbedding = embeddingClient.embed(question);
        List<ScoredChunk> retrieved = vectorStore.search(queryEmbedding, properties.getTopK());
        double topScore = retrieved.isEmpty() ? 0 : retrieved.get(0).score();

        if (topScore < properties.getMinScore()) {
            log.info("refusing '{}': top similarity {} is below the {} floor",
                    question, String.format("%.3f", topScore), properties.getMinScore());
            return refusal(question,
                    "no indexed passage was similar enough: top score %.3f is below the %.2f floor"
                            .formatted(topScore, properties.getMinScore()),
                    topScore, retrieved, start);
        }

        boolean injectionSuspected = prompts.looksLikeInjection(retrieved);
        if (injectionSuspected) {
            log.warn("retrieved context for '{}' contains suspected prompt-injection phrasing", question);
        }

        String answer = answerer.answer(question, retrieved);
        boolean modelDeclined = answer.isBlank() || answer.contains(PromptBuilder.NOT_FOUND_TOKEN);
        if (modelDeclined) {
            return refusal(question, "retrieved passages did not contain the answer",
                    topScore, retrieved, start);
        }

        return new AnswerResult(question, answer, true, "answered from " + retrieved.size() + " passages",
                topScore, citations(retrieved), injectionSuspected, millisSince(start),
                embeddingClient.describe(), answerer.describe());
    }

    /** Retrieval only, so you can inspect what the answerer was given. */
    public List<ScoredChunk> retrieve(String question, int topK) {
        if (vectorStore.size() == 0) {
            return List.of();
        }
        return vectorStore.search(embeddingClient.embed(question), topK);
    }

    private AnswerResult refusal(String question, String reason, double topScore,
                                 List<ScoredChunk> retrieved, long start) {
        return new AnswerResult(question,
                "I could not find this in the indexed documents.",
                false, reason, topScore, citations(retrieved), false, millisSince(start),
                embeddingClient.describe(), answerer.describe());
    }

    private List<AnswerResult.Citation> citations(List<ScoredChunk> retrieved) {
        return retrieved.stream()
                .map(scored -> new AnswerResult.Citation(
                        scored.chunk().source(),
                        scored.chunk().heading(),
                        round(scored.score()),
                        excerpt(scored.chunk().text())))
                .toList();
    }

    private String excerpt(String text) {
        String flat = text.replaceAll("\\s+", " ").strip();
        return flat.length() <= EXCERPT_LENGTH ? flat : flat.substring(0, EXCERPT_LENGTH) + "...";
    }

    private double round(double value) {
        return Math.round(value * 1000) / 1000.0;
    }

    private long millisSince(long startNanos) {
        return (System.nanoTime() - startNanos) / 1_000_000L;
    }
}
