-- V254: Root Cause Analysis
CREATE TABLE IF NOT EXISTS platform_root_cause_analysis (
    id                  BIGSERIAL PRIMARY KEY,
    causal_model_id     BIGINT NOT NULL,
    anomaly_event       VARCHAR(150) NOT NULL,
    probabilities_json  TEXT NOT NULL,
    root_cause_node     VARCHAR(150) NOT NULL,
    explanation         TEXT NOT NULL,
    analyzed_at         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
