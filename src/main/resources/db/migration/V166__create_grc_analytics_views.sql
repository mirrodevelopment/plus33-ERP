-- V166: GRC Analytics Views
CREATE TABLE IF NOT EXISTS grc_analytics_snapshots (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    metric_name VARCHAR(80) NOT NULL,
    metric_value NUMERIC(12, 4) NOT NULL,
    recorded_date DATE NOT NULL
);
