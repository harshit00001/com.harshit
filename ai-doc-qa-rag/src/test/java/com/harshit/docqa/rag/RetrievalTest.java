package com.harshit.docqa.rag;

import com.harshit.docqa.rag.embed.EmbeddingClient;
import com.harshit.docqa.rag.embed.LocalHashingEmbeddingClient;
import com.harshit.docqa.rag.embed.Vectors;
import com.harshit.docqa.rag.store.InMemoryVectorStore;
import com.harshit.docqa.rag.store.ScoredChunk;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class RetrievalTest {

    private final EmbeddingClient embeddings = new LocalHashingEmbeddingClient();
    private final InMemoryVectorStore store = new InMemoryVectorStore();

    @BeforeEach
    void indexThreeChunks() {
        List<Chunk> chunks = List.of(
                new Chunk("gc#0", "java-gc.md", "Promotion", 0,
                        "Objects surviving more young collections than MaxTenuringThreshold are promoted to the old generation."),
                new Chunk("cf#0", "completable-future.md", "Handlers", 1,
                        "The exceptionally method recovers with a fallback value while whenComplete only observes the failure."),
                new Chunk("ms#0", "spring-boot-microservices.md", "Transactions", 2,
                        "REQUIRES_NEW propagation suspends the caller transaction and starts an independent one."));

        embeddings.fit(chunks.stream().map(Chunk::text).toList());
        store.replaceAll(chunks, chunks.stream().map(c -> embeddings.embed(c.text())).toList());
    }

    @Test
    void retrievesTheChunkThatMatchesTheQuestion() {
        List<ScoredChunk> results = store.search(embeddings.embed("how does promotion to old generation work"), 3);

        assertThat(results.get(0).chunk().source()).isEqualTo("java-gc.md");
        assertThat(results.get(0).score()).isGreaterThan(results.get(1).score());
    }

    @Test
    void ranksAnUnrelatedQuestionFarBelowTheThreshold() {
        List<ScoredChunk> results = store.search(embeddings.embed("what is the capital of France"), 3);

        assertThat(results.get(0).score()).isLessThan(0.14);
    }

    @Test
    void normalisedVectorsMakeCosineEqualTheDotProduct() {
        float[] vector = embeddings.embed("kafka consumer offsets");

        double selfSimilarity = Vectors.cosine(vector, vector);

        assertThat(selfSimilarity).isCloseTo(1.0, org.assertj.core.data.Offset.offset(1e-6));
    }

    @Test
    void rejectsAQueryVectorFromADifferentModel() {
        assertThatThrownBy(() -> store.search(new float[7], 3))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("must be rebuilt");
    }
}
