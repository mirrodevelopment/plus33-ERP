-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 9
-- File              : V9__create_user_assignments.sql
-- Operation Type    : Schema Creation
-- Purpose           : create user assignments
--
-- Tables Created    : user_regions, user_warehouses, user_stores
-- Tables Altered    : N/A
-- Seed Data For     : N/A
-- Indexes           : idx_user_regions_user, idx_user_warehouses_user, idx_user_stores_user
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
-- ============================================================
-- V9__create_user_assignments.sql
-- PLUS33 ERP — User organizational unit assignments
-- ============================================================

CREATE TABLE user_regions (
    user_id BIGINT NOT NULL,
    region_id BIGINT NOT NULL,
    assigned_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (user_id, region_id),

    CONSTRAINT fk_user_regions_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_user_regions_region
        FOREIGN KEY (region_id)
        REFERENCES regions(id)
        ON DELETE CASCADE
);

CREATE TABLE user_warehouses (
    user_id BIGINT NOT NULL,
    warehouse_id BIGINT NOT NULL,
    assigned_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (user_id, warehouse_id),

    CONSTRAINT fk_user_warehouses_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_user_warehouses_warehouse
        FOREIGN KEY (warehouse_id)
        REFERENCES warehouses(id)
        ON DELETE CASCADE
);

CREATE TABLE user_stores (
    user_id BIGINT NOT NULL,
    store_id BIGINT NOT NULL,
    assigned_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (user_id, store_id),

    CONSTRAINT fk_user_stores_user
        FOREIGN KEY (user_id)
        REFERENCES users(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_user_stores_store
        FOREIGN KEY (store_id)
        REFERENCES stores(id)
        ON DELETE CASCADE
);

CREATE INDEX idx_user_regions_user ON user_regions(user_id);
CREATE INDEX idx_user_warehouses_user ON user_warehouses(user_id);
CREATE INDEX idx_user_stores_user ON user_stores(user_id);
