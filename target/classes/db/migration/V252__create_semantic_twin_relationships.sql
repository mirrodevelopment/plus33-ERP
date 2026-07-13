-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 252
-- File              : V252__create_semantic_twin_relationships.sql
-- Operation Type    : Schema Creation
-- Purpose           : create semantic twin relationships
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
-- V252: Semantic Twin Relationships
CREATE TABLE IF NOT EXISTS platform_twin_relation (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    source_instance_id  BIGINT NOT NULL,
    target_instance_id  BIGINT NOT NULL,
    relationship_type   VARCHAR(100) NOT NULL, -- DependsOn, LocatedIn, ProducedBy, Consumes, Controls, DerivedFrom, OwnedBy
    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
