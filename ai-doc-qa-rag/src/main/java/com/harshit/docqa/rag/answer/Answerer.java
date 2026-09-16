package com.harshit.docqa.rag.answer;

import com.harshit.docqa.rag.store.ScoredChunk;

import java.util.List;

/**
 * Produces the final answer from retrieved context. Swappable for the same reason as the embedding
 * client: the pipeline should not care whether a language model or plain extraction wrote the text.
 */
public interface Answerer {

    String answer(String question, List<ScoredChunk> context);

    String describe();
}
