-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 258
-- File              : V258__create_rl_policies.sql
-- Operation Type    : Schema Creation
-- Purpose           : create rl policies
--
-- Tables Created    : IF, IF
-- Tables Altered    : N/A
-- Seed Data For     : N/A
-- Indexes           : N/A
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
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
