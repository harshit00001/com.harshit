package com.harshit.docqa.rag.provider;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;

/**
 * One small JSON-over-HTTP helper shared by the model providers. Timeouts are set deliberately: a
 * model call is a remote dependency, and an unbounded one turns a slow model into a hung request.
 */
@Component
public class JsonHttpClient {

    private final HttpClient http = HttpClient.newBuilder()
            .connectTimeout(Duration.ofSeconds(5))
            .build();
    private final ObjectMapper mapper = new ObjectMapper();

    public JsonNode postJson(String url, Object body, Map<String, String> headers) {
        try {
            HttpRequest.Builder request = HttpRequest.newBuilder(URI.create(url))
                    .timeout(Duration.ofSeconds(60))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(mapper.writeValueAsString(body)));
            headers.forEach(request::header);

            HttpResponse<String> response = http.send(request.build(), HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() / 100 != 2) {
                throw new ProviderException("HTTP " + response.statusCode() + " from " + url + ": "
                        + truncate(response.body()));
            }
            return mapper.readTree(response.body());
        } catch (IOException e) {
            throw new ProviderException("call to " + url + " failed: " + e.getMessage(), e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new ProviderException("call to " + url + " was interrupted", e);
        }
    }

    private String truncate(String body) {
        return body != null && body.length() > 400 ? body.substring(0, 400) + "..." : body;
    }

    public static class ProviderException extends RuntimeException {
        public ProviderException(String message) {
            super(message);
        }

        public ProviderException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
