package com.harshit.docqa.rag;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class ChunkerTest {

    private final Chunker chunker = new Chunker();

    @Test
    void keepsHeadingWithItsBodySoChunksStayOnOneTopic() {
        String content = """
                # Garbage Collection
                Minor collections clean the young generation.

                # Transactions
                REQUIRES_NEW suspends the caller.
                """;

        List<Chunk> chunks = chunker.chunk("notes.md", content, 900, 150);

        assertThat(chunks).hasSize(2);
        assertThat(chunks.get(0).heading()).isEqualTo("Garbage Collection");
        assertThat(chunks.get(0).text()).contains("young generation").doesNotContain("REQUIRES_NEW");
        assertThat(chunks.get(1).heading()).isEqualTo("Transactions");
    }

    @Test
    void overlapsWindowsSoASentenceIsNeverLostOnABoundary() {
        String body = ("sentence about kafka consumers and offsets. ").repeat(60);

        List<Chunk> chunks = chunker.chunk("big.md", "# Topic\n" + body, 300, 100);

        assertThat(chunks).hasSizeGreaterThan(1);
        for (Chunk chunk : chunks) {
            assertThat(chunk.text().length()).isLessThanOrEqualTo(300);
        }
        // Consecutive windows must share text, which is what overlap buys.
        String first = chunks.get(0).text();
        String second = chunks.get(1).text();
        String tail = first.substring(Math.max(0, first.length() - 40));
        assertThat(second).contains(tail.strip().split("\\s+")[0]);
    }

    @Test
    void assignsStableIdsAndCitationLabels() {
        List<Chunk> chunks = chunker.chunk("java-gc.md", "# Heap\nEden fills up.\n", 900, 150);

        assertThat(chunks.get(0).id()).isEqualTo("java-gc.md#0");
        assertThat(chunks.get(0).citationLabel()).isEqualTo("java-gc.md > Heap");
    }

    @Test
    void handlesDocumentWithNoHeadings() {
        List<Chunk> chunks = chunker.chunk("plain.txt", "just some text without headings", 900, 150);

        assertThat(chunks).hasSize(1);
        assertThat(chunks.get(0).citationLabel()).isEqualTo("plain.txt");
    }
}
