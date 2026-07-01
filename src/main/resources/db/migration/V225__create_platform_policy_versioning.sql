-- V225: Policy Versioning DDL
CREATE TABLE IF NOT EXISTS platform_policy_version (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    policy_id           BIGINT NOT NULL,
    policy_version      VARCHAR(50) NOT NULL,
    rego_content        TEXT NOT NULL,
    effective_from      TIMESTAMP NOT NULL,
    effective_to        TIMESTAMP
);

CREATE TABLE IF NOT EXISTS platform_policy_history (
    id                  BIGSERIAL PRIMARY KEY,
    policy_code         VARCHAR(100) NOT NULL,
    previous_version    VARCHAR(50),
    new_version         VARCHAR(50) NOT NULL,
    operator            VARCHAR(100) NOT NULL,
    reason              TEXT,
    changed_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
