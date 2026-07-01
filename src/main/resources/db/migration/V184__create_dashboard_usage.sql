-- V184: Dashboard Analytics and Usage Logging Schema
CREATE TABLE IF NOT EXISTS bi_usage_log (
    id                  BIGSERIAL PRIMARY KEY,
    user_id             VARCHAR(100) NOT NULL,
    dashboard_code      VARCHAR(100) NOT NULL,
    action_type         VARCHAR(50) NOT NULL,
    duration_ms         BIGINT NOT NULL,
    company_id          BIGINT NOT NULL,
    ip_address          VARCHAR(45),
    accessed_at         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);