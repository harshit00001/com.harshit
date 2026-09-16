package com.harshit.docqa.rag.store;

import com.harshit.docqa.rag.Chunk;

import java.util.List;
import java.util.Map;

/**
 * The seam where a real vector database goes.
 * <p>
 * {@link InMemoryVectorStore} does a brute-force scan, which is correct and fast enough for
 * thousands of chunks. Swapping in PostgreSQL + pgvector means implementing this one interface with
 * {@code ORDER BY embedding <=> :query LIMIT :k} and an HNSW index — the rest of the pipeline does
 * not change.
 */
public interface VectorStore {

    void replaceAll(List<Chunk> chunks, List<float[]> embeddings);

    List<ScoredChunk> search(float[] queryEmbedding, int topK);

    int size();

    /** Chunk counts per source document, for the /documents endpoint. */
    Map<String, Integer> chunksBySource();
}
