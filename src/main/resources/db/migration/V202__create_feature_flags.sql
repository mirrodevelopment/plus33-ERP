-- V202: Feature Flags DDL
CREATE TABLE IF NOT EXISTS platform_feature_flag (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    flag_key            VARCHAR(250) NOT NULL UNIQUE,
    status              VARCHAR(50) NOT NULL DEFAULT 'DISABLED',
    rollout_percentage  INT NOT NULL DEFAULT 0,
    rules_json          TEXT,
    description         TEXT,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS platform_feature_flag_history (
    id                  BIGSERIAL PRIMARY KEY,
    flag_key            VARCHAR(250) NOT NULL,
    previous_value      VARCHAR(50),
    new_value           VARCHAR(50),
    operator            VARCHAR(100),
    reason              TEXT,
    rollout_percentage  INT NOT NULL DEFAULT 0,
    changed_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
