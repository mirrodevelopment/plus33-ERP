-- ============================================================
-- V87__create_wms_base_tables.sql
-- PLUS33 ERP — Enterprise WMS: Layout, Yard & Cross-Dock
-- ============================================================

-- ============================================================
-- SECTION 1: WAREHOUSE LAYOUT — Zones & Locations
-- ============================================================

CREATE TABLE warehouse_zones (
    id            BIGSERIAL PRIMARY KEY,
    company_id    BIGINT        NOT NULL REFERENCES companies(id),
    warehouse_id  BIGINT        NOT NULL REFERENCES warehouses(id),
    code          VARCHAR(30)   NOT NULL,
    name          VARCHAR(100)  NOT NULL,
    zone_type     VARCHAR(30)   NOT NULL DEFAULT 'BULK',
        -- RECEIVING, BULK, PICKING, STAGING, SHIPPING, COLD, HAZMAT, QUARANTINE, RETURNS
    description   VARCHAR(255),
    active        BOOLEAN       NOT NULL DEFAULT TRUE,
    sort_sequence INT           NOT NULL DEFAULT 10,
    created_at    TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_warehouse_zone UNIQUE (warehouse_id, code)
);

CREATE TABLE warehouse_locations (
    id                BIGSERIAL PRIMARY KEY,
    company_id        BIGINT          NOT NULL REFERENCES companies(id),
    warehouse_id      BIGINT          NOT NULL REFERENCES warehouses(id),
    zone_id           BIGINT          NOT NULL REFERENCES warehouse_zones(id),
    location_code     VARCHAR(50)     NOT NULL,
    aisle             VARCHAR(10),
    rack              VARCHAR(10),
    shelf             VARCHAR(10),
    bin               VARCHAR(10),
    location_type     VARCHAR(30)     NOT NULL DEFAULT 'STANDARD',
        -- STANDARD, BULK, FLOOR, RACK, MEZZANINE, STAGING, DOCK, VIRTUAL, CROSS_DOCK
    pick_sequence     INT             NOT NULL DEFAULT 0,
    -- Physical capacity constraints
    max_weight_kg     DECIMAL(12,3),
    max_volume_m3     DECIMAL(12,6),
    max_pallets       INT             NOT NULL DEFAULT 1,
    -- ABC velocity classification
    velocity_class    VARCHAR(5)      NOT NULL DEFAULT 'C',  -- A, B, C
    -- Flags
    active            BOOLEAN         NOT NULL DEFAULT TRUE,
    is_pickable       BOOLEAN         NOT NULL DEFAULT TRUE,
    is_receivable     BOOLEAN         NOT NULL DEFAULT TRUE,
    is_mixed_lot      BOOLEAN         NOT NULL DEFAULT FALSE,
    is_mixed_sku      BOOLEAN         NOT NULL DEFAULT FALSE,
    created_at        TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_warehouse_location UNIQUE (warehouse_id, location_code)
);

CREATE INDEX idx_warehouse_locations_zone ON warehouse_locations(zone_id);
CREATE INDEX idx_warehouse_locations_warehouse ON warehouse_locations(warehouse_id, active);
CREATE INDEX idx_warehouse_locations_type ON warehouse_locations(location_type, active);

-- ============================================================
-- SECTION 2: LOCATION STOCK (Bin-Level Inventory)
-- ============================================================

CREATE TABLE location_stock (
    id                  BIGSERIAL PRIMARY KEY,
    company_id          BIGINT          NOT NULL REFERENCES companies(id),
    location_id         BIGINT          NOT NULL REFERENCES warehouse_locations(id),
    product_id          BIGINT          NOT NULL REFERENCES products(id),
    lot_number          VARCHAR(50),
    serial_number       VARCHAR(100),
    quantity            DECIMAL(18,6)   NOT NULL DEFAULT 0,
    reserved_quantity   DECIMAL(18,6)   NOT NULL DEFAULT 0,
    available_quantity  DECIMAL(18,6)   GENERATED ALWAYS AS (quantity - reserved_quantity) STORED,
    expiry_date         DATE,
    manufacture_date    DATE,
    receipt_date        DATE,
    unit_cost           DECIMAL(18,6),
    -- ABC classification snapshot
    abc_class           VARCHAR(5),
    -- Optimistic lock for concurrent picking
    version             BIGINT          NOT NULL DEFAULT 0,
    created_at          TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_location_stock UNIQUE (location_id, product_id, lot_number, serial_number)
);

CREATE INDEX idx_location_stock_product ON location_stock(company_id, product_id);
CREATE INDEX idx_location_stock_location ON location_stock(location_id);
CREATE INDEX idx_location_stock_lot ON location_stock(lot_number) WHERE lot_number IS NOT NULL;
CREATE INDEX idx_location_stock_expiry ON location_stock(expiry_date) WHERE expiry_date IS NOT NULL;

-- ============================================================
-- SECTION 3: CROSS-DOCKING
-- ============================================================

CREATE TABLE cross_dock_orders (
    id              BIGSERIAL PRIMARY KEY,
    company_id      BIGINT          NOT NULL REFERENCES companies(id),
    dock_number     VARCHAR(50)     NOT NULL,
    status          VARCHAR(30)     NOT NULL DEFAULT 'OPEN',
        -- OPEN, IN_PROGRESS, COMPLETED, CANCELLED
    source_type     VARCHAR(30)     NOT NULL DEFAULT 'ASN',   -- ASN, PO, TRANSFER
    source_ref      VARCHAR(100),
    target_type     VARCHAR(30)     NOT NULL DEFAULT 'SALES_ORDER',
    target_ref      VARCHAR(100),
    notes           TEXT,
    created_by      BIGINT          REFERENCES users(id),
    completed_at    TIMESTAMP,
    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_cross_dock_number UNIQUE (company_id, dock_number)
);

