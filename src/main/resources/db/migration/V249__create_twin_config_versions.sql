-- V249: Twin configuration versions
CREATE TABLE IF NOT EXISTS platform_twin_config_version (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    instance_id         BIGINT NOT NULL,
    config_version      VARCHAR(50) NOT NULL,
    configuration_json  TEXT NOT NULL,
    effective_from      TIMESTAMP NOT NULL,
    effective_to        TIMESTAMP
);

CREATE TABLE IF NOT EXISTS platform_twin_config_history (
    id                  BIGSERIAL PRIMARY KEY,
    instance_id         BIGINT NOT NULL,
    previous_version    VARCHAR(50),
    new_version         VARCHAR(50) NOT NULL,
    operator            VARCHAR(100) NOT NULL,
    reason              TEXT,
    changed_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
