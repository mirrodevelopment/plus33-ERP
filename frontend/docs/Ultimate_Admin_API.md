# Ultimate Admin Dashboard - API Mapping Manual

This document details the exact REST API controller routes on the Spring Boot backend integrated with the Ultimate Admin Dashboard.

## Authentication Operations

*   **Endpoint:** `POST /api/v1/auth/login`
*   **Request Envelope:**
    ```json
    {
      "email": "admin@plus33.coffee",
      "password": "..."
    }
    ```
*   **Response Envelope:**
    ```json
    {
      "accessToken": "...",
      "tokenType": "Bearer",
      "expiresIn": 3600
    }
    ```

## BI Operations

*   **Endpoint:** `GET /api/bi/kpis`
    *   **Retrieves:** All active definitions.
*   **Endpoint:** `GET /api/bi/system/stats`
    *   **Retrieves:** KPI counts and running tasks.
*   **Endpoint:** `GET /api/bi/snapshots/{companyId}/{kpiCode}`
    *   **Retrieves:** Daily trend coordinates for metrics.

## Platform Operations

*   **Endpoint:** `GET /api/platform/dashboard`
    *   **Retrieves:** System indicators (total cache nodes, Kubernetes pods count, regions profiles, circuit breaker status).
