-- V314: Device Config Profiles
CREATE TABLE IF NOT EXISTS platform_device_config_profile (
    id                      BIGSERIAL PRIMARY KEY,
    version                 INT NOT NULL DEFAULT 0,
    profile_code            VARCHAR(100) NOT NULL UNIQUE,
    profile_name            VARCHAR(200) NOT NULL,
    profile_version         VARCHAR(50) NOT NULL,
    checksum                VARCHAR(64) NOT NULL,
    configuration_json      TEXT NOT NULL,
    rollback_profile_id     BIGINT,
    effective_from          TIMESTAMP,
    effective_to            TIMESTAMP,
    assignment_scope        VARCHAR(200) NOT NULL -- Base Profile, Warehouse Profile, POS Profile, Edge Gateway Profile
);
