package com.harshit.order.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;

/**
 * The only class that talks to inventory-service. Keep RestTemplate here so OrderService
 * stays a business class, not an HTTP class.
 */
@Component
public class InventoryClient {

    private static final Logger log = LoggerFactory.getLogger(InventoryClient.class);

    private final RestTemplate restTemplate;
    private final String baseUrl;

    public InventoryClient(RestTemplate restTemplate,
                           @Value("${inventory.base-url}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
    }

    public StockItem getStock(String sku) {
        String url = baseUrl + "/api/inventory/{sku}";
        log.info("RestTemplate GET {}", url.replace("{sku}", sku));
        try {
            return restTemplate.getForObject(url, StockItem.class, sku);
        } catch (HttpStatusCodeException e) {
            throw translate(e, "GET stock " + sku);
        } catch (ResourceAccessException e) {
            throw unavailable("inventory-service did not respond on GET " + sku, e);
        }
    }

    public ReserveResponse reserve(String sku, int qty) {
        String url = baseUrl + "/api/inventory/{sku}/reserve";
        log.info("RestTemplate POST {} qty={}", url.replace("{sku}", sku), qty);
        try {
            ResponseEntity<ReserveResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    new HttpEntity<>(new ReserveRequest(qty)),
                    ReserveResponse.class,
                    sku);
            return response.getBody();
        } catch (HttpStatusCodeException e) {
            throw translate(e, "POST reserve " + sku);
        } catch (ResourceAccessException e) {
            throw unavailable("inventory-service did not respond on POST reserve " + sku, e);
        }
    }

    private ResponseStatusException translate(HttpStatusCodeException e, String action) {
        log.warn("{} failed: {} {}", action, e.getStatusCode(), e.getResponseBodyAsString());
        return new ResponseStatusException(e.getStatusCode(), e.getResponseBodyAsString(), e);
    }

    private ResponseStatusException unavailable(String message, Exception cause) {
        log.error(message, cause);
        return new ResponseStatusException(
                org.springframework.http.HttpStatus.SERVICE_UNAVAILABLE, message, cause);
    }
}
