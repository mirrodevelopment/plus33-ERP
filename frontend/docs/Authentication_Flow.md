# Authentication Sequence Flow

This document details the authentication and credentials verification sequence for the PLUS33 ERP client.

## Sequential Authentication Sequence

```mermaid
sequenceDiagram
    participant User as Frontend Client View
    participant Auth as AuthService.js (Client)
    participant Api as ApiClient (client.js)
    participant Server as AuthController (Spring Boot)
    participant Sec as DaoAuthenticationProvider
    participant Db as PostgreSQL Database

    User->>Auth: Submit login form (LoginRequest)
    Auth->>Api: post('/api/v1/auth/login', LoginRequest)
    Api->>Server: HTTP POST /api/v1/auth/login
    Server->>Sec: authenticate(UsernamePasswordAuthenticationToken)
    Sec->>Db: findByEmail(email)
    Db-->>Sec: User Details (BCrypt password, Authorities)
    Sec-->>Server: Authentication (Authenticated)
    Server-->>Api: TokenResponse (accessToken, tokenType, expiresIn)
    Api-->>Auth: ApiResponse<TokenResponse>
    Auth->>Auth: Extract authorities & permissions
    Auth->>Auth: Cache JWT & user profile in LocalStorage
    Auth-->>User: Redirect to #dashboard
```

## Session Recovery & Interception

1.  **Anti-Flicker Bootstrapper:** On page load, `index.html` matches the cached token in `LocalStorage`.
2.  **Bearer Interception:** Every outgoing request through `ApiClient` gets decorated with the `Authorization: Bearer <JWT>` header.
3.  **Authentication Expiry Handling:** If the backend returns a `401 Unauthorized` response code, `ApiClient` triggers a session expired event, clearing the cached token and redirecting the page to `#login`.
