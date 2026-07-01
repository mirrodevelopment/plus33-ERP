-- V219: SLO/SLA DDL
CREATE TABLE IF NOT EXISTS platform_slo (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    name                VARCHAR(150) NOT NULL UNIQUE,
    target_percentage   NUMERIC(5,2) NOT NULL,
    service_name        VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS platform_slo_measurement (
    id                  BIGSERIAL PRIMARY KEY,
    slo_id              BIGINT NOT NULL,
    current_percentage  NUMERIC(5,2) NOT NULL,
    error_budget        NUMERIC(5,2) NOT NULL,
    recorded_at         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
