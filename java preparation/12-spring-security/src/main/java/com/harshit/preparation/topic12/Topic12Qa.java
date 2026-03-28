package com.harshit.preparation.topic12;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;

/**
 * Topic 12 — Spring Security, JWT.
 */
public final class Topic12Qa {

    private Topic12Qa() {
    }

    /*
     * Q: Authentication vs Authorization?
     *
     * SCRIPT:
     * Authentication answers “who are you?”—login, JWT validation, proving identity. Authorization
     * answers “what may you do?”—roles, scopes, method-level rules. In Spring Security the filter
     * chain establishes authentication first; then authorization checks like hasRole or @PreAuthorize
     * run. I always separate the two in interviews because teams confuse them.
     *
     * REAL LIFE:
     * Showing your ID at airport security is authentication; your boarding gate permission is authorization.
     */

    /*
     * Q: How do you implement JWT authentication in Spring Boot?
     *
     * SCRIPT:
     * I describe a login endpoint that validates credentials and issues a signed JWT with expiry
     * and claims. Clients send Authorization: Bearer. A filter validates the signature and builds the
     * SecurityContext with authorities. For resource servers I often use spring-boot-starter-oauth2-resource-server
     * or a custom JwtAuthenticationFilter. Refresh tokens and key rotation are follow-up topics I mention briefly.
     */

    /*
     * Q: How do you secure REST APIs?
     *
     * SCRIPT:
     * HTTPS everywhere, stateless JWT or session strategy chosen deliberately, CSRF disabled only
     * when the API is purely token-based, CORS restricted to known frontends, rate limiting at the
     * gateway, and method security for fine-grained rules. Security is layers—not one annotation.
     */

    /**
     * Authentication (password matches hash) vs authorization (roles in JWT payload) — runnable without a web server.
     */
    public static void demo() {
        BCryptPasswordEncoder bcrypt = new BCryptPasswordEncoder();
        String hash = bcrypt.encode("InterviewSecret!");
        System.out.println("auth — bcrypt matches: " + bcrypt.matches("InterviewSecret!", hash));
        System.out.println("auth — wrong password: " + bcrypt.matches("wrong", hash));

        String sampleJwt = buildUnsignedJwtSample();
        System.out.println("sample JWT: " + sampleJwt);
        decodeJwtPayload(sampleJwt).ifPresent(json -> System.out.println("authz — payload JSON: " + json));
    }

    /** Minimal JWT-shaped string (header.payload.sig) for decoding practice — not a production security check. */
    private static String buildUnsignedJwtSample() {
        String header = base64Url("{\"alg\":\"HS256\",\"typ\":\"JWT\"}");
        String payload = base64Url("{\"sub\":\"user1\",\"roles\":[\"USER\",\"ADMIN\"]}");
        return header + "." + payload + ".signature-not-verified-in-demo";
    }

    private static String base64Url(String utf8) {
        return Base64.getUrlEncoder().withoutPadding().encodeToString(utf8.getBytes(StandardCharsets.UTF_8));
    }

    static Optional<String> decodeJwtPayload(String jwt) {
        String[] parts = jwt.split("\\.");
        if (parts.length < 2) {
            return Optional.empty();
        }
        byte[] decoded = Base64.getUrlDecoder().decode(parts[1]);
        return Optional.of(new String(decoded, StandardCharsets.UTF_8));
    }

    public static void main(String[] args) {
        demo();
    }
}
