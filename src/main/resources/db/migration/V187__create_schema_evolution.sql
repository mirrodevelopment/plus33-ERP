-- V187: Schema Drift & Evolution Tracking Schema
CREATE TABLE IF NOT EXISTS bi_schema_evolution_history (
    id                  BIGSERIAL PRIMARY KEY,
    table_name          VARCHAR(100) NOT NULL,
    detected_at         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    action_type         VARCHAR(50) NOT NULL,
    action_detail       TEXT NOT NULL,
    validation_status   VARCHAR(50) NOT NULL DEFAULT 'PENDING'
);