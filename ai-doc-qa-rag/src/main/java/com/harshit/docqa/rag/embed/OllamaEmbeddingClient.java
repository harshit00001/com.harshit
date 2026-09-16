package com.harshit.docqa.rag.embed;

import com.fasterxml.jackson.databind.JsonNode;
import com.harshit.docqa.config.RagProperties;
import com.harshit.docqa.rag.provider.JsonHttpClient;

import java.util.Map;

/**
 * Real embeddings from a locally running Ollama, so semantic search costs nothing and no data
 * leaves the machine — the option to reach for when a client will not send documents to a
 * third-party API.
 * <p>
 * Start it with: {@code ollama pull nomic-embed-text}
 */
public class OllamaEmbeddingClient implements EmbeddingClient {

    private final JsonHttpClient http;
    private final RagProperties.Ollama config;

    public OllamaEmbeddingClient(JsonHttpClient http, RagProperties.Ollama config) {
        this.http = http;
        this.config = config;
    }

    @Override
    public float[] embed(String text) {
        JsonNode response = http.postJson(
                config.getBaseUrl() + "/api/embeddings",
                Map.of("model", config.getEmbeddingModel(), "prompt", text),
                Map.of());
        JsonNode embedding = response.path("embedding");
        if (!embedding.isArray() || embedding.isEmpty()) {
            throw new IllegalStateException("Ollama returned no embedding: " + response);
        }
        float[] vector = new float[embedding.size()];
        for (int i = 0; i < embedding.size(); i++) {
            vector[i] = (float) embedding.get(i).asDouble();
        }
        Vectors.normalise(vector);
        return vector;
    }

    @Override
    public String describe() {
        return "ollama(" + config.getEmbeddingModel() + " @ " + config.getBaseUrl() + ")";
    }
}
