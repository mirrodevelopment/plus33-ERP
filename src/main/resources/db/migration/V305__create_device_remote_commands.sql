-- V305: Device Remote Commands
CREATE TABLE IF NOT EXISTS platform_device_remote_command (
    id                      BIGSERIAL PRIMARY KEY,
    node_id                 BIGINT NOT NULL,
    command_type            VARCHAR(100) NOT NULL, -- Restart, Shutdown, Sync, CollectLogs, RotateCertificate
    parameters              TEXT,
    signature               VARCHAR(256) NOT NULL,
    timeout_seconds         INT NOT NULL DEFAULT 60,
    status                  VARCHAR(50) NOT NULL, -- DISPATCHED, EXECUTED, FAILED, TIMED_OUT
    response_payload        TEXT,
    exit_code               INT,
    execution_duration_ms   BIGINT,
    dispatched_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    executed_at             TIMESTAMP
);
