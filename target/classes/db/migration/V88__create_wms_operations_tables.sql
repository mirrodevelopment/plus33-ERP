-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 88
-- File              : V88__create_wms_operations_tables.sql
-- Operation Type    : Schema Creation
-- Purpose           : create wms operations tables
--
-- Tables Created    : carriers, advance_shipping_notices, asn_lines, put_away_work, waves, picking_work, shipments, shipment_lines, warehouse_tasks
-- Tables Altered    : N/A
-- Seed Data For     : N/A
-- Indexes           : idx_asn_warehouse, idx_asn_supplier, idx_put_away_work_warehouse, idx_picking_work_wave, idx_picking_work_product, idx_picking_work_location, idx_shipments_warehouse, idx_shipments_carrier, idx_warehouse_tasks_warehouse, idx_warehouse_tasks_assignee, idx_warehouse_tasks_type
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
-- ============================================================
-- V88__create_wms_operations_tables.sql
-- PLUS33 ERP — Enterprise WMS: Inbound, Outbound, Tasks, Carriers
-- ============================================================

-- ============================================================
-- SECTION 1: CARRIER REGISTRY
-- ============================================================

CREATE TABLE carriers (
    id              BIGSERIAL PRIMARY KEY,
    company_id      BIGINT          NOT NULL REFERENCES companies(id),
    code            VARCHAR(30)     NOT NULL,
    name            VARCHAR(100)    NOT NULL,
    carrier_type    VARCHAR(30)     NOT NULL DEFAULT 'COURIER',
        -- COURIER, LTL, FTL, AIR, OCEAN, RAIL, PARCEL, LOCAL
    provider_key    VARCHAR(50),     -- e.g. FEDEX, DHL, UPS, LOCAL, CUSTOM
    api_endpoint    VARCHAR(500),
    api_key_ref     VARCHAR(100),    -- secret name / vault ref, never actual key
    account_number  VARCHAR(100),
    active          BOOLEAN         NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_carrier UNIQUE (company_id, code)
);

-- ============================================================
-- SECTION 2: ADVANCE SHIPPING NOTICES (ASN) — Inbound
-- ============================================================

CREATE TABLE advance_shipping_notices (
    id              BIGSERIAL PRIMARY KEY,
    company_id      BIGINT          NOT NULL REFERENCES companies(id),
    warehouse_id    BIGINT          NOT NULL REFERENCES warehouses(id),
    asn_number      VARCHAR(50)     NOT NULL,
    supplier_id     BIGINT          REFERENCES suppliers(id),
    carrier_id      BIGINT          REFERENCES carriers(id),
    status          VARCHAR(30)     NOT NULL DEFAULT 'PENDING',
        -- PENDING, IN_TRANSIT, ARRIVED, RECEIVING, PARTIALLY_RECEIVED, RECEIVED, CANCELLED
    expected_arrival    DATE,
    actual_arrival      TIMESTAMP,
    tracking_number     VARCHAR(100),
    po_reference        VARCHAR(100),
    notes               TEXT,
    dock_door_id        BIGINT          REFERENCES dock_doors(id),
    checkin_id          BIGINT          REFERENCES truck_checkins(id),
    received_by         BIGINT          REFERENCES users(id),
    created_by          BIGINT          REFERENCES users(id),
    created_at          TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_asn_number UNIQUE (company_id, asn_number)
);

CREATE TABLE asn_lines (
    id              BIGSERIAL PRIMARY KEY,
    asn_id          BIGINT          NOT NULL REFERENCES advance_shipping_notices(id),
    line_number     INT             NOT NULL,
    product_id      BIGINT          NOT NULL REFERENCES products(id),
    lot_number      VARCHAR(50),
    serial_number   VARCHAR(100),
    expected_qty    DECIMAL(18,6)   NOT NULL,
    received_qty    DECIMAL(18,6)   NOT NULL DEFAULT 0,
    unit_id         BIGINT          NOT NULL REFERENCES units_of_measure(id),
    expiry_date     DATE,
    manufacture_date DATE,
    unit_cost       DECIMAL(18,6),
    status          VARCHAR(30)     NOT NULL DEFAULT 'PENDING',
        -- PENDING, PARTIALLY_RECEIVED, RECEIVED, OVERAGE, CANCELLED
    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_asn_line UNIQUE (asn_id, line_number)
);

