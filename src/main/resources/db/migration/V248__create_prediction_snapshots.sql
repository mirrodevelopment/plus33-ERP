-- V248: Prediction snapshots
CREATE TABLE IF NOT EXISTS platform_twin_prediction_snapshot (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    instance_id         BIGINT NOT NULL,
    target_time         TIMESTAMP NOT NULL,
    predicted_state_json TEXT NOT NULL,
    confidence          NUMERIC(5,2) NOT NULL,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
