package com.harshit.docqa.eval;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.harshit.docqa.config.RagProperties;
import com.harshit.docqa.rag.AnswerResult;
import com.harshit.docqa.rag.RagService;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Measures whether the pipeline actually works, which is the difference between a demo and a
 * project worth talking about.
 * <p>
 * Two metrics, deliberately chosen because they capture the two ways RAG fails:
 * <ul>
 *   <li><b>Citation accuracy</b> on in-scope questions: did it answer, and did it cite the document
 *       that really contains the answer? An answer with the wrong citation is worse than no answer.</li>
 *   <li><b>Refusal rate</b> on out-of-scope questions: did the similarity floor hold, or did it
 *       invent something from unrelated passages?</li>
 * </ul>
 * Tuning {@code rag.min-score} trades these against each other: raise it and refusals grow, lower it
 * and hallucination risk grows. Being able to say that with numbers is the whole point.
 */
@Service
public class EvaluationHarness {

    private final RagService ragService;
    private final RagProperties properties;
    private final ObjectMapper mapper = new ObjectMapper();

    public EvaluationHarness(RagService ragService, RagProperties properties) {
        this.ragService = ragService;
        this.properties = properties;
    }

    public EvalReport run() {
        List<EvalCase> cases = loadCases(Path.of(properties.getEvalFile()));
        List<EvalReport.CaseResult> results = new ArrayList<>();

        int inScope = 0;
        int answeredWithCorrectCitation = 0;
        int outOfScope = 0;
        int correctlyRefused = 0;
        long totalLatency = 0;

        for (EvalCase testCase : cases) {
            AnswerResult result = ragService.ask(testCase.question());
            totalLatency += result.latencyMillis();

            boolean expectedAnswerable = testCase.expectedSource() != null
                    && !testCase.expectedSource().isBlank();
            // Deliberately strict: the *top-ranked* passage must come from the expected document.
            // Scoring "expected document appears anywhere in top-k" would flatter the retriever.
            boolean citedExpected = expectedAnswerable
                    && !result.citations().isEmpty()
                    && result.citations().get(0).source().equalsIgnoreCase(testCase.expectedSource());
            boolean pass;

            if (expectedAnswerable) {
                inScope++;
                pass = result.answered() && citedExpected;
                if (pass) {
                    answeredWithCorrectCitation++;
                }
            } else {
                outOfScope++;
                pass = !result.answered();
                if (pass) {
                    correctlyRefused++;
                }
            }

            results.add(new EvalReport.CaseResult(
                    testCase.question(),
                    expectedAnswerable ? testCase.expectedSource() : "(should refuse)",
                    result.answered(),
                    result.citations().isEmpty() ? "-" : result.citations().get(0).source(),
                    result.topScore(),
                    pass));
        }

        return new EvalReport(
                cases.size(), inScope, answeredWithCorrectCitation, outOfScope, correctlyRefused,
                cases.isEmpty() ? 0 : totalLatency / cases.size(),
                properties.getMinScore(), results);
    }

    /**
     * Sweeps the similarity floor over a range and reports what each value would score.
     * <p>
     * The guardrail is a single comparison against the top similarity, so one retrieval pass is
     * enough to evaluate every candidate threshold — no need to restart the service per value. This
     * is how {@code rag.min-score} was chosen instead of guessed, and the shape of the result is the
     * interesting part: where the two curves cross is the precision/recall trade for this corpus.
     */
    public List<SweepRow> sweep(double from, double to, double step) {
        List<EvalCase> cases = loadCases(Path.of(properties.getEvalFile()));
        List<Probe> probes = new ArrayList<>();
        for (EvalCase testCase : cases) {
            var retrieved = ragService.retrieve(testCase.question(), properties.getTopK());
            probes.add(new Probe(
                    testCase.expectedSource(),
                    retrieved.isEmpty() ? "-" : retrieved.get(0).chunk().source(),
                    retrieved.isEmpty() ? 0 : retrieved.get(0).score()));
        }

        List<SweepRow> rows = new ArrayList<>();
        for (double threshold = from; threshold <= to + 1e-9; threshold += step) {
            int inScope = 0;
            int answeredCorrectly = 0;
            int outOfScope = 0;
            int refused = 0;
            for (Probe probe : probes) {
                boolean answerable = probe.expectedSource() != null && !probe.expectedSource().isBlank();
                if (answerable) {
                    inScope++;
                    if (probe.topScore() >= threshold
                            && probe.topSource().equalsIgnoreCase(probe.expectedSource())) {
                        answeredCorrectly++;
                    }
                } else {
                    outOfScope++;
                    if (probe.topScore() < threshold) {
                        refused++;
                    }
                }
            }
            rows.add(new SweepRow(round(threshold), answeredCorrectly, inScope, refused, outOfScope,
                    answeredCorrectly + refused));
        }
        return rows;
    }

    private double round(double value) {
        return Math.round(value * 1000) / 1000.0;
    }

    public record SweepRow(double threshold, int answeredWithCorrectCitation, int inScopeCases,
                           int correctlyRefused, int outOfScopeCases, int totalPassing) {
    }

    private record Probe(String expectedSource, String topSource, double topScore) {
    }

    private List<EvalCase> loadCases(Path path) {
        try {
            if (!Files.exists(path)) {
                throw new IllegalStateException("eval file not found: " + path.toAbsolutePath());
            }
            return List.of(mapper.readValue(Files.readString(path), EvalCase[].class));
        } catch (IOException e) {
            throw new UncheckedIOException("could not read eval file " + path, e);
        }
    }
}
