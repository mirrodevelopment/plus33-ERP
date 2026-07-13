-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 193
-- File              : V193__create_inbox.sql
-- Operation Type    : Schema Creation
-- Purpose           : create inbox
--
-- Tables Created    : IF, IF, IF
-- Tables Altered    : N/A
-- Seed Data For     : N/A
-- Indexes           : N/A
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
-- V193: Inbox schema DDL
CREATE TABLE IF NOT EXISTS integration_inbox (
    id                  BIGSERIAL PRIMARY KEY,
    event_id            VARCHAR(100) NOT NULL UNIQUE,
    topic               VARCHAR(250) NOT NULL,
    received_at         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS processed_messages (
    id                  BIGSERIAL PRIMARY KEY,
    message_id          VARCHAR(100) NOT NULL UNIQUE,
    consumer_group      VARCHAR(100) NOT NULL,
    processed_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS consumer_checkpoint (
    id                     BIGSERIAL PRIMARY KEY,
    group_name             VARCHAR(100) NOT NULL,
    consumer_name          VARCHAR(100) NOT NULL,
    partition_assignment   VARCHAR(250),
    rebalance_generation   INT NOT NULL DEFAULT 0,
    topic                  VARCHAR(250) NOT NULL,
    checkpoint_offset      BIGINT NOT NULL DEFAULT 0,
    updated_at             TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(group_name, topic)
);
