-- V264: Autonomous Rerouting
CREATE TABLE IF NOT EXISTS platform_rerouting_policy (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    policy_code         VARCHAR(100) NOT NULL UNIQUE,
    trigger_threshold_minutes INT NOT NULL
);

CREATE TABLE IF NOT EXISTS platform_autonomous_rerouting (
    id                  BIGSERIAL PRIMARY KEY,
    transit_route_id    BIGINT NOT NULL,
    policy_id           BIGINT NOT NULL,
    confidence          NUMERIC(5,2) NOT NULL,
    suggested_route_json TEXT NOT NULL,
    status              VARCHAR(50) NOT NULL DEFAULT 'PENDING'
);

CREATE TABLE IF NOT EXISTS platform_rerouting_execution (
    id                  BIGSERIAL PRIMARY KEY,
    rerouting_id        BIGINT NOT NULL,
    status              VARCHAR(50) NOT NULL DEFAULT 'EXECUTED',
    executed_at         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
