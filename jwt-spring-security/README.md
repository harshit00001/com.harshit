# 🔐 JWT Spring Security - Complete Interview Guide

A comprehensive guide to JWT (JSON Web Token) implementation with Spring Security from **Basic to Advanced** with detailed explanations, code examples, and interview questions.

---

## 📚 Table of Contents

1. [Introduction to JWT](#introduction-to-jwt)
2. [What is JWT?](#what-is-jwt)
3. [JWT Structure](#jwt-structure)
4. [Why JWT?](#why-jwt)
5. [Project Structure](#project-structure)
6. [Basic Implementation](#basic-implementation)
7. [Intermediate Features](#intermediate-features)
8. [Advanced Features](#advanced-features)
9. [Complete Code Walkthrough](#complete-code-walkthrough)
10. [Interview Questions & Answers](#interview-questions--answers)
11. [Best Practices](#best-practices)
12. [Common Mistakes](#common-mistakes)
13. [Security Considerations](#security-considerations)

---

## Introduction to JWT

### What is JWT?

**JWT (JSON Web Token)** is a compact, URL-safe token format used for securely transmitting information between parties. It's an open standard (RFC 7519) that defines a way to securely represent claims between two parties.

**Simple Explanation**: 
Think of JWT as a "ticket" that proves you're authenticated. When you log in, the server gives you a JWT token. You include this token in every request, and the server verifies it to know who you are without needing to check a database every time.

**Key Characteristics**:
- **Stateless**: Server doesn't need to store session information
- **Self-contained**: Token contains all necessary user information
- **Compact**: Can be sent via URL, POST parameter, or HTTP header
- **Signed**: Cryptographically signed to prevent tampering

---

## JWT Structure

A JWT consists of **three parts** separated by dots (`.`):

```
Header.Payload.Signature
```

### 1. Header

The header typically consists of two parts:
- **Type**: Always "JWT"
- **Algorithm**: The signing algorithm (e.g., HS256, RS256)

**Example**:
```json
{
  "alg": "HS512",
  "typ": "JWT"
}
```

This is then **Base64Url encoded** to form the first part of the JWT.

### 2. Payload

The payload contains the **claims**. Claims are statements about an entity (typically the user) and additional metadata.

**Types of Claims**:
- **Registered Claims**: Standard claims like `iss` (issuer), `exp` (expiration), `sub` (subject)
- **Public Claims**: Can be defined by anyone
- **Private Claims**: Custom claims specific to your application

**Example**:
```json
{
  "sub": "john_doe",
  "iat": 1516239022,
  "exp": 1516242622,
  "roles": ["ROLE_USER", "ROLE_ADMIN"]
}
```

This is **Base64Url encoded** to form the second part of the JWT.

### 3. Signature

The signature is used to verify that the token hasn't been tampered with. It's created by:

```
HMACSHA512(
  base64UrlEncode(header) + "." + base64UrlEncode(payload),
  secret
)
```

**Complete JWT Example**:
```
eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb2huX2RvZSIsImlhdCI6MTUxNjIzOTAyMiwiZXhwIjoxNTE2MjQyNjIyLCJyb2xlcyI6WyJST0xFX1VTRVIiLCJST0xFX0FETUlOIl19.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c
```

---

## Why JWT?

### Advantages

1. **Stateless**: No server-side session storage needed
2. **Scalable**: Works well in distributed systems
3. **Cross-domain**: Can be used across different domains
4. **Mobile-friendly**: Works well with mobile apps
5. **Self-contained**: Contains user information, reducing database queries

### Disadvantages

1. **Cannot be revoked easily**: Tokens are valid until expiration (unless blacklisted)
2. **Size**: Larger than session IDs
3. **Security**: If stolen, valid until expiration
4. **No built-in refresh**: Need to implement refresh token mechanism

---

## Project Structure

```
jwt-spring-security/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/harshit/jwt/
│   │   │       ├── JwtSpringSecurityApplication.java
│   │   │       ├── config/
│   │   │       │   ├── DataInitializer.java
│   │   │       │   └── RedisConfig.java
│   │   │       ├── controller/
│   │   │       │   ├── AuthController.java
│   │   │       │   └── UserController.java
│   │   │       ├── dto/
│   │   │       │   ├── JwtResponse.java
│   │   │       │   ├── LoginRequest.java
│   │   │       │   └── RegisterRequest.java
│   │   │       ├── entity/
│   │   │       │   ├── Role.java
│   │   │       │   ├── RefreshToken.java
│   │   │       │   └── User.java
│   │   │       ├── repository/
│   │   │       │   ├── RefreshTokenRepository.java
│   │   │       │   ├── RoleRepository.java
│   │   │       │   └── UserRepository.java
│   │   │       ├── security/
│   │   │       │   ├── JwtAuthenticationFilter.java
│   │   │       │   └── SecurityConfig.java
│   │   │       ├── service/
│   │   │       │   ├── RefreshTokenService.java
│   │   │       │   ├── TokenBlacklistService.java
│   │   │       │   └── UserService.java
│   │   │       └── util/
│   │   │           └── JwtTokenUtil.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
└── pom.xml
```

---

## Basic Implementation

### Step 1: Dependencies (pom.xml)

Key dependencies:
- `spring-boot-starter-security`: Spring Security
- `spring-boot-starter-web`: Spring Web
- `jjwt`: JWT library
- `spring-boot-starter-data-jpa`: Database access
- `h2`: In-memory database (for demo)

### Step 2: JWT Utility Class

The `JwtTokenUtil` class handles:
- Token generation
- Token validation
- Token parsing
- Claim extraction

**Key Methods**:
- `generateToken()`: Creates JWT token
- `validateToken()`: Validates token signature and expiration
- `getUsernameFromToken()`: Extracts username from token

### Step 3: Security Configuration

The `SecurityConfig` class:
- Configures authentication
- Defines authorization rules
- Sets up JWT filter
- Configures password encoding

### Step 4: JWT Filter

The `JwtAuthenticationFilter`:
- Intercepts every request
- Extracts JWT from Authorization header
- Validates token
- Sets authentication in SecurityContext

---

## Intermediate Features

### 1. Refresh Tokens

**Why Refresh Tokens?**
- Access tokens have short expiration (15 min - 1 hour) for security
- Refresh tokens have long expiration (7-30 days) for convenience
- If access token is stolen, damage is limited

**How it Works**:
1. User logs in → Gets access token + refresh token
2. Access token expires → Use refresh token to get new access token
3. Refresh token expires → User must login again

### 2. Role-Based Access Control (RBAC)

**Implementation**:
- Store roles in JWT claims
- Use `@PreAuthorize` annotation
- Check roles in SecurityConfig

**Example**:
```java
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<List<User>> getAllUsers() {
    // Only ADMIN can access
}
```

### 3. Token Blacklisting

**Why Blacklist?**
- When user logs out, we want to invalidate their token
- JWT is stateless, so we can't delete it
- Solution: Store blacklisted tokens in Redis

**How it Works**:
1. User logs out → Token added to Redis blacklist
2. Before validating token → Check if it's blacklisted
3. Blacklisted tokens are rejected

---

## Advanced Features

### 1. Token Blacklisting with Redis

**Implementation**: `TokenBlacklistService`
- Uses Redis for fast lookup
- Auto-expires when token naturally expires
- O(1) lookup time

### 2. Multi-Factor Authentication (MFA)

**Concept** (not fully implemented, but structure provided):
- After login, require additional verification
- Send OTP via email/SMS
- Verify OTP before issuing token

### 3. Rate Limiting

**Concept**:
- Limit login attempts per IP
- Prevent brute force attacks
- Use Redis to track attempts

---

## Complete Code Walkthrough

### Authentication Flow

```
1. User sends login request
   ↓
2. AuthController receives request
   ↓
3. AuthenticationManager authenticates user
   ↓
4. If successful:
   - Generate JWT access token
   - Generate refresh token
   - Return tokens to client
   ↓
5. Client stores tokens
   ↓
6. Client sends request with token in Authorization header
   ↓
7. JwtAuthenticationFilter intercepts request
   ↓
8. Filter extracts and validates token
   ↓
9. If valid:
   - Set authentication in SecurityContext
   - Allow request to proceed
   ↓
10. Controller processes request
```

### Token Generation Process

```java
// 1. Create claims (payload)
Map<String, Object> claims = new HashMap<>();
claims.put("roles", userRoles);

// 2. Build JWT
String token = Jwts.builder()
    .setClaims(claims)                    // Payload
    .setSubject(username)                 // Username
    .setIssuedAt(new Date())              // Issued time
    .setExpiration(expirationDate)        // Expiration
    .signWith(secretKey, HS512)           // Sign
    .compact();                           // Convert to string
```

### Token Validation Process

```java
// 1. Parse token
Claims claims = Jwts.parserBuilder()
    .setSigningKey(secretKey)
    .build()
    .parseClaimsJws(token)
    .getBody();

// 2. Check expiration
if (claims.getExpiration().before(new Date())) {
    throw new ExpiredJwtException(...);
}

// 3. Extract username
String username = claims.getSubject();
```

---

## Interview Questions & Answers

### Basic Level Questions

#### Q1: What is JWT and how does it work?

**Answer**:
JWT (JSON Web Token) is a compact, URL-safe token format for securely transmitting information between parties. It consists of three parts:

1. **Header**: Contains algorithm and token type
2. **Payload**: Contains claims (user info, expiration, etc.)
3. **Signature**: Ensures token hasn't been tampered with

**How it works**:
1. User logs in with credentials
2. Server validates credentials
3. Server generates JWT token (signed with secret key)
4. Server sends token to client
5. Client stores token (localStorage, cookie, etc.)
6. Client sends token in Authorization header with every request
7. Server validates token signature and expiration
8. If valid, server processes request

**Example**:
```
Client Request:
GET /api/user/profile
Authorization: Bearer eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9...

Server:
1. Extracts token from header
2. Validates signature
3. Checks expiration
4. Extracts user info from payload
5. Processes request
```

---

#### Q2: What are the advantages and disadvantages of JWT?

**Advantages**:
1. **Stateless**: No server-side session storage
2. **Scalable**: Works in distributed/microservices architecture
3. **Cross-domain**: Can be used across different domains
4. **Self-contained**: Contains user information
5. **Mobile-friendly**: Works well with mobile apps

**Disadvantages**:
1. **Cannot revoke easily**: Token valid until expiration (need blacklisting)
2. **Size**: Larger than session IDs
3. **Security risk**: If stolen, valid until expiration
4. **No built-in refresh**: Need to implement refresh token mechanism
5. **Secret key management**: Secret key must be secure

---

#### Q3: Explain JWT structure in detail.

**Answer**:
JWT has three parts separated by dots:

**1. Header**:
```json
{
  "alg": "HS512",  // Algorithm (HMAC SHA-512)
  "typ": "JWT"     // Type
}
```
Base64Url encoded → `eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9`

**2. Payload**:
```json
{
  "sub": "john_doe",           // Subject (username)
  "iat": 1516239022,          // Issued at
  "exp": 1516242622,          // Expiration
  "roles": ["ROLE_USER"]      // Custom claims
}
```
Base64Url encoded → `eyJzdWIiOiJqb2huX2RvZSIs...`

**3. Signature**:
```
HMACSHA512(
  base64UrlEncode(header) + "." + base64UrlEncode(payload),
  secret
)
```
→ `SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c`

**Complete JWT**:
```
eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqb2huX2RvZSIs...SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c
```

---

#### Q4: How do you validate a JWT token?

**Answer**:
Validation involves multiple checks:

1. **Structure Check**: Token has 3 parts separated by dots
2. **Signature Verification**: Verify signature using secret key
3. **Expiration Check**: Token not expired
4. **Blacklist Check**: Token not in blacklist (if implemented)

**Code Example**:
```java
public Boolean validateToken(String token) {
    try {
        // Parse and verify signature
        Claims claims = Jwts.parserBuilder()
            .setSigningKey(secretKey)
            .build()
            .parseClaimsJws(token)
            .getBody();
        
        // Check expiration
        if (claims.getExpiration().before(new Date())) {
            return false;
        }
        
        // Check blacklist (if implemented)
        if (tokenBlacklistService.isTokenBlacklisted(token)) {
            return false;
        }
        
        return true;
    } catch (JwtException | IllegalArgumentException e) {
        return false;
    }
}
```

---

### Intermediate Level Questions

#### Q5: What is the difference between access token and refresh token?

**Answer**:

| Aspect | Access Token | Refresh Token |
|--------|--------------|---------------|
| **Purpose** | Access protected resources | Get new access tokens |
| **Expiration** | Short (15 min - 1 hour) | Long (7-30 days) |
| **Storage** | Client-side (localStorage/cookie) | Client-side (secure storage) |
| **Frequency** | Sent with every request | Sent only when refreshing |
| **Security** | If stolen, limited damage | If stolen, can get new access tokens |
| **Revocation** | Hard to revoke | Can be revoked easily |

**Why this design?**
- **Security**: Short-lived access tokens limit damage if stolen
- **Convenience**: Long-lived refresh tokens avoid frequent logins
- **Revocation**: Can revoke refresh token to force re-login

**Flow**:
```
1. Login → Get access token (1 hour) + refresh token (7 days)
2. Use access token for requests
3. Access token expires → Use refresh token to get new access token
4. Refresh token expires → User must login again
```

---

#### Q6: How do you implement token blacklisting?

**Answer**:
Since JWT is stateless, we can't delete tokens. Solution: **Blacklist**.

**Approach 1: Redis (Recommended)**
```java
// On logout
public void blacklistToken(String token, long expirationTime) {
    String key = "blacklist:" + token;
    redisTemplate.opsForValue().set(
        key, 
        "blacklisted", 
        expirationTime, 
        TimeUnit.MILLISECONDS
    );
}

// Before validation
public boolean isTokenBlacklisted(String token) {
    return redisTemplate.hasKey("blacklist:" + token);
}
```

**Approach 2: Database**
- Store blacklisted tokens in database
- Check database before validation
- Slower than Redis

**Approach 3: Token Versioning**
- Store token version in database
- Include version in JWT
- On logout, increment version
- Reject tokens with old version

---

#### Q7: How do you handle token expiration?

**Answer**:
Multiple strategies:

**1. Frontend Detection**:
```javascript
// Check expiration before request
if (isTokenExpired(token)) {
    // Use refresh token to get new access token
    refreshAccessToken();
}
```

**2. Backend Response**:
```java
// If token expired, return 401
if (isTokenExpired(token)) {
    return ResponseEntity.status(401)
        .body(Map.of("error", "Token expired", "code", "TOKEN_EXPIRED"));
}
```

**3. Refresh Token Flow**:
```java
// Client receives 401
// Client uses refresh token
POST /api/auth/refresh
{
    "refreshToken": "..."
}

// Server returns new access token
{
    "accessToken": "new_token",
    "refreshToken": "same_or_new"
}
```

---

#### Q8: Explain the JWT authentication filter.

**Answer**:
The `JwtAuthenticationFilter` is a Spring Security filter that:

1. **Intercepts requests**: Runs before controllers
2. **Extracts token**: Gets JWT from Authorization header
3. **Validates token**: Checks signature, expiration, blacklist
4. **Sets authentication**: If valid, sets authentication in SecurityContext
5. **Continues chain**: Allows request to proceed

**Code Flow**:
```java
@Override
protected void doFilterInternal(HttpServletRequest request, 
                                HttpServletResponse response, 
                                FilterChain chain) {
    // 1. Extract token
    String token = extractToken(request);
    
    // 2. Validate token
    if (token != null && validateToken(token)) {
        // 3. Load user details
        UserDetails userDetails = loadUser(token);
        
        // 4. Create authentication
        Authentication auth = new UsernamePasswordAuthenticationToken(
            userDetails, null, userDetails.getAuthorities()
        );
        
        // 5. Set in SecurityContext
        SecurityContextHolder.getContext().setAuthentication(auth);
    }
    
    // 6. Continue filter chain
    chain.doFilter(request, response);
}
```

**Why OncePerRequestFilter?**
- Ensures filter runs only once per request
- Prevents multiple executions

---

### Advanced Level Questions

#### Q9: How do you secure JWT tokens in production?

**Answer**:
Multiple security measures:

**1. Secret Key Management**:
```java
// ❌ BAD: Hardcoded
String secret = "mySecretKey";

// ✅ GOOD: Environment variable
String secret = System.getenv("JWT_SECRET");

// ✅ BETTER: Key vault (AWS Secrets Manager, HashiCorp Vault)
String secret = keyVault.getSecret("jwt-secret");
```

**2. Token Storage**:
```javascript
// ❌ BAD: localStorage (XSS vulnerable)
localStorage.setItem('token', token);

// ✅ GOOD: httpOnly cookie
// Set-Cookie: token=...; HttpOnly; Secure; SameSite=Strict
```

**3. HTTPS Only**:
- Always use HTTPS in production
- Prevents man-in-the-middle attacks

**4. Short Expiration**:
- Access tokens: 15 minutes - 1 hour
- Refresh tokens: 7-30 days

**5. Token Rotation**:
- Rotate refresh tokens on use
- Prevents token reuse if stolen

**6. Rate Limiting**:
- Limit login attempts
- Prevent brute force attacks

**7. IP Whitelisting** (optional):
- Allow tokens only from specific IPs
- More secure but less flexible

---

#### Q10: What is token rotation and why is it important?

**Answer**:
**Token Rotation**: When refresh token is used, issue a new refresh token and invalidate the old one.

**Why Important?**
- **Security**: If refresh token is stolen, old token becomes invalid
- **Detection**: Can detect token theft (if old token is used after rotation)
- **Compliance**: Meets security standards

**Implementation**:
```java
@PostMapping("/refresh")
public ResponseEntity<?> refreshToken(@RequestBody RefreshRequest request) {
    // 1. Validate old refresh token
    RefreshToken oldToken = validateRefreshToken(request.getRefreshToken());
    
    // 2. Generate new access token
    String newAccessToken = generateAccessToken(oldToken.getUser());
    
    // 3. Generate new refresh token
    String newRefreshToken = generateRefreshToken(oldToken.getUser());
    
    // 4. Delete old refresh token
    deleteRefreshToken(oldToken);
    
    // 5. Return new tokens
    return ResponseEntity.ok(new TokenResponse(newAccessToken, newRefreshToken));
}
```

---

#### Q11: How do you implement role-based access control with JWT?

**Answer**:
Multiple approaches:

**1. Store Roles in JWT**:
```java
// Generate token with roles
Map<String, Object> claims = new HashMap<>();
claims.put("roles", Arrays.asList("ROLE_USER", "ROLE_ADMIN"));
String token = generateToken(claims, username);
```

**2. Extract Roles from Token**:
```java
// In filter or controller
String[] roles = jwtTokenUtil.getRolesFromToken(token);
```

**3. Use @PreAuthorize**:
```java
@GetMapping("/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public ResponseEntity<List<User>> getAllUsers() {
    // Only ADMIN can access
}
```

**4. Method-Level Security**:
```java
@PreAuthorize("hasAnyRole('ADMIN', 'MODERATOR')")
public void deleteUser(Long userId) {
    // ADMIN or MODERATOR can delete
}
```

**5. Custom Security Expression**:
```java
@PreAuthorize("hasRole('ADMIN') or (hasRole('USER') and #userId == authentication.principal.id)")
public User getUser(Long userId) {
    // ADMIN can access any, USER can access own
}
```

---

#### Q12: How do you handle JWT in microservices architecture?

**Answer**:
Challenges and solutions:

**1. Token Propagation**:
```java
// Service A receives request with JWT
// Service A calls Service B
// Propagate JWT in header
HttpHeaders headers = new HttpHeaders();
headers.set("Authorization", "Bearer " + token);
restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<>(headers), Response.class);
```

**2. Token Validation**:
- **Option A**: Each service validates token (needs secret key)
- **Option B**: API Gateway validates, passes user info
- **Option C**: Centralized auth service validates

**3. Service-to-Service Auth**:
```java
// Use service accounts
String serviceToken = generateServiceToken("service-a");
// Include in inter-service calls
```

**4. Token Sharing**:
- Share secret key securely (Key Vault)
- Or use public key cryptography (RS256)

**5. Token Claims**:
```json
{
  "sub": "user123",
  "services": ["service-a", "service-b"],  // Allowed services
  "permissions": ["read", "write"]        // Permissions
}
```

---

#### Q13: What are the security vulnerabilities of JWT and how to mitigate them?

**Answer**:

**1. XSS (Cross-Site Scripting)**:
- **Vulnerability**: Storing token in localStorage
- **Mitigation**: Use httpOnly cookies

**2. CSRF (Cross-Site Request Forgery)**:
- **Vulnerability**: Token in cookie without CSRF protection
- **Mitigation**: Use SameSite cookie attribute, CSRF tokens

**3. Token Theft**:
- **Vulnerability**: Token intercepted
- **Mitigation**: HTTPS, short expiration, token rotation

**4. Algorithm Confusion**:
- **Vulnerability**: Attacker uses "none" algorithm
- **Mitigation**: Always specify algorithm in validation

```java
// ❌ BAD: Allows any algorithm
Jwts.parser().setSigningKey(key).parse(token);

// ✅ GOOD: Specify algorithm
Jwts.parserBuilder()
    .setSigningKey(key)
    .requireAlgorithm(SignatureAlgorithm.HS512)
    .build()
    .parse(token);
```

**5. Secret Key Exposure**:
- **Vulnerability**: Secret key in code
- **Mitigation**: Environment variables, key vaults

**6. Token Replay**:
- **Vulnerability**: Using same token multiple times
- **Mitigation**: Token rotation, nonce, timestamp validation

---

#### Q14: Explain the difference between HS256 and RS256.

**Answer**:

| Aspect | HS256 (Symmetric) | RS256 (Asymmetric) |
|--------|------------------|-------------------|
| **Type** | Symmetric (same key) | Asymmetric (public/private) |
| **Key** | Single secret key | Public/private key pair |
| **Speed** | Faster | Slower |
| **Key Distribution** | Must share secret | Only public key needed |
| **Use Case** | Single service | Microservices, public APIs |
| **Security** | Key must be secret | Private key must be secret |

**HS256 Example**:
```java
// Same key for signing and verification
SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());
String token = Jwts.builder().signWith(key).compact();
Claims claims = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
```

**RS256 Example**:
```java
// Private key for signing
PrivateKey privateKey = getPrivateKey();
String token = Jwts.builder().signWith(privateKey).compact();

// Public key for verification
PublicKey publicKey = getPublicKey();
Claims claims = Jwts.parserBuilder().setSigningKey(publicKey).build().parseClaimsJws(token);
```

**When to Use**:
- **HS256**: Single application, internal services
- **RS256**: Microservices, public APIs, third-party integrations

---

#### Q15: How do you implement refresh token rotation?

**Answer**:
Refresh token rotation enhances security:

**Implementation**:
```java
@PostMapping("/refresh")
public ResponseEntity<?> refreshToken(@RequestBody RefreshRequest request) {
    // 1. Validate refresh token
    RefreshToken oldToken = refreshTokenRepository
        .findByToken(request.getRefreshToken())
        .orElseThrow(() -> new RuntimeException("Invalid token"));
    
    // 2. Check if token is expired
    if (oldToken.isExpired()) {
        throw new RuntimeException("Token expired");
    }
    
    // 3. Check if token was already used (detect theft)
    if (oldToken.isUsed()) {
        // Token theft detected! Revoke all tokens for this user
        revokeAllTokens(oldToken.getUser());
        throw new RuntimeException("Token reuse detected");
    }
    
    // 4. Mark old token as used
    oldToken.setUsed(true);
    refreshTokenRepository.save(oldToken);
    
    // 5. Generate new access token
    String newAccessToken = generateAccessToken(oldToken.getUser());
    
    // 6. Generate new refresh token
    RefreshToken newRefreshToken = createRefreshToken(oldToken.getUser());
    
    // 7. Return new tokens
    return ResponseEntity.ok(new TokenResponse(newAccessToken, newRefreshToken.getToken()));
}
```

**Benefits**:
- Detects token theft
- Limits damage if token is stolen
- Meets security compliance

---

## Best Practices

### 1. Secret Key Management
- ✅ Use environment variables
- ✅ Use key vaults (AWS Secrets Manager, HashiCorp Vault)
- ✅ Rotate keys periodically
- ❌ Never hardcode in source code

### 2. Token Storage
- ✅ Use httpOnly cookies for web apps
- ✅ Use secure storage for mobile apps
- ❌ Avoid localStorage (XSS vulnerable)

### 3. Token Expiration
- ✅ Short expiration for access tokens (15 min - 1 hour)
- ✅ Long expiration for refresh tokens (7-30 days)
- ✅ Implement token rotation

### 4. Security Headers
```java
response.setHeader("X-Content-Type-Options", "nosniff");
response.setHeader("X-Frame-Options", "DENY");
response.setHeader("X-XSS-Protection", "1; mode=block");
```

### 5. HTTPS
- ✅ Always use HTTPS in production
- ✅ Enforce HTTPS (redirect HTTP to HTTPS)

### 6. Rate Limiting
- ✅ Limit login attempts
- ✅ Limit token refresh requests
- ✅ Prevent brute force attacks

---

## Common Mistakes

### 1. Storing Secret Key in Code
```java
// ❌ BAD
String secret = "mySecretKey123";

// ✅ GOOD
String secret = System.getenv("JWT_SECRET");
```

### 2. Not Validating Algorithm
```java
// ❌ BAD: Allows algorithm confusion attack
Jwts.parser().setSigningKey(key).parse(token);

// ✅ GOOD
Jwts.parserBuilder()
    .setSigningKey(key)
    .requireAlgorithm(SignatureAlgorithm.HS512)
    .build()
    .parse(token);
```

### 3. Long Token Expiration
```java
// ❌ BAD: 30 days
.setExpiration(new Date(System.currentTimeMillis() + 2592000000L))

// ✅ GOOD: 1 hour
.setExpiration(new Date(System.currentTimeMillis() + 3600000))
```

### 4. Storing Sensitive Data in Token
```java
// ❌ BAD: Password in token
claims.put("password", user.getPassword());

// ✅ GOOD: Only non-sensitive data
claims.put("username", user.getUsername());
claims.put("roles", user.getRoles());
```

### 5. Not Handling Token Expiration
```java
// ❌ BAD: No expiration handling
String token = generateToken(user);

// ✅ GOOD: Handle expiration
try {
    validateToken(token);
} catch (ExpiredJwtException e) {
    // Return 401, client uses refresh token
    return ResponseEntity.status(401).build();
}
```

---

## Security Considerations

### 1. Token Theft Prevention
- Use HTTPS
- Use httpOnly cookies
- Implement token rotation
- Short token expiration

### 2. Token Validation
- Always validate signature
- Check expiration
- Verify algorithm
- Check blacklist

### 3. Secret Key Security
- Strong secret key (at least 256 bits)
- Secure storage
- Key rotation
- Different keys for different environments

### 4. Token Storage
- httpOnly cookies (web)
- Secure storage (mobile)
- Never in URL
- Never in logs

### 5. Monitoring
- Log failed authentication attempts
- Monitor token usage
- Alert on suspicious activity
- Track token expiration patterns

---

## Running the Application

### Prerequisites
- Java 11+
- Maven 3.6+
- Redis (for token blacklisting - optional)

### Steps

1. **Clone and Navigate**:
```bash
cd jwt-spring-security
```

2. **Build Project**:
```bash
mvn clean install
```

3. **Run Application**:
```bash
mvn spring-boot:run
```

4. **Test Endpoints**:

**Register User**:
```bash
curl -X POST http://localhost:8080/api/auth/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john",
    "password": "password123",
    "email": "john@example.com"
  }'
```

**Login**:
```bash
curl -X POST http://localhost:8080/api/auth/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john",
    "password": "password123"
  }'
```

**Access Protected Resource**:
```bash
curl -X GET http://localhost:8080/api/user/profile \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

---

## Key Takeaways

1. **JWT is stateless** - No server-side session storage
2. **Always validate** - Signature, expiration, algorithm
3. **Use refresh tokens** - For better security and UX
4. **Implement blacklisting** - For logout functionality
5. **Secure storage** - Use httpOnly cookies, not localStorage
6. **Short expiration** - Limit damage if token is stolen
7. **HTTPS only** - In production
8. **Secret key security** - Never in code, use environment variables
9. **Token rotation** - Detect and prevent token theft
10. **Monitor and log** - Track authentication events

---

## Resources

- **JWT.io**: https://jwt.io (JWT debugger and documentation)
- **Spring Security**: https://spring.io/projects/spring-security
- **JJWT Library**: https://github.com/jwtk/jjwt
- **RFC 7519**: JWT specification

---

**Happy Learning! 🔐**

*Remember: Security is not optional. Always follow best practices!*

