-- V204: Backup & Recovery DDL
CREATE TABLE IF NOT EXISTS platform_backup_schedule (
    id                  BIGSERIAL PRIMARY KEY,
    schedule_cron       VARCHAR(100) NOT NULL,
    target_type         VARCHAR(50) NOT NULL, -- e.g. DATABASE, FILE_STORAGE
    destination         VARCHAR(250) NOT NULL,
    active              BOOLEAN NOT NULL DEFAULT TRUE,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS platform_backup_run (
    id                  BIGSERIAL PRIMARY KEY,
    backup_file         VARCHAR(250) NOT NULL,
    status              VARCHAR(50) NOT NULL,
    size_bytes          BIGINT NOT NULL DEFAULT 0,
    checksum            VARCHAR(100),
    sandbox_restored    BOOLEAN NOT NULL DEFAULT FALSE,
    integrity_checked   BOOLEAN NOT NULL DEFAULT FALSE,
    integrity_message   TEXT,
    completed_at        TIMESTAMP,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
