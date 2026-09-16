package com.harshit.docqa.api;

import com.harshit.docqa.eval.EvalReport;
import com.harshit.docqa.eval.EvaluationHarness;
import com.harshit.docqa.rag.AnswerResult;
import com.harshit.docqa.rag.IndexService;
import com.harshit.docqa.rag.RagService;
import com.harshit.docqa.rag.provider.JsonHttpClient;
import com.harshit.docqa.rag.store.ScoredChunk;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class RagController {

    private final RagService ragService;
    private final IndexService indexService;
    private final EvaluationHarness evaluationHarness;

    public RagController(RagService ragService, IndexService indexService,
                         EvaluationHarness evaluationHarness) {
        this.ragService = ragService;
        this.indexService = indexService;
        this.evaluationHarness = evaluationHarness;
    }

    @PostMapping("/ask")
    public AnswerResult ask(@Valid @RequestBody AskRequest request) {
        return ragService.ask(request.question());
    }

    /** Retrieval without answering, so you can see exactly what the answerer was handed. */
    @GetMapping("/search")
    public List<Map<String, Object>> search(@RequestParam String q,
                                            @RequestParam(defaultValue = "5") int topK) {
        return ragService.retrieve(q, topK).stream()
                .map(this::asMap)
                .toList();
    }

    @PostMapping("/index")
    public IndexService.IndexStats reindex() {
        return indexService.reindex();
    }

    @GetMapping("/documents")
    public Map<String, Object> documents() {
        return Map.of(
                "lastRun", indexService.lastRun(),
                "chunksBySource", indexService.chunksBySource());
    }

    @GetMapping("/evaluate")
    public EvalReport evaluate() {
        return evaluationHarness.run();
    }

    /** What every candidate similarity floor would have scored, from one retrieval pass. */
    @GetMapping("/evaluate/sweep")
    public List<EvaluationHarness.SweepRow> sweep(
            @RequestParam(defaultValue = "0.05") double from,
            @RequestParam(defaultValue = "0.40") double to,
            @RequestParam(defaultValue = "0.01") double step) {
        return evaluationHarness.sweep(from, to, step);
    }

    private Map<String, Object> asMap(ScoredChunk scored) {
        return Map.of(
                "source", scored.chunk().source(),
                "heading", scored.chunk().heading() == null ? "" : scored.chunk().heading(),
                "similarity", Math.round(scored.score() * 1000) / 1000.0,
                "text", scored.chunk().text());
    }

    @ExceptionHandler(JsonHttpClient.ProviderException.class)
    public ResponseEntity<Map<String, String>> onProviderFailure(JsonHttpClient.ProviderException e) {
        // A model provider is a remote dependency; surface it as 503 rather than a 500 stack trace.
        return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(Map.of("error", "model provider unavailable", "detail", e.getMessage()));
    }

    public record AskRequest(@NotBlank(message = "question must not be blank") String question) {
    }
}
