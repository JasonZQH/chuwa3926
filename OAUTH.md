# OAuth and JWT Authorization Notes

This document explains two things:

1. The current implemented authentication and authorization solution in this project
2. The steps required to evolve it into a real OAuth 2.0 Authorization Server

## Current Status

This project currently uses the allowed fallback approach from the assignment:

- JWT Bearer token-only authentication
- Stateless authorization with Spring Security
- Protected post and comment APIs

This is not a full OAuth 2.0 Authorization Server yet.

It is a resource-server-style JWT flow implemented inside the same Spring Boot application.

## What Is Already Implemented

### 1. JWT signin endpoint

The application exposes a JWT login endpoint:

- `POST /api/v1/auth/jwt/signin`

Implementation:

- `src/main/java/com/chuwa/redbook/controller/AuthJWTController.java`

When a user signs in successfully:

- Spring Security authenticates the username/email and password
- `JwtTokenProvider` generates a JWT access token
- The response returns:
  - `accessToken`
  - `tokenType = Bearer`

Relevant code:

- `src/main/java/com/chuwa/redbook/controller/AuthJWTController.java`
- `src/main/java/com/chuwa/redbook/security/JwtTokenProvider.java`
- `src/main/java/com/chuwa/redbook/payload/security/JWTAuthResponse.java`

### 2. JWT signup endpoint

The application exposes a registration endpoint:

- `POST /api/v1/auth/jwt/signup`

Implementation:

- `src/main/java/com/chuwa/redbook/controller/AuthJWTController.java`

During signup:

- The user is stored in the database
- The password is encoded with BCrypt
- A role is assigned

### 3. Stateless security configuration

The security chain is configured as stateless:

- CSRF disabled for API usage
- Session creation policy set to `STATELESS`
- JWT filter added before `UsernamePasswordAuthenticationFilter`

Implementation:

- `src/main/java/com/chuwa/redbook/config/security/SecurityDBJWTConfig.java`

### 4. JWT request filter

Each protected request is processed by `JwtAuthenticationFilter`.

Flow:

1. Read the `Authorization` header
2. Extract `Bearer <token>`
3. Validate the JWT
4. Read the username from the token
5. Load the user from the database
6. Put an authenticated object into `SecurityContextHolder`

Implementation:

- `src/main/java/com/chuwa/redbook/security/JwtAuthenticationFilter.java`
- `src/main/java/com/chuwa/redbook/security/CustomUserDetailsService.java`
- `src/main/java/com/chuwa/redbook/security/JwtTokenProvider.java`

### 5. Protected post and comment APIs

The security configuration now requires authentication for post routes:

- `/api/v1/posts/**`

Method-level authorization is applied in controllers:

- `PostController`
- `CommentController`

Current authorization rules:

- `GET /api/v1/posts` requires `ADMIN` or `USER`
- `GET /api/v1/posts/{id}` requires `ADMIN` or `USER`
- `POST /api/v1/posts` requires `ADMIN` or `USER`
- `PUT /api/v1/posts/{id}` requires `ADMIN`
- `DELETE /api/v1/posts/{id}` requires `ADMIN`
- All comment CRUD endpoints require `ADMIN` or `USER`

Relevant code:

- `src/main/java/com/chuwa/redbook/config/security/SecurityDBJWTConfig.java`
- `src/main/java/com/chuwa/redbook/controller/PostController.java`
- `src/main/java/com/chuwa/redbook/controller/CommentController.java`

## Current Request Flow

### Signin flow

1. Client sends credentials to `/api/v1/auth/jwt/signin`
2. `AuthenticationManager` authenticates the request
3. `JwtTokenProvider` generates a signed JWT
4. API returns a Bearer token

### Protected API flow

1. Client calls a protected API
2. Client sends `Authorization: Bearer <token>`
3. `JwtAuthenticationFilter` validates the token
4. Spring Security restores authentication from the token
5. Controller method is checked by `@PreAuthorize`
6. API returns data or `401/403`

## Example Usage

### Sign in

```bash
curl -k -X POST https://localhost:8443/api/v1/auth/jwt/signin \
  -H "Content-Type: application/json" \
  -d '{
    "accountOrEmail":"test@example.com",
    "password":"123456"
  }'
```

Example response:

```json
{
  "accessToken": "eyJhbGciOiJIUzUxMiJ9...",
  "tokenType": "Bearer"
}
```

### Call a protected post API

```bash
curl -k https://localhost:8443/api/v1/posts \
  -H "Authorization: Bearer YOUR_TOKEN"
```

### Expected behavior

- Without token: `401`
- With invalid token: `400` or `401` depending on failure point
- With valid token and allowed role: `200`
- With valid token but insufficient role: `403`

## Why This Is Not a Full OAuth 2.0 Authorization Server

The current solution is valid as a JWT Bearer token fallback, but it is still missing standard OAuth 2.0 server capabilities.

Missing pieces include:

- No client registration model
- No `client_id` / `client_secret`
- No authorization endpoint such as `/oauth2/authorize`
- No token endpoint such as `/oauth2/token`
- No authorization code flow
- No refresh token support
- No scopes model
- No consent screen
- No JWK set endpoint
- No token introspection endpoint
- No OpenID Connect metadata endpoint

In short:

- The current implementation authenticates users and issues JWTs
- A real OAuth 2.0 Authorization Server authenticates both users and clients, then issues standardized tokens through standard endpoints and grant flows

