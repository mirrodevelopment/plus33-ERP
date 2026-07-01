-- V201: Platform Configuration DDL
CREATE TABLE IF NOT EXISTS platform_config (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 1,
    config_key          VARCHAR(250) NOT NULL UNIQUE,
    config_value        TEXT NOT NULL,
    profile             VARCHAR(50) NOT NULL DEFAULT 'default',
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS platform_config_version (
    id                  BIGSERIAL PRIMARY KEY,
    config_id           BIGINT NOT NULL,
    version             INT NOT NULL,
    previous_version    INT,
    config_value        TEXT NOT NULL,
    effective_from      TIMESTAMP NOT NULL,
    effective_to        TIMESTAMP,
    checksum            VARCHAR(100) NOT NULL,
    modified_by         VARCHAR(100) NOT NULL,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(config_id, version)
);
