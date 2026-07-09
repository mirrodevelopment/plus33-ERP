-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 89
-- File              : V89__create_wms_reservation_and_ledger.sql
-- Operation Type    : Schema Creation
-- Purpose           : create wms reservation and ledger
--
-- Tables Created    : inventory_reservations, inventory_allocation_logs, inventory_movements, replenishment_plans, replenishment_tasks
-- Tables Altered    : N/A
-- Seed Data For     : N/A
-- Indexes           : idx_reservations_product, idx_reservations_source, idx_reservations_location, idx_reservations_expiry, idx_alloc_log_reservation, idx_inv_movements_product, idx_inv_movements_type, idx_inv_movements_from_loc, idx_inv_movements_to_loc, idx_inv_movements_lot, idx_replenishment_tasks_warehouse, idx_replenishment_tasks_product
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
-- ============================================================
-- V89__create_wms_reservation_and_ledger.sql
-- PLUS33 ERP — Inventory Reservations, Movement Ledger & Replenishment
-- ============================================================

-- ============================================================
-- SECTION 1: INVENTORY RESERVATIONS
-- ============================================================

CREATE TABLE inventory_reservations (
    id                  BIGSERIAL PRIMARY KEY,
    company_id          BIGINT          NOT NULL REFERENCES companies(id),
    warehouse_id        BIGINT          NOT NULL REFERENCES warehouses(id),
    product_id          BIGINT          NOT NULL REFERENCES products(id),
    location_id         BIGINT          REFERENCES warehouse_locations(id),
    lot_number          VARCHAR(50),
    serial_number       VARCHAR(100),
    reserved_quantity   DECIMAL(18,6)   NOT NULL,
    allocated_quantity  DECIMAL(18,6)   NOT NULL DEFAULT 0,
    picked_quantity     DECIMAL(18,6)   NOT NULL DEFAULT 0,
    status              VARCHAR(30)     NOT NULL DEFAULT 'CREATED',
        -- CREATED, ALLOCATED, PICKED, CONSUMED, RELEASED, EXPIRED
    source_type         VARCHAR(30)     NOT NULL,
        -- SALES_ORDER, PRODUCTION_ORDER, TRANSFER, SERVICE_ORDER, PROJECT, WAVE
    source_id           BIGINT,
    source_line_id      BIGINT,
    wave_id             BIGINT          REFERENCES waves(id),
    expiry_at           TIMESTAMP,
    idempotency_key     VARCHAR(100),
    notes               TEXT,
    created_by          BIGINT          REFERENCES users(id),
    released_by         BIGINT          REFERENCES users(id),
    released_at         TIMESTAMP,
    created_at          TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_reservation_idempotency UNIQUE (company_id, idempotency_key)
);

CREATE INDEX idx_reservations_product ON inventory_reservations(company_id, product_id, status);
CREATE INDEX idx_reservations_source ON inventory_reservations(source_type, source_id);
CREATE INDEX idx_reservations_location ON inventory_reservations(location_id, status);
CREATE INDEX idx_reservations_expiry ON inventory_reservations(expiry_at) WHERE expiry_at IS NOT NULL;

-- ============================================================
-- SECTION 2: INVENTORY ALLOCATION LOG (Audit)
-- ============================================================

