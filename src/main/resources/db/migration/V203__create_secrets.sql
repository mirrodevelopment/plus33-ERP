-- V203: Secrets DDL
CREATE TABLE IF NOT EXISTS platform_secret_definition (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    alias_path          VARCHAR(250) NOT NULL UNIQUE,
    secret_key          VARCHAR(250) NOT NULL,
    rotation_policy     VARCHAR(100),
    next_rotation       TIMESTAMP,
    last_rotation       TIMESTAMP,
    provider_version    VARCHAR(50) NOT NULL DEFAULT '1',
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