CREATE TABLE cross_dock_lines (
    id                  BIGSERIAL PRIMARY KEY,
    cross_dock_order_id BIGINT          NOT NULL REFERENCES cross_dock_orders(id),
    product_id          BIGINT          NOT NULL REFERENCES products(id),
    lot_number          VARCHAR(50),
    expected_quantity   DECIMAL(18,6)   NOT NULL,
    docked_quantity     DECIMAL(18,6)   NOT NULL DEFAULT 0,
    unit_id             BIGINT          NOT NULL REFERENCES units_of_measure(id),
    destination_location_id BIGINT      REFERENCES warehouse_locations(id),
    status              VARCHAR(30)     NOT NULL DEFAULT 'PENDING',
    created_at          TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- SECTION 4: YARD & DOCK MANAGEMENT
-- ============================================================

CREATE TABLE yard_locations (
    id              BIGSERIAL PRIMARY KEY,
    company_id      BIGINT          NOT NULL REFERENCES companies(id),
    warehouse_id    BIGINT          NOT NULL REFERENCES warehouses(id),
    location_code   VARCHAR(30)     NOT NULL,
    location_type   VARCHAR(30)     NOT NULL DEFAULT 'TRAILER', -- TRAILER, CONTAINER, STAGING
    capacity        INT             NOT NULL DEFAULT 1,
    active          BOOLEAN         NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_yard_location UNIQUE (warehouse_id, location_code)
);

CREATE TABLE dock_doors (
    id              BIGSERIAL PRIMARY KEY,
    company_id      BIGINT          NOT NULL REFERENCES companies(id),
    warehouse_id    BIGINT          NOT NULL REFERENCES warehouses(id),
    door_code       VARCHAR(30)     NOT NULL,
    door_type       VARCHAR(30)     NOT NULL DEFAULT 'INBOUND', -- INBOUND, OUTBOUND, BOTH
    zone_id         BIGINT          REFERENCES warehouse_zones(id),
    active          BOOLEAN         NOT NULL DEFAULT TRUE,
    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_dock_door UNIQUE (warehouse_id, door_code)
);

CREATE TABLE appointment_slots (
    id              BIGSERIAL PRIMARY KEY,
    company_id      BIGINT          NOT NULL REFERENCES companies(id),
    warehouse_id    BIGINT          NOT NULL REFERENCES warehouses(id),
    dock_door_id    BIGINT          NOT NULL REFERENCES dock_doors(id),
    slot_date       DATE            NOT NULL,
    slot_start      TIME            NOT NULL,
    slot_end        TIME            NOT NULL,
    slot_type       VARCHAR(30)     NOT NULL DEFAULT 'INBOUND',
    supplier_id     BIGINT          REFERENCES suppliers(id),
    customer_id     BIGINT,
    reference       VARCHAR(100),
    status          VARCHAR(30)     NOT NULL DEFAULT 'AVAILABLE',
        -- AVAILABLE, BOOKED, CHECKED_IN, COMPLETED, CANCELLED
    notes           TEXT,
    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE truck_checkins (
    id                  BIGSERIAL PRIMARY KEY,
    company_id          BIGINT          NOT NULL REFERENCES companies(id),
    warehouse_id        BIGINT          NOT NULL REFERENCES warehouses(id),
    appointment_slot_id BIGINT          REFERENCES appointment_slots(id),
    dock_door_id        BIGINT          REFERENCES dock_doors(id),
    yard_location_id    BIGINT          REFERENCES yard_locations(id),
    truck_plate         VARCHAR(50)     NOT NULL,
    driver_name         VARCHAR(100),
    carrier_name        VARCHAR(100),
    checkin_type        VARCHAR(30)     NOT NULL DEFAULT 'INBOUND',
    status              VARCHAR(30)     NOT NULL DEFAULT 'CHECKED_IN',
        -- CHECKED_IN, DOCKED, LOADING, UNLOADING, COMPLETED, DEPARTED
    arrived_at          TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    docked_at           TIMESTAMP,
    departed_at         TIMESTAMP,
    notes               TEXT,
    created_by          BIGINT          REFERENCES users(id),
    created_at          TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE dock_schedules (
    id              BIGSERIAL PRIMARY KEY,
    company_id      BIGINT          NOT NULL REFERENCES companies(id),
    dock_door_id    BIGINT          NOT NULL REFERENCES dock_doors(id),
    checkin_id      BIGINT          REFERENCES truck_checkins(id),
    scheduled_start TIMESTAMP       NOT NULL,
    scheduled_end   TIMESTAMP,
    actual_start    TIMESTAMP,
    actual_end      TIMESTAMP,
    status          VARCHAR(30)     NOT NULL DEFAULT 'SCHEDULED',
    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_appointment_slots_date ON appointment_slots(warehouse_id, slot_date);
CREATE INDEX idx_truck_checkins_warehouse ON truck_checkins(warehouse_id, status);
CREATE INDEX idx_dock_schedules_door ON dock_schedules(dock_door_id, scheduled_start);
