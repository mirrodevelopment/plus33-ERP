-- V333: Route Cost Metrics
CREATE TABLE IF NOT EXISTS platform_route_cost_log (
    id                      BIGSERIAL PRIMARY KEY,
    route_id                BIGINT NOT NULL,
    fuel_cost               NUMERIC(12,2) NOT NULL,
    driver_cost             NUMERIC(12,2) NOT NULL,
    maintenance_cost        NUMERIC(12,2) NOT NULL,
    toll_cost               NUMERIC(12,2) NOT NULL,
    parking_cost            NUMERIC(12,2) NOT NULL,
    insurance_cost          NUMERIC(12,2) NOT NULL,
    depreciation_cost       NUMERIC(12,2) NOT NULL,
    carbon_cost             NUMERIC(12,2) NOT NULL,
    total_cost              NUMERIC(12,2) NOT NULL,
    currency                VARCHAR(3) NOT NULL DEFAULT 'USD',
    logged_at               TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
