package com.harshit.docqa.rag.store;

import com.harshit.docqa.rag.Chunk;
import com.harshit.docqa.rag.embed.Vectors;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * Brute-force similarity search. Exact, with no index to tune, and linear in the number of chunks.
 * <p>
 * That is the right trade for a corpus of this size: a few thousand chunks scored with a dot product
 * is sub-millisecond work, and an approximate index (HNSW, IVFFlat) only starts to pay off in the
 * hundreds of thousands, where it buys speed by giving up exact recall.
 */
@Component
public class InMemoryVectorStore implements VectorStore {

    private volatile List<Entry> entries = List.of();

    @Override
    public synchronized void replaceAll(List<Chunk> chunks, List<float[]> embeddings) {
        if (chunks.size() != embeddings.size()) {
            throw new IllegalArgumentException(
                    "chunks (" + chunks.size() + ") and embeddings (" + embeddings.size() + ") differ");
        }
        List<Entry> rebuilt = new ArrayList<>(chunks.size());
        for (int i = 0; i < chunks.size(); i++) {
            rebuilt.add(new Entry(chunks.get(i), embeddings.get(i)));
        }
        // Swapped atomically so queries never observe a half-built index.
        this.entries = List.copyOf(rebuilt);
    }

    @Override
    public List<ScoredChunk> search(float[] queryEmbedding, int topK) {
        List<Entry> snapshot = entries;
        PriorityQueue<ScoredChunk> best =
                new PriorityQueue<>(Comparator.comparingDouble(ScoredChunk::score));
        for (Entry entry : snapshot) {
            double score = Vectors.cosine(queryEmbedding, entry.embedding());
            if (best.size() < topK) {
                best.offer(new ScoredChunk(entry.chunk(), score));
            } else if (best.peek() != null && score > best.peek().score()) {
                best.poll();
                best.offer(new ScoredChunk(entry.chunk(), score));
            }
        }
        List<ScoredChunk> results = new ArrayList<>(best);
        results.sort(Comparator.comparingDouble(ScoredChunk::score).reversed());
        return results;
    }

    @Override
    public int size() {
        return entries.size();
    }

    @Override
    public Map<String, Integer> chunksBySource() {
        Map<String, Integer> counts = new LinkedHashMap<>();
        for (Entry entry : entries) {
            counts.merge(entry.chunk().source(), 1, Integer::sum);
        }
        return counts;
    }

    private record Entry(Chunk chunk, float[] embedding) {
    }
}
