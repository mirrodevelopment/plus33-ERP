-- V291: Edge Node Registry
CREATE TABLE IF NOT EXISTS platform_edge_node (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    node_code           VARCHAR(100) NOT NULL UNIQUE,
    node_name           VARCHAR(200) NOT NULL,
    edge_cluster        VARCHAR(100),
    hardware_model      VARCHAR(100),
    firmware_version    VARCHAR(50),
    os_version          VARCHAR(50),
    cpu_architecture    VARCHAR(50),
    serial_number       VARCHAR(100),
    status              VARCHAR(50) NOT NULL, -- ACTIVE, OFFLINE, PROVISIONING, DECOMMISSIONED
    last_seen           TIMESTAMP,
    ip_address          VARCHAR(45),
    mac_address         VARCHAR(17),
    location            VARCHAR(200),
    tenant_id           VARCHAR(100) NOT NULL DEFAULT 'DEFAULT_TENANT',
    certificate_thumbprint VARCHAR(256),
    provisioned_at      TIMESTAMP,
    heartbeat_interval_seconds INT NOT NULL DEFAULT 30,
    sync_interval_seconds      INT NOT NULL DEFAULT 60,
    offline_timeout_seconds    INT NOT NULL DEFAULT 120,
    software_version    VARCHAR(50),
    deployment_group    VARCHAR(100)
);
