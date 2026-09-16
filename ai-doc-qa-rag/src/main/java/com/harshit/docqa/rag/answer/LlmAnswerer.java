package com.harshit.docqa.rag.answer;

import com.fasterxml.jackson.databind.JsonNode;
import com.harshit.docqa.config.RagProperties;
import com.harshit.docqa.rag.provider.JsonHttpClient;
import com.harshit.docqa.rag.store.ScoredChunk;

import java.util.List;
import java.util.Map;

/**
 * Generated answers from a chat model, grounded in the retrieved passages.
 * <p>
 * {@code temperature} is pinned low: this is an extraction-and-summarise task, so creativity is not
 * a feature here, it is a source of invented detail.
 */
public class LlmAnswerer implements Answerer {

    private static final double TEMPERATURE = 0.1;

    private final JsonHttpClient http;
    private final PromptBuilder prompts;
    private final Provider provider;
    private final RagProperties properties;

    public enum Provider { OLLAMA, OPENAI }

    public LlmAnswerer(JsonHttpClient http, PromptBuilder prompts, Provider provider,
                       RagProperties properties) {
        this.http = http;
        this.prompts = prompts;
        this.provider = provider;
        this.properties = properties;
    }

    @Override
    public String answer(String question, List<ScoredChunk> context) {
        String system = prompts.systemPrompt();
        String user = prompts.userPrompt(question, context);
        return provider == Provider.OLLAMA ? callOllama(system, user) : callOpenAi(system, user);
    }

    private String callOllama(String system, String user) {
        RagProperties.Ollama config = properties.getOllama();
        JsonNode response = http.postJson(config.getBaseUrl() + "/api/chat",
                Map.of("model", config.getChatModel(),
                        "stream", false,
                        "options", Map.of("temperature", TEMPERATURE),
                        "messages", List.of(
                                Map.of("role", "system", "content", system),
                                Map.of("role", "user", "content", user))),
                Map.of());
        return response.path("message").path("content").asText().strip();
    }

    private String callOpenAi(String system, String user) {
        RagProperties.OpenAi config = properties.getOpenai();
        JsonNode response = http.postJson(config.getBaseUrl() + "/v1/chat/completions",
                Map.of("model", config.getChatModel(),
                        "temperature", TEMPERATURE,
                        "messages", List.of(
                                Map.of("role", "system", "content", system),
                                Map.of("role", "user", "content", user))),
                Map.of("Authorization", "Bearer " + config.getApiKey()));
        return response.path("choices").path(0).path("message").path("content").asText().strip();
    }

    @Override
    public String describe() {
        return provider == Provider.OLLAMA
                ? "ollama(" + properties.getOllama().getChatModel() + ")"
                : "openai(" + properties.getOpenai().getChatModel() + ")";
    }
}
