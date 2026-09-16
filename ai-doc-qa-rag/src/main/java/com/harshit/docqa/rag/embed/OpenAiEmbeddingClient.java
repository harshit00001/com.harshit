package com.harshit.docqa.rag.embed;

import com.fasterxml.jackson.databind.JsonNode;
import com.harshit.docqa.config.RagProperties;
import com.harshit.docqa.rag.provider.JsonHttpClient;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Hosted embeddings. Note {@link #embedAll(List)} sends a batch in one request: the API accepts an
 * array of inputs, and batching is where most of the cost and latency of indexing disappears.
 */
public class OpenAiEmbeddingClient implements EmbeddingClient {

    private static final int BATCH_SIZE = 64;

    private final JsonHttpClient http;
    private final RagProperties.OpenAi config;

    public OpenAiEmbeddingClient(JsonHttpClient http, RagProperties.OpenAi config) {
        this.http = http;
        this.config = config;
        if (config.getApiKey() == null || config.getApiKey().isBlank()) {
            throw new IllegalStateException(
                    "rag.openai.api-key is empty - set OPENAI_API_KEY or use rag.embedding-provider=local");
        }
    }

    @Override
    public float[] embed(String text) {
        return embedAll(List.of(text)).get(0);
    }

    @Override
    public List<float[]> embedAll(List<String> texts) {
        List<float[]> vectors = new ArrayList<>(texts.size());
        for (int from = 0; from < texts.size(); from += BATCH_SIZE) {
            List<String> batch = texts.subList(from, Math.min(texts.size(), from + BATCH_SIZE));
            JsonNode response = http.postJson(
                    config.getBaseUrl() + "/v1/embeddings",
                    Map.of("model", config.getEmbeddingModel(), "input", batch),
                    Map.of("Authorization", "Bearer " + config.getApiKey()));
            for (JsonNode item : response.path("data")) {
                JsonNode embedding = item.path("embedding");
                float[] vector = new float[embedding.size()];
                for (int i = 0; i < embedding.size(); i++) {
                    vector[i] = (float) embedding.get(i).asDouble();
                }
                Vectors.normalise(vector);
                vectors.add(vector);
            }
        }
        if (vectors.size() != texts.size()) {
            throw new IllegalStateException(
                    "expected " + texts.size() + " embeddings but received " + vectors.size());
        }
        return vectors;
    }

    @Override
    public String describe() {
        return "openai(" + config.getEmbeddingModel() + ")";
    }
}
