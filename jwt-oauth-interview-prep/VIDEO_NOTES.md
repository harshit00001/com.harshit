# Video study guide (timestamped) — OAuth2, JWT, Okta, OIDC

**Source style:** *“OAuth2 JWT Interview Questions and Answers | Grant types, Scope, Access Token, Claims”* (Code Decode–style content; timestamps are approximate to match the talk track.)

Use this as **revision notes**; for production, always confirm behavior with **official RFCs** and your **identity provider** documentation.

---

## [00:00 – 00:30] Why this topic; authentication vs authorization

**What the video sets up:** Security breaks into two big ideas. **Authentication** answers *who you are*—whether you are a legitimate user (like a watchman at a building checking your identity). **Authorization** answers *where you are allowed to go* and *what you are allowed to access*—the watchman also checks whether you may enter a particular flat or block, sometimes by calling that owner.

**In one line for interviews:** Authentication = identity. Authorization = permissions on resources. OAuth 2.0 is primarily a **framework for authorization** (delegated access to resources), not a full “login standard” on its own.

**Takeaway:** Do not say “OAuth 2.0 = login” without nuance; you often pair it with **OpenID Connect (OIDC)** when you need identity (who the user is) in a standard way.

---

## [00:30 – 01:05] OAuth 2.0 is not “authentication by itself”

**Main point:** OAuth 2.0 tells you *what* can be accessed (and under which delegated consent). It is **not** the same as “the protocol that authenticates the user in every sense”—though real products bundle login + tokens together.

**Mental model for interviews:** *Authorization* = *what* you can do with someone else’s data after they consent.

---

## [01:05 – 02:20] “Sign in with Google” (Twitter / third-party app example)

**Story:** When you use an app (e.g. Twitter) and choose **Sign in with Google**, Twitter is saying: *Google has already **authenticated** you; I (Twitter) do not want to redo full identity proof myself for that step.* Twitter mainly wants **permission to read specific data** (e.g. name, email, profile picture) that lives with Google.

- **Google** (or Apple, etc.) is the place where the **account and data** live.
- **Twitter** is the **client** asking for *limited* access, not the user’s Google password (in the normal OAuth flow).
- Before data is shared, the user is asked: *“Can this app get these items?”* The **kinds of data** (name, email, profile picture) are examples of what we call **scopes**.

**Terminology to remember:** **Delegate authentication** to the service that *hosts* the account; your app then requests **authorized access** to specific pieces of data via **scopes**.

---

## [02:20 – 04:20] OAuth 1 vs OAuth 2.0; how the story fits the market

**OAuth 1.0 and OAuth 2.0** are *not* small upgrades of each other. OAuth 1.0 is **legacy/deprecated** in practice; the ecosystem standard for new work is **OAuth 2.0** (and often **OIDC** on top for identity). Do not treat them as backward compatible.

**Where you see OAuth 2.0 in real life:** Any “**Login with X**” or “**connect your account to import contacts**” flow where one product asks another to vouch for the user and release only agreed data.

---

## [04:20 – 05:15] High-level flow: grant → token → call the resource API

**Narrated flow (useful for whiteboard interviews):**

1. User in the **browser** wants to use app **A** (e.g. Twitter).
2. App **A** sends the user to the **Authorization Server** (e.g. **Okta**, **Auth0**, or Google as IdP) to get an **authorization grant** (user consent + grant artifact).
3. The client exchanges that grant at the **token endpoint** for an **access token** (and sometimes a **refresh token**—not always stressed in a short video).
4. The client then calls the **Resource Server** (e.g. Google’s userinfo / Gmail API) with: *“Here is an access token proving the user let me read scopes X, Y, Z.”*
5. The resource server returns data if the token is **valid** and the **scope** is sufficient.

**Roles in one breath:** who owns the data (**resource owner**), who wants it (**client**), who issues tokens (**authorization server**), who serves the protected API (**resource server**). In dev terms, the **Resource Server** is often *your Spring Boot API on `localhost:8080`*; the **client** is *your Angular app on `localhost:4200`*; the **authorization server** is *Okta/Auth0/Keycloak* you configured.

---

## [05:15 – 07:00] Okta: product vs protocol (common interview confusion)

**Clarify in interviews:**

- **OAuth 2.0** = *standard / protocol* for how tokens and consent work.
- **Okta** (or **Auth0**) = a **company and product** that *implements* that standard (and usually **OpenID Connect**), hosts users, apps, and endpoints, and gives you things like `client_id`, `client_secret`, and integration guides.

**Video-style line:** *Okta is not “the name of the OAuth protocol.”* It is an **Identity Provider (IdP)** / **Authorization Server** that *uses* OAuth 2.0 (and often OIDC) so you do not have to build everything in-house.

**OpenID Connect (OIDC) one-liner:** A layer on top of OAuth 2.0 for **identity**: you get an **ID token** (profile-style claims) and standard scopes like `openid`, `profile`, `email`, plus things like `userinfo` and discovery. If the video rushes this part, still remember: **OAuth 2.0 = authorization to APIs**; **OIDC = who the user is** in a standard way.

