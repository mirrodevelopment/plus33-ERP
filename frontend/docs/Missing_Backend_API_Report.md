# Missing Backend API Report

This document records the results of the reverse engineering validation pass between the frontend requirements and backend REST controller routes.

## Detected API Gaps & Missing Backend Capabilities

The Spring Boot backend exposes a wide array of CRUD and transaction endpoints. However, some advanced executive summary statistics are not yet exposed as dedicated endpoints. 

### 1. CRM Customer Sentiment Overview
*   **Required Endpoint:** `GET /api/v1/crm/sentiment`
*   **Codebase Status:** CRUDBase routes only. Sentiment classification logic exists in the Service Layer but lacks a direct REST mapping in `CustomerController`.
*   **Fallback Strategy:** Renders summary calculations by counting active tickets returned from `/api/v1/crm/tickets`.
*   **Suggested DTO:** `CrmSentimentResponse` containing score metrics.
*   **Suggested Controller:** `CustomerController` or a new `CrmAnalyticsController`.
*   **Business Justification:** Required to support the executive CRM overview widget.

### 2. ESG Scope 1/2 Emissions Progress
*   **Required Endpoint:** `GET /api/v1/esg/emissions/summary`
*   **Codebase Status:** POST emissions calculation exists under `EsgController`, but no historical summaries or caching mechanisms are exposed.
*   **Fallback Strategy:** Aggregates entries returned by querying processed calculations.
*   **Suggested DTO:** `EsgEmissionsSummaryResponse` containing monthly scopes totals.
*   **Suggested Controller:** `EsgController`.
*   **Business Justification:** Required to fuel the sustainability progress widget.

### 3. Fleet Charging Schedules Summary
*   **Required Endpoint:** `GET /api/ev/charging/schedules/summary`
*   **Codebase Status:** `EvController` handles POST charging slots scheduling but does not offer summary aggregations for current schedules.
*   **Fallback Strategy:** Fetches slots via list endpoints and aggregates states on the client.
*   **Suggested DTO:** `EvChargingSummaryResponse`.
*   **Suggested Controller:** `EvController`.
*   **Business Justification:** Required to show dynamic charging grid indicators on the WMS Physical Topology.
