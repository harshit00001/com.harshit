package com.harshit.docqa.eval;

import com.harshit.docqa.config.RagProperties;
import com.harshit.docqa.rag.Chunker;
import com.harshit.docqa.rag.DocumentLoader;
import com.harshit.docqa.rag.IndexService;
import com.harshit.docqa.rag.RagService;
import com.harshit.docqa.rag.answer.ExtractiveAnswerer;
import com.harshit.docqa.rag.answer.PromptBuilder;
import com.harshit.docqa.rag.embed.LocalHashingEmbeddingClient;
import com.harshit.docqa.rag.store.InMemoryVectorStore;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Runs the real corpus through the real labelled question set, so a regression in chunking,
 * retrieval or the threshold fails the build rather than being discovered in a demo.
 */
class EvaluationHarnessTest {

    @Test
    void meetsTheQualityBarOnTheLabelledQuestionSet() {
        RagProperties properties = new RagProperties();
        properties.setDocsPath("./docs");
        properties.setEvalFile("./eval/questions.json");

        LocalHashingEmbeddingClient embeddings = new LocalHashingEmbeddingClient();
        InMemoryVectorStore store = new InMemoryVectorStore();
        IndexService indexService = new IndexService(new DocumentLoader(), new Chunker(),
                embeddings, store, properties);
        indexService.reindex();

        RagService ragService = new RagService(embeddings, store, new ExtractiveAnswerer(),
                new PromptBuilder(), properties);
        EvalReport report = new EvaluationHarness(ragService, properties).run();

        assertThat(store.size()).isGreaterThan(5);
        // In-scope questions must be answered from the right document...
        assertThat(report.answeredWithCorrectCitation())
                .as("citation accuracy was %s", report.citationAccuracy())
                .isGreaterThanOrEqualTo((int) Math.ceil(report.inScopeCases() * 0.8));
        // ...and every out-of-scope question must be refused, which is the non-negotiable one.
        assertThat(report.correctlyRefused())
                .as("refusal accuracy was %s", report.refusalAccuracy())
                .isEqualTo(report.outOfScopeCases());
    }
}