---

## [07:00 – 08:00] The two famous endpoints: `/authorize` and `/token`

**Typical pattern (as in the slide):**

- **`/oauth2/authorize`** (or similar): user is redirected here, logs in, **consents**; you get an **authorization grant** (e.g. **authorization code** in the “code” flow).
- **`/oauth2/token`**: the client **exchanges** the grant (plus `client_id` / `client_secret` where applicable) for **access token** (and often **refresh token** in code flow).

**Diagram order:** *Authorize* → *Grant* → *Token* → *Access token* → call APIs with that token (often in `Authorization: Bearer ...`).

---

## [08:00 – 09:20] The four roles (relate to your own stack)

| Role | Who it is in the video’s dev story |
|------|------------------------------------|
| **Resource owner** | The **end user** (you) who can consent. |
| **Client** | The **front-end** or mobile app that *needs* data (e.g. Angular on port 4200). |
| **Resource server** | The **back-end** that actually reads the DB and exposes REST APIs; must **validate** the token. |
| **Authorization server** | Okta/Auth0/etc. that **issues** tokens after login/consent. |

**Interview sentence:** *My Angular app cannot read the database directly; it first obtains an access token from the authorization server, then sends that token to my Spring resource server, which checks it and then returns data.*

---

## [09:20 – 12:00] Scopes and access tokens (movie ticket analogy)

**Scopes + tokens = fine-grained “what you may do.”** Together they mean: *this identity has **permission** to do **something specific** on a resource.*

**Analogy (from the video):**  
- The **scope** = *which movie* and *show time* (what you are allowed to use).  
- The **access token** = the **ticket**—without it, the theater (resource server) does not let you in. The ticket can also be checked for **expiry** (like a show time).

**Coarse “types” of access often taught in intro slides (read / write / read–write / none):** treat these as **teaching examples** of *granularity*; real systems define **named scopes** (strings) like `read:email` or `api.orders.write`.

**Important nuance (professional reality):** The **access token** is what you present to the API. It often **embeds or references** which scopes were granted, depending on whether the token is self-contained (JWT) or **opaque** (server-side lookup).

---

## [12:00 – 16:00] Grant types = different “ways to get a ticket”

**Idea:** A **grant** (or **flow**) is a **recipe** to obtain an **access token** *without* handing your password to the wrong place—each fits a different kind of app.

**Movie analogy:** You can get a seat by *walking to the box office* or *booking online*—two **flows**; same end goal, different steps.

**Grants the video calls out (typical study map):**

1. **Client credentials** — **No interactive user** in the loop. Good for **backend services**, **batch jobs**, **microservice-to-microservice** where the **client** can safely store `client_id` and `client_secret` (e.g. in a server `application.properties` or a vault). The **application authenticates as itself** to the token endpoint.

2. **Authorization code** — **User in a browser**; common for **SPAs** (e.g. Angular). The public SPA **cannot** keep a `client_secret` private (anyone can read the JS bundle), so the flow is designed so the **code** is exchanged for tokens, often with only `client_id` in the public part—follow your IdP’s **SPA** / **public client** pattern.

3. **Authorization code + PKCE** — **Same family** as auth code, but **extra proof** (code verifier/challenge) so that even if a **code** is stolen in transit, it is harder to exchange it without the original app. **Strongly** recommended for **SPAs** and **native/mobile** apps (public clients).

4. **Implicit (legacy in teaching)** — Access token in the **front channel**; **avoid** for new SPAs; **authorization code + PKCE** is the modern replacement.

5. **Resource owner password** — The app **asks for username and password** and sends them to the token endpoint. **Only** if the client is **completely trusted** and special cases; **not** the default for third-party or random apps (security and phishing risk).

6. **Device code** — For **TVs, CLI tools**, or bad keyboards: user opens a **phone** or **browser** to approve while the **device** polls the token endpoint until the user completes login.

**Exam tip:** If asked “**Which grant for a React SPA?**” → **Authorization Code with PKCE** (not implicit).

---

## [16:00 – 19:00] Why not return permissions as “plain JSON” sent to the client?

**Problem:** If the “permission object” is **plain JSON** with no **integrity** guarantee, a **man in the middle** or a **malicious client** could **edit** fields (e.g. change `userId` or `sub`) and your API might be fooled if it only trusted the **body** without **cryptographic verification**.

**Point of a token (JWT or opaque):** The **authorization server** (or the API working with that server) issues something the **resource server** can **verify**—signature (for JWT) or **introspection** / **server-side** validation (for opaque). That is *why* we don’t just email JSON blobs to say “this user is admin.”

**OAuth 2.0 and format:** The spec does **not** say “access token must be JWT.” Opaque **random** strings with **server lookup** are valid. In practice, many systems use **JWT** for the access token (or a separate **ID token** in OIDC) because you can **embed claims** and **sign** them.

