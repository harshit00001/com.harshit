package com.harshit.docqa.config;

import com.harshit.docqa.rag.answer.Answerer;
import com.harshit.docqa.rag.answer.ExtractiveAnswerer;
import com.harshit.docqa.rag.answer.LlmAnswerer;
import com.harshit.docqa.rag.answer.PromptBuilder;
import com.harshit.docqa.rag.embed.EmbeddingClient;
import com.harshit.docqa.rag.embed.LocalHashingEmbeddingClient;
import com.harshit.docqa.rag.embed.OllamaEmbeddingClient;
import com.harshit.docqa.rag.embed.OpenAiEmbeddingClient;
import com.harshit.docqa.rag.provider.JsonHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Locale;

/**
 * Chooses the embedding and answering implementations from configuration.
 * <p>
 * This is the class that makes the "swap the model without touching the pipeline" claim real, and it
 * is the one to open in an interview when asked how the design keeps the provider replaceable.
 */
@Configuration
public class ProviderConfiguration {

    private static final Logger log = LoggerFactory.getLogger(ProviderConfiguration.class);

    @Bean
    public EmbeddingClient embeddingClient(RagProperties properties, JsonHttpClient http) {
        String provider = properties.getEmbeddingProvider().toLowerCase(Locale.ROOT);
        EmbeddingClient client = switch (provider) {
            case "ollama" -> new OllamaEmbeddingClient(http, properties.getOllama());
            case "openai" -> new OpenAiEmbeddingClient(http, properties.getOpenai());
            case "local" -> new LocalHashingEmbeddingClient();
            default -> throw new IllegalArgumentException(
                    "unknown rag.embedding-provider '" + provider + "' (use local, ollama or openai)");
        };
        log.info("embedding provider: {}", client.describe());
        return client;
    }

    @Bean
    public Answerer answerer(RagProperties properties, JsonHttpClient http, PromptBuilder prompts) {
        String provider = properties.getAnswerProvider().toLowerCase(Locale.ROOT);
        Answerer answerer = switch (provider) {
            case "ollama" -> new LlmAnswerer(http, prompts, LlmAnswerer.Provider.OLLAMA, properties);
            case "openai" -> new LlmAnswerer(http, prompts, LlmAnswerer.Provider.OPENAI, properties);
            case "extractive" -> new ExtractiveAnswerer();
            default -> throw new IllegalArgumentException(
                    "unknown rag.answer-provider '" + provider + "' (use extractive, ollama or openai)");
        };
        log.info("answer provider: {}", answerer.describe());
        return answerer;
    }
}
