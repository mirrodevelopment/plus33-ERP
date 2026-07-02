-- V302: OTA Campaigns & Node Executions
CREATE TABLE IF NOT EXISTS platform_ota_campaign (
    id                      BIGSERIAL PRIMARY KEY,
    version                 INT NOT NULL DEFAULT 0,
    campaign_name           VARCHAR(200) NOT NULL,
    package_id              BIGINT NOT NULL,
    scheduled_start         TIMESTAMP,
    scheduled_end           TIMESTAMP,
    rollout_percentage      INT NOT NULL DEFAULT 100,
    batch_size              INT NOT NULL DEFAULT 10,
    retry_policy            VARCHAR(100),
    failure_threshold       INT NOT NULL DEFAULT 5,
    rollback_enabled        BOOLEAN NOT NULL DEFAULT TRUE,
    status                  VARCHAR(50) NOT NULL, -- PENDING, ACTIVE, COMPLETED, ROLLED_BACK, FAILED
    created_at              TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS platform_ota_node_execution (
    id                      BIGSERIAL PRIMARY KEY,
    campaign_id             BIGINT NOT NULL,
    node_id                 BIGINT NOT NULL,
    download_started        TIMESTAMP,
    download_completed      TIMESTAMP,
    install_started         TIMESTAMP,
    install_completed       TIMESTAMP,
    reboot_required         BOOLEAN NOT NULL DEFAULT FALSE,
    reboot_completed        BOOLEAN NOT NULL DEFAULT FALSE,
    execution_status        VARCHAR(50) NOT NULL, -- QUEUED, DOWNLOADING, INSTALLING, SUCCESS, FAILED, ROLLED_BACK
    failure_reason          VARCHAR(500),
    updated_at              TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
