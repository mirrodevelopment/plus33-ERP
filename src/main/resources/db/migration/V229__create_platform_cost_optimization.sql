-- V229: Cost Optimization Recommendations DDL
CREATE TABLE IF NOT EXISTS platform_cost_recommendation (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    resource_id         VARCHAR(250) NOT NULL,
    recommendation_type VARCHAR(100) NOT NULL, -- RIGHTSIZE, TERMINATE, PURCHASE_RI
    savings_potential   NUMERIC(19,4) NOT NULL,
    reason              TEXT NOT NULL,
    status              VARCHAR(50) NOT NULL DEFAULT 'ACTIVE', -- ACTIVE, APPLIED, DISMISSED
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
