-- V224: Policy Engine DDL
CREATE TABLE IF NOT EXISTS platform_access_policy (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    policy_code         VARCHAR(100) NOT NULL UNIQUE,
    rego_content        TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS platform_policy_audit (
    id                  BIGSERIAL PRIMARY KEY,
    policy_code         VARCHAR(100) NOT NULL,
    user_identity       VARCHAR(100) NOT NULL,
    action              VARCHAR(150) NOT NULL,
    decision            VARCHAR(50) NOT NULL, -- ALLOW, DENY
    evaluated_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