CREATE INDEX idx_asn_warehouse ON advance_shipping_notices(warehouse_id, status);
CREATE INDEX idx_asn_supplier ON advance_shipping_notices(supplier_id);

-- ============================================================
-- SECTION 3: PUT-AWAY WORK
-- ============================================================

CREATE TABLE put_away_work (
    id                  BIGSERIAL PRIMARY KEY,
    company_id          BIGINT          NOT NULL REFERENCES companies(id),
    warehouse_id        BIGINT          NOT NULL REFERENCES warehouses(id),
    asn_id              BIGINT          REFERENCES advance_shipping_notices(id),
    asn_line_id         BIGINT          REFERENCES asn_lines(id),
    product_id          BIGINT          NOT NULL REFERENCES products(id),
    lot_number          VARCHAR(50),
    serial_number       VARCHAR(100),
    quantity            DECIMAL(18,6)   NOT NULL,
    unit_id             BIGINT          NOT NULL REFERENCES units_of_measure(id),
    source_location_id  BIGINT          REFERENCES warehouse_locations(id),  -- staging/receiving bin
    target_location_id  BIGINT          REFERENCES warehouse_locations(id),  -- directed put-away target
    strategy_used       VARCHAR(50),     -- e.g. FEFO, CAPACITY_BASED, FIXED_BIN
    status              VARCHAR(30)      NOT NULL DEFAULT 'PENDING',
        -- PENDING, ASSIGNED, IN_PROGRESS, COMPLETED, CANCELLED
    assigned_to         BIGINT          REFERENCES users(id),
    started_at          TIMESTAMP,
    completed_at        TIMESTAMP,
    created_at          TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_put_away_work_warehouse ON put_away_work(warehouse_id, status);

-- ============================================================
-- SECTION 4: WAVES & PICKING WORK — Outbound
-- ============================================================

CREATE TABLE waves (
    id              BIGSERIAL PRIMARY KEY,
    company_id      BIGINT          NOT NULL REFERENCES companies(id),
    warehouse_id    BIGINT          NOT NULL REFERENCES warehouses(id),
    wave_number     VARCHAR(50)     NOT NULL,
    wave_type       VARCHAR(30)     NOT NULL DEFAULT 'STANDARD',
        -- STANDARD, BATCH, CLUSTER, ZONE, EMERGENCY
    status          VARCHAR(30)     NOT NULL DEFAULT 'DRAFT',
        -- DRAFT, RELEASED, PICKING, PARTIALLY_PICKED, PICKED, PACKING, SHIPPED, CLOSED, CANCELLED
    picking_strategy VARCHAR(50)    NOT NULL DEFAULT 'FEFO',
    priority        INT             NOT NULL DEFAULT 5,
    planned_date    DATE,
    notes           TEXT,
    created_by      BIGINT          REFERENCES users(id),
    released_at     TIMESTAMP,
    completed_at    TIMESTAMP,
    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_wave_number UNIQUE (company_id, wave_number)
);

CREATE TABLE picking_work (
    id                  BIGSERIAL PRIMARY KEY,
    company_id          BIGINT          NOT NULL REFERENCES companies(id),
    wave_id             BIGINT          NOT NULL REFERENCES waves(id),
    source_type         VARCHAR(30)     NOT NULL DEFAULT 'SALES_ORDER',
        -- SALES_ORDER, PRODUCTION_ORDER, TRANSFER, SERVICE_ORDER
    source_id           BIGINT,
    source_line_id      BIGINT,
    product_id          BIGINT          NOT NULL REFERENCES products(id),
    lot_number          VARCHAR(50),
    serial_number       VARCHAR(100),
    pick_quantity       DECIMAL(18,6)   NOT NULL,
    picked_quantity     DECIMAL(18,6)   NOT NULL DEFAULT 0,
    unit_id             BIGINT          NOT NULL REFERENCES units_of_measure(id),
    from_location_id    BIGINT          REFERENCES warehouse_locations(id),
    to_location_id      BIGINT          REFERENCES warehouse_locations(id),  -- staging/packing
    strategy_used       VARCHAR(50),
    status              VARCHAR(30)     NOT NULL DEFAULT 'PENDING',
        -- PENDING, ASSIGNED, IN_PROGRESS, PARTIALLY_PICKED, COMPLETED, CANCELLED, SHORTED
    assigned_to         BIGINT          REFERENCES users(id),
    -- Optimistic lock for concurrent picking guard
    version             BIGINT          NOT NULL DEFAULT 0,
    started_at          TIMESTAMP,
    completed_at        TIMESTAMP,
    created_at          TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_picking_work_wave ON picking_work(wave_id, status);
CREATE INDEX idx_picking_work_product ON picking_work(product_id);
CREATE INDEX idx_picking_work_location ON picking_work(from_location_id, status);

-- ============================================================
-- SECTION 5: SHIPMENTS
-- ============================================================

CREATE TABLE shipments (
    id              BIGSERIAL PRIMARY KEY,
    company_id      BIGINT          NOT NULL REFERENCES companies(id),
    warehouse_id    BIGINT          NOT NULL REFERENCES warehouses(id),
    shipment_number VARCHAR(50)     NOT NULL,
    wave_id         BIGINT          REFERENCES waves(id),
    carrier_id      BIGINT          REFERENCES carriers(id),
    status          VARCHAR(30)     NOT NULL DEFAULT 'DRAFT',
        -- DRAFT, PACKED, LOADED, DISPATCHED, IN_TRANSIT, DELIVERED, RETURNED, CANCELLED
    ship_method     VARCHAR(50),
    tracking_number VARCHAR(100),
    pro_number      VARCHAR(100),
    estimated_delivery  DATE,
    actual_delivery     TIMESTAMP,
    total_weight_kg     DECIMAL(12,3),
    total_volume_m3     DECIMAL(12,6),
    freight_cost        DECIMAL(18,2),
    freight_currency    VARCHAR(3) NOT NULL DEFAULT 'EUR',
    pod_reference       VARCHAR(100),  -- proof of delivery
    pod_signed_by       VARCHAR(100),
    pod_at              TIMESTAMP,
    dock_door_id        BIGINT          REFERENCES dock_doors(id),
    dispatched_by       BIGINT          REFERENCES users(id),
    created_at          TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_shipment_number UNIQUE (company_id, shipment_number)
);

CREATE TABLE shipment_lines (
    id              BIGSERIAL PRIMARY KEY,
    shipment_id     BIGINT          NOT NULL REFERENCES shipments(id),
    picking_work_id BIGINT          REFERENCES picking_work(id),
    product_id      BIGINT          NOT NULL REFERENCES products(id),
    lot_number      VARCHAR(50),
    serial_number   VARCHAR(100),
    shipped_qty     DECIMAL(18,6)   NOT NULL,
    unit_id         BIGINT          NOT NULL REFERENCES units_of_measure(id),
    source_type     VARCHAR(30),
    source_id       BIGINT,
    source_line_id  BIGINT,
    package_number  VARCHAR(50),
    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_shipments_warehouse ON shipments(warehouse_id, status);
CREATE INDEX idx_shipments_carrier ON shipments(carrier_id);

-- ============================================================
-- SECTION 6: CENTRALIZED WAREHOUSE TASK ENGINE
-- ============================================================

CREATE TABLE warehouse_tasks (
    id              BIGSERIAL PRIMARY KEY,
    company_id      BIGINT          NOT NULL REFERENCES companies(id),
    warehouse_id    BIGINT          NOT NULL REFERENCES warehouses(id),
    task_type       VARCHAR(30)     NOT NULL,
        -- PUT_AWAY, PICKING, PACKING, LOADING, CYCLE_COUNT, TRANSFER, REPLENISHMENT, RECEIVING
    task_status     VARCHAR(30)     NOT NULL DEFAULT 'PENDING',
        -- PENDING, ASSIGNED, IN_PROGRESS, PAUSED, COMPLETED, CANCELLED
    priority        INT             NOT NULL DEFAULT 5,
    ref_type        VARCHAR(30),     -- PUT_AWAY_WORK, PICKING_WORK, CYCLE_COUNT_TASK, etc.
    ref_id          BIGINT,
    assigned_to     BIGINT          REFERENCES users(id),
    assigned_at     TIMESTAMP,
    started_at      TIMESTAMP,
    paused_at       TIMESTAMP,
    completed_at    TIMESTAMP,
    cancelled_at    TIMESTAMP,
    notes           TEXT,
    created_by      BIGINT          REFERENCES users(id),
    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_warehouse_tasks_warehouse ON warehouse_tasks(warehouse_id, task_status);
CREATE INDEX idx_warehouse_tasks_assignee ON warehouse_tasks(assigned_to, task_status);
CREATE INDEX idx_warehouse_tasks_type ON warehouse_tasks(task_type, task_status);
