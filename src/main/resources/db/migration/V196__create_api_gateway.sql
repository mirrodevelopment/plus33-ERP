-- V196: API Gateway DDL
CREATE TABLE IF NOT EXISTS integration_gateway_key (
    id                  BIGSERIAL PRIMARY KEY,
    api_key             VARCHAR(100) NOT NULL UNIQUE,
    partner_code        VARCHAR(100) NOT NULL,
    rate_limit_per_min  INT NOT NULL DEFAULT 60,
    quota_per_day       INT NOT NULL DEFAULT 10000,
    allowed_routes      VARCHAR(500) NOT NULL DEFAULT '*',
    active              BOOLEAN NOT NULL DEFAULT TRUE,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS integration_gateway_usage_log (
    id                  BIGSERIAL PRIMARY KEY,
    api_key             VARCHAR(100) NOT NULL,
    request_path        VARCHAR(250) NOT NULL,
    http_method         VARCHAR(10) NOT NULL,
    status_code         INT NOT NULL,
    response_time_ms    BIGINT NOT NULL,
    called_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
