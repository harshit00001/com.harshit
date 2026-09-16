package com.harshit.docqa.rag;

import com.harshit.docqa.config.RagProperties;
import com.harshit.docqa.rag.embed.EmbeddingClient;
import com.harshit.docqa.rag.store.VectorStore;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The ingest path: load documents, chunk them, embed once, and swap the index in.
 * <p>
 * Embedding happens here and never on the query path, because embedding the corpus is the expensive
 * part. One question needs one embedding call; the 50 passages it searches were embedded once at
 * index time.
 */
@Service
public class IndexService {

    private static final Logger log = LoggerFactory.getLogger(IndexService.class);

    private final DocumentLoader loader;
    private final Chunker chunker;
    private final EmbeddingClient embeddingClient;
    private final VectorStore vectorStore;
    private final RagProperties properties;

    private volatile IndexStats lastRun = new IndexStats(0, 0, 0, "not indexed yet");

    public IndexService(DocumentLoader loader, Chunker chunker, EmbeddingClient embeddingClient,
                        VectorStore vectorStore, RagProperties properties) {
        this.loader = loader;
        this.chunker = chunker;
        this.embeddingClient = embeddingClient;
        this.vectorStore = vectorStore;
        this.properties = properties;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void indexOnStartup() {
        reindex();
    }

    public IndexStats reindex() {
        long start = System.nanoTime();
        Path root = Path.of(properties.getDocsPath());
        List<DocumentLoader.LoadedDocument> documents = loader.load(root);

        List<Chunk> chunks = new ArrayList<>();
        for (DocumentLoader.LoadedDocument document : documents) {
            chunks.addAll(chunker.chunk(document.source(), document.content(),
                    properties.getChunkSize(), properties.getChunkOverlap()));
        }

        List<String> texts = chunks.stream().map(Chunk::text).toList();
        // Corpus-statistics embedders (TF-IDF) need to see the corpus before vectorising it;
        // trained models treat this as a no-op.
        embeddingClient.fit(texts);
        List<float[]> embeddings = chunks.isEmpty() ? List.of() : embeddingClient.embedAll(texts);
        vectorStore.replaceAll(chunks, embeddings);

        long millis = (System.nanoTime() - start) / 1_000_000L;
        lastRun = new IndexStats(documents.size(), chunks.size(), millis,
                "indexed with " + embeddingClient.describe());
        log.info("indexed {} documents into {} chunks from {} in {} ms",
                documents.size(), chunks.size(), root.toAbsolutePath(), millis);
        return lastRun;
    }

    public IndexStats lastRun() {
        return lastRun;
    }

    public Map<String, Integer> chunksBySource() {
        return vectorStore.chunksBySource();
    }

    public record IndexStats(int documents, int chunks, long millis, String detail) {
    }
}