## Recommended Upgrade Path to a Real OAuth 2.0 Authorization Server

The cleanest path is to add a true Authorization Server layer and keep the current post/comment APIs as protected resources.

### Step 1. Choose a compatible Authorization Server library

Use a Spring Authorization Server version that is compatible with:

- Spring Boot 2.7.x
- Spring Security 5.7.x

Do not blindly add the newest version if it targets Spring Boot 3 / Spring Security 6 only.

### Step 2. Add Authorization Server dependencies

You will need dependencies for:

- Spring Authorization Server
- Spring Security OAuth2 JOSE support

Typical goals:

- expose OAuth 2.0 endpoints
- sign JWT access tokens
- publish public keys

### Step 3. Split security into multiple filter chains

You should create separate security chains for:

1. Authorization Server endpoints
2. Application API endpoints
3. Swagger and public endpoints if needed

Why:

- OAuth endpoints have different rules from normal business APIs
- `/oauth2/authorize` and `/oauth2/token` need special handling

### Step 4. Add registered client support

A real OAuth server must know which clients are allowed to request tokens.

Implement:

- in-memory clients for quick setup, or
- database-backed `RegisteredClientRepository`

Typical fields:

- client id
- encoded client secret
- redirect URIs
- authorized grant types
- scopes
- token settings

### Step 5. Add signing keys and JWK support

A real Authorization Server should expose signing keys.

Implement:

- RSA key pair generation or loading from keystore
- JWK source bean
- JWK set endpoint

This allows downstream resource servers to validate JWTs correctly.

### Step 6. Define token issuance strategy

Decide which grant types you will support.

Recommended for a web application:

- Authorization Code
- Refresh Token

Recommended for service-to-service:

- Client Credentials

Avoid relying on Resource Owner Password Credentials because it is deprecated and not considered a modern OAuth flow.

### Step 7. Replace custom signin as the main token entry point

Right now, token issuance happens through:

- `POST /api/v1/auth/jwt/signin`

In a real OAuth design, token issuance should move to standardized endpoints like:

- `GET /oauth2/authorize`
- `POST /oauth2/token`

Your current signin endpoint can remain temporarily for backward compatibility, but it should no longer be the primary token issuance path.

### Step 8. Add scopes and map them to roles/authorities

Today the app mainly uses roles:

- `ROLE_ADMIN`
- `ROLE_USER`

With OAuth 2.0, introduce scopes such as:

- `posts.read`
- `posts.write`
- `comments.read`
- `comments.write`

Then map:

- scopes to token claims
- scopes to Spring Security authorities
- authorities to controller access rules

This is the proper bridge between OAuth clients and your business authorization.

### Step 9. Configure the API side as a resource server

The post and comment APIs should validate OAuth-issued JWTs as a resource server.

That means:

- enable OAuth2 resource server JWT support
- validate JWT signature
- validate issuer and audience if used
- derive authorities from token scopes and claims

At that point, the post/comment APIs stop depending on the custom login endpoint and instead trust tokens issued by the Authorization Server.

### Step 10. Add refresh tokens

For real OAuth usability, support refresh tokens so clients do not need to repeatedly prompt the user to log in.

This requires:

- refresh token grant support
- token storage strategy
- expiration policy

### Step 11. Add consent and authorization persistence

If you support authorization code flow, you also need:

- authorization consent storage
- authorization persistence

This is usually provided via:

- `OAuth2AuthorizationService`
- `OAuth2AuthorizationConsentService`

Store them in memory first, then move to database if needed.

### Step 12. Publish metadata endpoints

A complete Authorization Server should expose discovery and key metadata, for example:

- `/.well-known/openid-configuration`
- `/oauth2/jwks`

These are critical if external clients or tools must integrate with your server.

### Step 13. Update controllers and authorization rules

Once scopes are available, update authorization expressions.

Examples:

- `hasAuthority('SCOPE_posts.read')`
- `hasAuthority('SCOPE_posts.write')`

This is more OAuth-native than only checking `ROLE_ADMIN` and `ROLE_USER`.

### Step 14. Update documentation and test flows

After real OAuth support is added, document:

- registered clients
- redirect URIs
- grant types
- token endpoint usage
- how to call protected APIs with OAuth-issued tokens

Also test:

- authorization code flow
- refresh token flow
- client credentials flow if implemented
- unauthorized access
- insufficient scope responses

## Suggested Implementation Order

If you want to upgrade this repo incrementally, use this order:

1. Keep the current JWT Bearer flow working
2. Add Spring Authorization Server dependencies
3. Add authorization server config and key material
4. Add one registered client
5. Support authorization code flow
6. Support refresh token flow
7. Convert post/comment APIs to resource server scope-based protection
8. Deprecate the custom `/api/v1/auth/jwt/signin` token issuance path

## Practical Conclusion

For the assignment:

- The current implementation satisfies the allowed fallback path:
  - JWT token-only authentication and authorization
  - Bearer token format
  - protected post and comment APIs

For a full OAuth 2.0 implementation:

- you need standardized OAuth endpoints
- client registration
- scopes
- JWK support
- token grant flows
- resource server JWT validation based on OAuth-issued tokens

Until that full migration is done, this project should be described as:

`JWT Bearer token-only authorization and authentication implemented inside the Redbook application, with all post and comment APIs protected by Spring Security and validated through JWT.`
