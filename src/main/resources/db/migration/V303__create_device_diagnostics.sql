-- V303: Device Diagnostics & Crash Reports
CREATE TABLE IF NOT EXISTS platform_device_diagnostic (
    id                      BIGSERIAL PRIMARY KEY,
    node_id                 BIGINT NOT NULL,
    cpu_usage               NUMERIC(5,2),
    memory_usage            NUMERIC(5,2),
    disk_usage              NUMERIC(5,2),
    temperature             NUMERIC(5,2),
    running_services        TEXT,
    firmware_version        VARCHAR(50),
    uptime_seconds          BIGINT,
    network_quality         VARCHAR(50),
    logs                    TEXT,
    stack_trace             TEXT,
    exception_message       VARCHAR(500),
    thread_dump             TEXT,
    core_dump_location      VARCHAR(500),
    reported_at             TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
