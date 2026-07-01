-- V220: AIOps DDL
CREATE TABLE IF NOT EXISTS platform_aiops_model (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    model_name          VARCHAR(150) NOT NULL UNIQUE,
    accuracy            NUMERIC(5,2) NOT NULL,
    status              VARCHAR(50) NOT NULL DEFAULT 'ACTIVE'
);

CREATE TABLE IF NOT EXISTS platform_capacity_forecast (
    id                  BIGSERIAL PRIMARY KEY,
    metric_name         VARCHAR(100) NOT NULL,
    forecast_value      NUMERIC(19,4) NOT NULL,
    confidence          NUMERIC(5,2) NOT NULL,
    target_time         TIMESTAMP NOT NULL
);
