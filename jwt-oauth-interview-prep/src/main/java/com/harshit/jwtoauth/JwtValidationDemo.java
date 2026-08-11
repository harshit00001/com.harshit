package com.harshit.jwtoauth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

/**
 * Runnable demo: build an HMAC-SHA256 JWT, parse it, and verify the signature
 * and expiration (what a resource server would do, simplified).
 * <p>
 * Run: {@code mvn -q compile exec:java} from this project directory.
 */
public final class JwtValidationDemo {

    /** Demo only — use a strong secret and config (env/vault) in real apps. */
    private static final String DEMO_SECRET = "my-demo-secret-must-be-256-bit-long!!";

    private JwtValidationDemo() { }

    public static void main(String[] args) {
        SecretKey key = Keys.hmacShaKeyFor(padTo256Bits(DEMO_SECRET));

        long nowMs = System.currentTimeMillis();
        long expMs = nowMs + 60_000; // 1 minute valid

        String token = Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setSubject("user-42")
                .setIssuer("demo-auth-server")
                .setAudience("demo-api")
                .setIssuedAt(new Date(nowMs))
                .setExpiration(new Date(expMs))
                .addClaims(Map.of("role", "READ_USER"))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        System.out.println("=== Signed JWT (demo) ===");
        System.out.println(token);
        System.out.println();

        // Parse and verify signature + validate exp/nbf
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        System.out.println("=== Parsed claims (after signature verification) ===");
        System.out.println("sub=" + claims.getSubject());
        System.out.println("iss=" + claims.getIssuer());
        System.out.println("aud=" + claims.getAudience());
        System.out.println("exp=" + claims.getExpiration());
        System.out.println("role=" + claims.get("role", String.class));
        System.out.println();
        System.out.println("Token verified: signature is valid and not expired (jjwt enforces exp).");

        // Show tamper rejection: change payload in token string → parse should fail
        int dot = token.lastIndexOf('.');
        String bad = token.substring(0, dot) + ".tampered-signature";
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(bad);
            System.out.println("Unexpected: tampered token accepted.");
        } catch (Exception e) {
            System.out.println("Tampered token rejected as expected: " + e.getClass().getSimpleName());
        }
    }

    private static byte[] padTo256Bits(String s) {
        byte[] b = s.getBytes(StandardCharsets.UTF_8);
        if (b.length >= 32) {
            return b;
        }
        byte[] out = new byte[32];
        System.arraycopy(b, 0, out, 0, b.length);
        return out;
    }
}
