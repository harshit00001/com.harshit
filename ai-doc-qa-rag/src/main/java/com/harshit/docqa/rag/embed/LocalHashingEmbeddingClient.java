package com.harshit.docqa.rag.embed;

import com.harshit.docqa.rag.Text;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * A dependency-free embedding so the whole pipeline runs with no model, no API key and no network.
 * <p>
 * It is a <b>TF-IDF vectoriser with feature hashing</b>: tokens and adjacent token pairs are weighted
 * by sub-linear term frequency times inverse document frequency, hashed into a fixed-width vector,
 * then L2-normalised. Cosine similarity over these vectors measures weighted lexical overlap, which
 * is close to what BM25 does.
 * <p>
 * The IDF term is not decoration. Without it every word counts equally, so a question containing
 * {@code MaxTenuringThreshold} scores no higher than one made of filler words, and the score
 * distributions for in-scope and out-of-scope questions overlap — which makes the similarity
 * threshold unable to separate them. Weighting rare terms up fixed that; see PROJECT_DEEP_DIVE.md
 * for the before and after numbers.
 * <p>
 * <b>Be honest about this in an interview.</b> It captures wording, not meaning: "event-driven
 * messaging" will not match "Kafka" the way a trained embedding model would. It exists so the
 * retrieval, guardrail and evaluation machinery is testable offline and in CI; switch
 * {@code rag.embedding-provider} to {@code ollama} or {@code openai} for real semantic search. The
 * fact that nothing else in the pipeline changes when you do is the design point.
 */
public class LocalHashingEmbeddingClient implements EmbeddingClient {

    private static final int DIMENSIONS = 1024;
    private static final double BIGRAM_WEIGHT = 0.6;

    /** Term to inverse document frequency, built at index time. Empty means "not fitted yet". */
    private volatile Map<String, Double> idf = Map.of();
    private volatile double unseenTermIdf = 1.0;

    @Override
    public void fit(List<String> corpus) {
        Map<String, Integer> documentFrequency = new HashMap<>();
        for (String document : corpus) {
            for (String term : new HashSet<>(weightedTerms(document).keySet())) {
                documentFrequency.merge(term, 1, Integer::sum);
            }
        }
        int total = corpus.size();
        Map<String, Double> fitted = new HashMap<>(documentFrequency.size());
        documentFrequency.forEach((term, frequency) ->
                // Smoothed IDF: a term in every chunk carries almost no information, a term in one
                // chunk carries a lot.
                fitted.put(term, Math.log((total + 1.0) / (frequency + 1.0)) + 1.0));

        this.idf = Map.copyOf(fitted);
        // A query word absent from the corpus cannot match anything, but it still enlarges the query
        // vector, which correctly pushes the similarity of an off-topic question down.
        this.unseenTermIdf = Math.log(total + 1.0) + 1.0;
    }

    @Override
    public float[] embed(String text) {
        float[] vector = new float[DIMENSIONS];
        Map<String, Double> localIdf = idf;

        weightedTerms(text).forEach((term, termFrequency) -> {
            double weight = 1.0 + Math.log(termFrequency);
            double inverseDocumentFrequency = localIdf.isEmpty()
                    ? 1.0
                    : localIdf.getOrDefault(term, unseenTermIdf);
            int index = Math.floorMod(term.hashCode(), DIMENSIONS);
            vector[index] += (float) (weight * inverseDocumentFrequency);
        });

        Vectors.normalise(vector);
        return vector;
    }

    @Override
    public String describe() {
        return "local-tfidf-hashing(dim=" + DIMENSIONS + ", vocab=" + idf.size()
                + ", lexical only, no network)";
    }

    /** Raw counts for unigrams plus down-weighted adjacent bigrams, which capture short phrases. */
    private Map<String, Double> weightedTerms(String text) {
        List<String> tokens = Text.tokens(text);
        Map<String, Double> counts = new HashMap<>();
        for (String token : tokens) {
            counts.merge(token, 1.0, Double::sum);
        }
        for (int i = 0; i < tokens.size() - 1; i++) {
            counts.merge(tokens.get(i) + '_' + tokens.get(i + 1), BIGRAM_WEIGHT, Double::sum);
        }
        return counts;
    }
}
