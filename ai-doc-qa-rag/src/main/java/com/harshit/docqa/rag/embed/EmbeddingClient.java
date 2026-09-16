package com.harshit.docqa.rag.embed;

import java.util.List;

/**
 * Turns text into a vector. The whole point of this interface is that retrieval quality, cost and
 * latency can be traded off by swapping the implementation, without the RAG pipeline changing.
 */
public interface EmbeddingClient {

    float[] embed(String text);

    /**
     * Called once at index time with the whole corpus. Trained models ignore this; corpus-statistics
     * models such as TF-IDF need it, because term weights only mean something relative to a corpus.
     */
    default void fit(List<String> corpus) {
    }

    /** Overridden by remote providers that support batching, which is where the cost savings are. */
    default List<float[]> embedAll(List<String> texts) {
        return texts.stream().map(this::embed).toList();
    }

    /** Reported in the API response so a demo always shows which provider actually ran. */
    String describe();
}
