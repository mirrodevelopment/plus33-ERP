-- V255: Predictive Maintenance
CREATE TABLE IF NOT EXISTS platform_forecast_model_registry (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    model_code          VARCHAR(100) NOT NULL UNIQUE,
    accuracy_score      NUMERIC(5,2) NOT NULL,
    status              VARCHAR(50) NOT NULL DEFAULT 'ACTIVE'
);

CREATE TABLE IF NOT EXISTS platform_predictive_maintenance_forecast (
    id                  BIGSERIAL PRIMARY KEY,
    model_id            BIGINT NOT NULL,
    twin_instance_id    BIGINT NOT NULL,
    failure_probability NUMERIC(5,2) NOT NULL,
    expected_failure_at TIMESTAMP NOT NULL,
    generated_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
