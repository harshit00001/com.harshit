package com.harshit.jwtoauth;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Frequently asked OAuth 2.0, OIDC, and JWT interview questions with short answers
 * (aligned with common interview talking points; verify details against RFCs for production).
 * <p>
 * Run main to print all Q&amp;A, or use {@link #items()}.
 */
public final class OAuth2JwtInterviewQa {

    public static final class Qa {
        private final String question;
        private final String answer;

        public Qa(String question, String answer) {
            this.question = question;
            this.answer = answer;
        }

        public String question() {
            return question;
        }

        public String answer() {
            return answer;
        }
    }

    private static final List<Qa> ITEMS = buildItems();

    private OAuth2JwtInterviewQa() { }

    private static List<Qa> buildItems() {
        List<Qa> list = new ArrayList<>();
        list.add(new Qa(
                "What is the difference between authentication and authorization?",
                "Authentication answers who you are. Authorization answers what you are allowed to access or do. OAuth 2.0 is mainly about authorization (delegated access to resources), though providers often pair it with sign-in (OpenID Connect)."));
        list.add(new Qa(
                "What is OAuth 2.0?",
                "An authorization framework: a client obtains limited access to a user's resources on a resource server, usually by obtaining an access token from an authorization server, without sharing the user's password with the client in the common browser-based flows."));
        list.add(new Qa(
                "Is OAuth 2.0 the same as authentication?",
                "Not by itself. OAuth 2.0 does not define user login; it defines how to obtain and use access tokens. OpenID Connect (OIDC) is often used on top of OAuth 2.0 to obtain identity (ID token) and authenticate users."));
        list.add(new Qa(
                "Name the four main OAuth 2.0 roles.",
                "Resource owner (user), client (app requesting access), authorization server (issues tokens), resource server (API holding data)."));
        list.add(new Qa(
                "What is a scope?",
                "A scope is a string (or set of strings) that limits what the access token is allowed to do (e.g. read profile, read email). The token and scope together mean permission to perform specific actions on resources."));
        list.add(new Qa(
                "What is an access token?",
                "A credential the client presents to the resource server to access protected resources. OAuth 2.0 does not mandate a format; opaque tokens and JWTs are both common."));
        list.add(new Qa(
                "What is a grant (grant type / flow)?",
                "A standardized way the client gets an access token (e.g. authorization code, client credentials). Different apps use different grants depending on whether a user is present and how secrets are stored."));
        list.add(new Qa(
                "When do you use the Client Credentials grant?",
                "Machine-to-machine or trusted backend services: the client authenticates with its own client_id and client_secret and gets a token without a user in the loop."));
        list.add(new Qa(
                "When do you use Authorization Code (with or without PKCE)?",
                "User-facing apps: browser SPAs, mobile, native. PKCE is recommended for public clients that cannot keep a client_secret safe; it mitigates authorization code interception."));
        list.add(new Qa(
                "What is the Implicit grant? Is it still recommended for SPAs?",
                "The implicit flow returned tokens in the front channel. It is largely deprecated for SPAs; prefer Authorization Code + PKCE (modern best practice)."));
        list.add(new Qa(
                "What is the Resource Owner Password grant?",
                "The app collects username/password and exchanges them for tokens. Avoid for third-party or untrusted clients; use only in narrow legacy or highly trusted cases."));
        list.add(new Qa(
                "What is the Device Code flow?",
                "For devices with limited input (smart TV, CLI): user completes login on another device; client polls until tokens are ready."));
        list.add(new Qa(
                "Why not send permissions as plain JSON instead of a token?",
                "A plain JSON object cannot be integrity-protected on its own. A client or attacker could alter fields (e.g. user id). Signed tokens (or opaque tokens verified server-side) let the resource server trust the data."));
        list.add(new Qa(
                "What is a JWT?",
                "JSON Web Token: a compact, URL-safe string (usually header.payload.signature) for carrying claims. Often used as a self-contained access or ID token, but JWT is a format, not the same as OAuth itself."));
        list.add(new Qa(
                "What are the three parts of a JWT?",
                "Header (alg, typ), Payload (claims), Signature (HMAC or asymmetric signature over header+payload). Parts are Base64url-encoded and separated by dots."));
        list.add(new Qa(
                "What are claims?",
                "Name/value pairs in the payload: statements about a subject. Reserved (iss, sub, exp, ...), public (IANA-registered to avoid collision), and private (app-specific)."));
        list.add(new Qa(
                "What must you validate on the resource server for a JWT access token (typical checks)?",
                "Signature (using issuer's key or your secret), typ/alg, issuer (iss) and audience (aud) as appropriate, and expiration (exp) / not-before (nbf) if present."));
        list.add(new Qa(
                "What is the difference between OAuth 2.0 and OpenID Connect?",
                "OIDC is an identity layer on top of OAuth 2.0: it defines ID token (identity claims), standard scopes like openid, profile, email, and userinfo, plus discovery and session concepts."));
        list.add(new Qa(
                "What is an Identity Provider (IdP) / authorization server in products like Okta or Auth0?",
                "A hosted service that implements OAuth 2.0 and often OIDC: user store, /authorize, /token, JWKS for keys, and client configuration (client_id, redirect URIs, etc.)."));
        return Collections.unmodifiableList(list);
    }

    public static List<Qa> items() {
        return ITEMS;
    }

    /** Prints all Q&amp;A to stdout (e.g. for quick review). */
    public static void printAll() {
        int n = 1;
        for (Qa qa : ITEMS) {
            System.out.println("--- " + n++ + " ---");
            System.out.println("Q: " + qa.question());
            System.out.println("A: " + qa.answer());
            System.out.println();
        }
    }

    public static void main(String[] args) {
        printAll();
    }
}
