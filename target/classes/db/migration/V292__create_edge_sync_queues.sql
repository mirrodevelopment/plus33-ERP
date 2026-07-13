-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 292
-- File              : V292__create_edge_sync_queues.sql
-- Operation Type    : Schema Creation
-- Purpose           : create edge sync queues
--
-- Tables Created    : IF
-- Tables Altered    : N/A
-- Seed Data For     : N/A
-- Indexes           : N/A
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
-- V292: Store-and-Forward Sync Queue
CREATE TABLE IF NOT EXISTS platform_edge_sync_queue (
    id                  BIGSERIAL PRIMARY KEY,
    node_id             BIGINT NOT NULL,
    payload             TEXT NOT NULL,
    payload_hash        VARCHAR(64) NOT NULL,
    payload_type        VARCHAR(100) NOT NULL,
    priority            INT NOT NULL DEFAULT 0,
    sequence_number     BIGINT NOT NULL,
    retry_count         INT NOT NULL DEFAULT 0,
    max_retry           INT NOT NULL DEFAULT 5,
    status              VARCHAR(50) NOT NULL, -- PENDING, SENT, ACKNOWLEDGED, FAILED
    compressed          BOOLEAN NOT NULL DEFAULT FALSE,
    encrypted           BOOLEAN NOT NULL DEFAULT FALSE,
    checksum            VARCHAR(64),
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    sent_at             TIMESTAMP,
    acknowledged_at     TIMESTAMP
);
