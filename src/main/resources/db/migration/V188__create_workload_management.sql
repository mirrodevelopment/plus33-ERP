-- V188: Workload Management & Query Optimization DDL
CREATE TABLE IF NOT EXISTS bi_workload_queue (
    id                  BIGSERIAL PRIMARY KEY,
    query_id            VARCHAR(100) NOT NULL UNIQUE,
    user_id             VARCHAR(100) NOT NULL,
    pool_name           VARCHAR(50) NOT NULL DEFAULT 'DEFAULT',
    priority            INTEGER NOT NULL DEFAULT 5,
    query_text          TEXT NOT NULL,
    status              VARCHAR(50) NOT NULL DEFAULT 'QUEUED',
    submitted_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    started_at          TIMESTAMP,
    ended_at            TIMESTAMP
);