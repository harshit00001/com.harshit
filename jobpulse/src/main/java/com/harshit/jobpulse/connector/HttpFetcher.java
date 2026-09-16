package com.harshit.jobpulse.connector;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.harshit.jobpulse.config.JobPulseProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.net.ProxySelector;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.time.Duration;
import java.util.Optional;

/**
 * Single HTTP entry point for every connector: shared timeouts, retry, throttling
 * and a descriptive user agent so upstream sites can identify the caller.
 */
@Component
public class HttpFetcher {

    private static final Logger log = LoggerFactory.getLogger(HttpFetcher.class);

    private final JobPulseProperties properties;
    private final ObjectMapper objectMapper;
    private final HttpClient httpClient;

    public HttpFetcher(JobPulseProperties properties, ObjectMapper objectMapper) {
        this.properties = properties;
        this.objectMapper = objectMapper;

        HttpClient.Builder builder = HttpClient.newBuilder()
                .connectTimeout(Duration.ofSeconds(properties.getCrawl().getConnectTimeoutSeconds()))
                .followRedirects(HttpClient.Redirect.NORMAL)
                .proxy(ProxySelector.getDefault());

        osTrustStoreContext(properties.getCrawl().getTrustStoreType()).ifPresent(builder::sslContext);
        this.httpClient = builder.build();
    }

    /**
     * Builds an {@link SSLContext} backed by the OS certificate store.
     *
     * <p>Needed on networks that inspect TLS: the interception CA is trusted by the OS but
     * absent from the JDK's {@code cacerts}, so every HTTPS call fails PKIX validation.
     * Returns empty when the store type does not exist, leaving JDK defaults in place.
     */
    private Optional<SSLContext> osTrustStoreContext(String trustStoreType) {
        if (trustStoreType == null || trustStoreType.isBlank()) {
            return Optional.empty();
        }
        try {
            KeyStore keyStore = KeyStore.getInstance(trustStoreType);
            keyStore.load(null, null);

            TrustManagerFactory trustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            trustManagerFactory.init(keyStore);

            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, trustManagerFactory.getTrustManagers(), null);

            log.info("HTTPS trust store: {} ({} certificates)", trustStoreType, keyStore.size());
            return Optional.of(sslContext);
        } catch (GeneralSecurityException | IOException exception) {
            log.info("Trust store '{}' unavailable ({}), using JDK defaults",
                    trustStoreType, exception.getMessage());
            return Optional.empty();
        }
    }

    public JsonNode getJson(String url) {
        return readJson(buildGet(url), url);
    }

    public JsonNode postJson(String url, Object body) {
        String payload;
        try {
            payload = objectMapper.writeValueAsString(body);
        } catch (IOException exception) {
            throw new IllegalArgumentException("Unable to serialise request body", exception);
        }
        HttpRequest request = baseRequest(url)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .build();
        return readJson(request, url);
    }

    public String getHtml(String url) {
        HttpRequest request = baseRequest(url)
                .header("Accept", "text/html,application/xhtml+xml")
                .GET()
                .build();
        return send(request, url).body();
    }

    private HttpRequest buildGet(String url) {
        return baseRequest(url).GET().build();
    }

    private HttpRequest.Builder baseRequest(String url) {
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(properties.getCrawl().getReadTimeoutSeconds()))
                .header("User-Agent", properties.getCrawl().getUserAgent())
                .header("Accept", "application/json");
    }

    private JsonNode readJson(HttpRequest request, String url) {
        String body = send(request, url).body();
        try {
            return objectMapper.readTree(body);
        } catch (IOException exception) {
            throw new ConnectorException("Response from " + url + " was not valid JSON", exception);
        }
    }

    private HttpResponse<String> send(HttpRequest request, String url) {
        int attempts = properties.getCrawl().getMaxRetries() + 1;
        ConnectorException lastFailure = null;

        for (int attempt = 1; attempt <= attempts; attempt++) {
            try {
                throttle();
                HttpResponse<String> response =
                        httpClient.send(request, HttpResponse.BodyHandlers.ofString());
                if (response.statusCode() >= 200 && response.statusCode() < 300) {
                    return response;
                }
                lastFailure = new ConnectorException(
                        "HTTP " + response.statusCode() + " from " + url);
            } catch (IOException exception) {
                lastFailure = new ConnectorException(
                        "Request to " + url + " failed: " + describe(exception), exception);
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                throw new ConnectorException("Interrupted while calling " + url, exception);
            }
            log.warn("Attempt {}/{} failed for {}: {}", attempt, attempts, url,
                    lastFailure.getMessage());
        }
        throw lastFailure;
    }

    /** Flattens the cause chain, since the top-level IOException is often uninformative. */
    private String describe(Throwable throwable) {
        StringBuilder message = new StringBuilder();
        Throwable current = throwable;
        while (current != null) {
            if (!message.isEmpty()) {
                message.append(" <- ");
            }
            message.append(current.getClass().getSimpleName())
                    .append(": ")
                    .append(current.getMessage());
            current = current.getCause();
        }
        return message.toString();
    }

    private void throttle() throws InterruptedException {
        long delay = properties.getCrawl().getRequestDelayMillis();
        if (delay > 0) {
            Thread.sleep(delay);
        }
    }
}
