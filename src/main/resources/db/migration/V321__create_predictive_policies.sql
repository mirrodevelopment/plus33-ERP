-- V321: Predictive Maintenance Policies
CREATE TABLE IF NOT EXISTS platform_predictive_maintenance_policy (
    id                              BIGSERIAL PRIMARY KEY,
    version                         INT NOT NULL DEFAULT 0,
    policy_code                     VARCHAR(100) NOT NULL UNIQUE,
    policy_name                     VARCHAR(200) NOT NULL,
    asset_type                      VARCHAR(100) NOT NULL,
    prediction_model                VARCHAR(100) NOT NULL,
    minimum_health_score            NUMERIC(5,2) NOT NULL,
    remaining_useful_life_threshold NUMERIC(10,2) NOT NULL,
    failure_probability_threshold   NUMERIC(5,2) NOT NULL,
    maintenance_strategy            VARCHAR(100) NOT NULL, -- Preventive, Predictive, Condition-Based
    priority                        INT NOT NULL DEFAULT 0,
    enabled                         BOOLEAN NOT NULL DEFAULT TRUE,
    created_by                      VARCHAR(100) NOT NULL,
    created_at                      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
