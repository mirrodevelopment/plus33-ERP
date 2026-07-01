-- V258: Reinforcement Learning Policies
CREATE TABLE IF NOT EXISTS platform_rl_policy (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    policy_code         VARCHAR(100) NOT NULL UNIQUE,
    current_state_json  TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS platform_rl_policy_update (
    id                  BIGSERIAL PRIMARY KEY,
    policy_id           BIGINT NOT NULL,
    action_taken        VARCHAR(150) NOT NULL,
    reward              NUMERIC(19,4) NOT NULL,
    state_json          TEXT NOT NULL,
    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
