-- V152: HCM Analytics projections
CREATE TABLE IF NOT EXISTS hcm_analytics_snapshots (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    metric_name VARCHAR(50) NOT NULL,
    metric_value NUMERIC(12, 4) NOT NULL,
    recorded_date DATE NOT NULL
);