---

## [19:00 – 22:00] What is a JWT, really?

**Vocabulary for interviews:**  
- **OAuth 2.0** = *framework for obtaining tokens* (and flows, clients, etc.).  
- **JWT (JSON Web Token)** = a **string format** for a **signed, compact** object—**not** the same word as “OAuth.”  
- You often *carry* the **access token** or **ID token** as a JWT, but the **OAuth protocol** and the **JWT format** are different layers.

**Why JWTs are popular for tokens:** The payload can hold **claims**; the **signature** (HMAC or RSA/EC keys) means **tampering** breaks **verification**; you can have **`exp`**, `iat`, and other time checks.

**Structure (three base64url pieces, two dots):** `header.payload.signature`  
- **Header** — e.g. `alg` (algorithm), `typ: JWT`.  
- **Payload** — **Claims** (JSON statements about a subject, expiry, custom fields).  
- **Signature** — e.g. `HMACSHA256( base64url(header) + "." + base64url(payload), secret )` (conceptually; real systems may use **asymmetric** keys and **JWKS** for verification).

**Tools like jwt.io (video demo):** Decode visually; you still must **not** put production secrets in random websites—use local tools in real work.

---

## [22:00 – 25:00] Claims: reserved, public, private; changing payload changes signature

**Definition:** A **claim** is a **key–value** pair in the payload JSON (e.g. `"name": "John Doe"`). Claims are **statements** about a user, session, or context.

**Three buckets (simplified from the slides):**

- **Reserved** — Commonly `iss` (issuer), `sub` (subject), `aud` (audience), `exp` (expiry), `iat` (issued at), etc. Short names keep the token **compact** for wire size.
- **Public** — Names you can look up in registries to avoid name collisions (like picking a `email_verified` style claim if standardized).
- **Private** — **App-specific** claims your org agrees on (e.g. `shopId`, `tenantId`, `role` for your API).

**Video demo idea:** If you **edit the payload** in a JWT, the **signature** no longer matches unless you **re-sign** with the real secret. That is a **concrete** way to see **integrity** in action.

*Interview note:* For **bearer** JWTs in real systems, your security policy and **OIDC/OAuth profiles** may require certain claims; always quote **what your product requires**, not a generic one-liner from a slide.

---

## [25:00 – 28:00] How your back-end “validates” a JWT (step-by-step story)

A typical **resource server** (e.g. Spring Security filter) does approximately:

1. **Read the header** (first segment), **Base64url-decode** it, ensure **`typ` is `JWT`**, and the **`alg` is** one your server **accepts** (your policy might reject `none` or unexpected algorithms to avoid **algorithm confusion** attacks).
2. **Obtain the key** to **verify the signature** — shared **secret** (HS\*) for symmetric, or **public key** from **issuer’s JWKS** (RS/ES) for production IdPs. The secret in tutorials is the `client_secret` / signing key *only* in toy examples; real IdPs have **key rotation** and **iss** URLs.
3. **Recompute** the signature and **compare** with the third segment. Mismatch → **reject**.
4. **Decode the payload** and check **`exp` (and `nbf` if present)**. Expired → **reject**.
5. (Often) check **`iss` matches your expected issuer** and **`aud` matches your API’s audience**, if you use them.

**If everything passes** → you trust the **claims** that were **signed** (and still enforce **your own** business authorization on top).

---

## [28:00 – end] If you go deeper: OIDC + Okta in Spring (beyond this video)

**What the outro often says:** *OIDC* and *hands-on Okta + Spring Boot* are **longer** topics: registration of the app, redirect URIs, handling the **code** exchange, **ID token** vs **access token**, **refresh** rotation, and **state** / **nonce** for CSRF and replay. Treat this video as a **map**; use **Spring Security OAuth2** / **Okta** guides for code.

---

## One-page “cheat sheet” (non-timestamped)

- **AuthN** = who you are; **AuthZ** = what you can do. **OAuth2** = authorization; **OIDC** = identity on top.  
- **Roles:** resource owner, client, authorization server, resource server.  
- **Scope** = *what* is allowed; **access token** = *credential* the API checks.  
- **Grants:** client creds (B2B), **auth code + PKCE** (SPAs/mobile), device code (TV/CLI), avoid implicit; password only if fully trusted.  
- **Why token:** **integrity** and **verifiability**; not raw editable JSON.  
- **JWT** = **format**; three parts; **sign** and check **`exp`**, **`iss`**, **`aud`**, `alg` policy.  
- **Okta/Auth0** = **IdP product**, not a synonym for “OAuth2 text.”

---

## Run the companion Java project

```text
cd jwt-oauth-interview-prep
mvn -q compile exec:java
```

Print **interview Q&A** from the same project:

```text
mvn -q compile exec:java -Dexec.mainClass=com.harshit.jwtoauth.OAuth2JwtInterviewQa
```
