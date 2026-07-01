-- V218: Distributed Logs DDL
CREATE TABLE IF NOT EXISTS platform_log_entry (
    id                  BIGSERIAL PRIMARY KEY,
    trace_id            VARCHAR(100),
    span_id             VARCHAR(100),
    service_name        VARCHAR(100) NOT NULL,
    node_id             VARCHAR(100) NOT NULL,
    log_level           VARCHAR(50) NOT NULL,
    logger              VARCHAR(250) NOT NULL,
    message             TEXT NOT NULL,
    json_payload        TEXT,
    timestamp           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