CREATE TABLE inventory_allocation_logs (
    id                  BIGSERIAL PRIMARY KEY,
    reservation_id      BIGINT          NOT NULL REFERENCES inventory_reservations(id),
    company_id          BIGINT          NOT NULL REFERENCES companies(id),
    action              VARCHAR(30)     NOT NULL,
        -- RESERVE, ALLOCATE, PICK, CONSUME, RELEASE, EXPIRE, ADJUST
    quantity_before     DECIMAL(18,6)   NOT NULL,
    quantity_change     DECIMAL(18,6)   NOT NULL,
    quantity_after      DECIMAL(18,6)   NOT NULL,
    performed_by        BIGINT          REFERENCES users(id),
    notes               TEXT,
    created_at          TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_alloc_log_reservation ON inventory_allocation_logs(reservation_id);

-- ============================================================
-- SECTION 3: IMMUTABLE INVENTORY MOVEMENT LEDGER
-- ============================================================

CREATE TABLE inventory_movements (
    id                  BIGSERIAL PRIMARY KEY,
    company_id          BIGINT          NOT NULL REFERENCES companies(id),
    warehouse_id        BIGINT          NOT NULL REFERENCES warehouses(id),
    movement_type       VARCHAR(50)     NOT NULL,
        -- RECEIPT, PUT_AWAY, PICK, ISSUE, TRANSFER_OUT, TRANSFER_IN,
        -- ADJUSTMENT_INCREASE, ADJUSTMENT_DECREASE, RETURN, SCRAP,
        -- CYCLE_COUNT_ADJUST, CROSS_DOCK, REPLENISHMENT
    product_id          BIGINT          NOT NULL REFERENCES products(id),
    from_location_id    BIGINT          REFERENCES warehouse_locations(id),
    to_location_id      BIGINT          REFERENCES warehouse_locations(id),
    lot_number          VARCHAR(50),
    serial_number       VARCHAR(100),
    quantity            DECIMAL(18,6)   NOT NULL,
    unit_id             BIGINT          NOT NULL REFERENCES units_of_measure(id),
    unit_cost           DECIMAL(18,6),
    total_cost          DECIMAL(18,6),
    -- Reference to source document
    source_type         VARCHAR(30),
    source_id           BIGINT,
    source_line_id      BIGINT,
    -- Idempotency guard
    idempotency_key     VARCHAR(100),
    -- Audit trail — this record is NEVER updated or deleted
    performed_by        BIGINT          REFERENCES users(id),
    movement_at         TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    notes               TEXT,
    CONSTRAINT uk_movement_idempotency UNIQUE (company_id, idempotency_key)
);

CREATE INDEX idx_inv_movements_product ON inventory_movements(company_id, product_id, movement_at);
CREATE INDEX idx_inv_movements_type ON inventory_movements(movement_type, movement_at);
CREATE INDEX idx_inv_movements_from_loc ON inventory_movements(from_location_id);
CREATE INDEX idx_inv_movements_to_loc ON inventory_movements(to_location_id);
CREATE INDEX idx_inv_movements_lot ON inventory_movements(lot_number) WHERE lot_number IS NOT NULL;

-- ============================================================
-- SECTION 4: REPLENISHMENT ENGINE
-- ============================================================

CREATE TABLE replenishment_plans (
    id                  BIGSERIAL PRIMARY KEY,
    company_id          BIGINT          NOT NULL REFERENCES companies(id),
    warehouse_id        BIGINT          NOT NULL REFERENCES warehouses(id),
    product_id          BIGINT          NOT NULL REFERENCES products(id),
    from_zone_id        BIGINT          REFERENCES warehouse_zones(id),   -- bulk storage
    to_location_id      BIGINT          REFERENCES warehouse_locations(id), -- pick face
    strategy            VARCHAR(30)     NOT NULL DEFAULT 'MIN_MAX',
        -- MIN_MAX, KANBAN, FORWARD_PICK, DEMAND_BASED, MRP_DRIVEN
    min_quantity        DECIMAL(18,6)   NOT NULL DEFAULT 0,
    max_quantity        DECIMAL(18,6)   NOT NULL DEFAULT 0,
    replenish_quantity  DECIMAL(18,6)   NOT NULL DEFAULT 0,
    unit_id             BIGINT          NOT NULL REFERENCES units_of_measure(id),
    active              BOOLEAN         NOT NULL DEFAULT TRUE,
    created_at          TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_replenishment_plan UNIQUE (warehouse_id, product_id, to_location_id)
);

CREATE TABLE replenishment_tasks (
    id                      BIGSERIAL PRIMARY KEY,
    company_id              BIGINT          NOT NULL REFERENCES companies(id),
    replenishment_plan_id   BIGINT          REFERENCES replenishment_plans(id),
    warehouse_id            BIGINT          NOT NULL REFERENCES warehouses(id),
    product_id              BIGINT          NOT NULL REFERENCES products(id),
    from_location_id        BIGINT          REFERENCES warehouse_locations(id),
    to_location_id          BIGINT          REFERENCES warehouse_locations(id),
    lot_number              VARCHAR(50),
    quantity                DECIMAL(18,6)   NOT NULL,
    moved_quantity          DECIMAL(18,6)   NOT NULL DEFAULT 0,
    unit_id                 BIGINT          NOT NULL REFERENCES units_of_measure(id),
    trigger_reason          VARCHAR(100),   -- e.g. "Below min threshold: 2 < 10"
    status                  VARCHAR(30)     NOT NULL DEFAULT 'PENDING',
        -- PENDING, ASSIGNED, IN_PROGRESS, COMPLETED, CANCELLED
    assigned_to             BIGINT          REFERENCES users(id),
    completed_at            TIMESTAMP,
    created_at              TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at              TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_replenishment_tasks_warehouse ON replenishment_tasks(warehouse_id, status);
CREATE INDEX idx_replenishment_tasks_product ON replenishment_tasks(product_id, status);
