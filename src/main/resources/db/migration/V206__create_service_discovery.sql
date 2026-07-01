-- V206: Service Discovery & Maintenance Window DDL
CREATE TABLE IF NOT EXISTS platform_discovery_node (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    node_code           VARCHAR(100) NOT NULL UNIQUE,
    ip_address          VARCHAR(100) NOT NULL,
    port                INT NOT NULL,
    status              VARCHAR(50) NOT NULL DEFAULT 'STARTING',
    cluster_role        VARCHAR(50) NOT NULL DEFAULT 'FOLLOWER',
    last_heartbeat      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS platform_maintenance_window (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    start_time          TIMESTAMP NOT NULL,
    end_time            TIMESTAMP NOT NULL,
    affected_services   VARCHAR(500) NOT NULL,
    notification_msg    TEXT,
    allowed_users       VARCHAR(500),
    active              BOOLEAN NOT NULL DEFAULT TRUE
);
