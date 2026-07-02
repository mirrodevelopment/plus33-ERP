-- V293: Edge Health Metrics
CREATE TABLE IF NOT EXISTS platform_edge_health_metric (
    id                  BIGSERIAL PRIMARY KEY,
    node_id             BIGINT NOT NULL,
    cpu_usage           NUMERIC(5,2) NOT NULL,
    memory_usage        NUMERIC(5,2) NOT NULL,
    disk_usage          NUMERIC(5,2) NOT NULL,
    temperature         NUMERIC(5,2),
    network_latency_ms  INT,
    packet_loss_rate    NUMERIC(5,2),
    battery_level       NUMERIC(5,2),
    uptime_seconds      BIGINT NOT NULL,
    active_threads      INT NOT NULL,
    telemetry_backlog   INT NOT NULL DEFAULT 0,
    queue_depth         INT NOT NULL DEFAULT 0,
    sync_lag_seconds    INT NOT NULL DEFAULT 0,
    recorded_at         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
