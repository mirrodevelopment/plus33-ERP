-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 85
-- File              : V85__create_manufacturing_enterprise_module.sql
-- Operation Type    : Schema Creation
-- Purpose           : create manufacturing enterprise module
--
-- Tables Created    : bom_headers, bom_lines, bom_substitutes, engineering_change_orders, engineering_change_lines, work_centers, machines, labor_groups, routing_headers, routing_operations, manufacturing_calendars, manufacturing_calendar_shifts, manufacturing_calendar_exceptions, mrp_runs, planned_orders, mrp_pegging_links, capacity_plans, production_orders, production_order_operations, production_material_issues, production_confirmations, production_scrap, production_rework, production_costs, cost_roll_up_snapshots, manufacturing_serial_genealogy, manufacturing_batch_genealogy, quality_inspections, manufacturing_events
-- Tables Altered    : N/A
-- Seed Data For     : N/A
-- Indexes           : idx_bom_headers_company_product, idx_bom_headers_status, idx_bom_headers_effective, idx_bom_lines_header, idx_bom_lines_component, idx_eco_company_status, idx_routing_headers_product, idx_mrp_runs_company_status, idx_planned_orders_company_product, idx_planned_orders_required_date, idx_mrp_pegging_links_run, idx_capacity_plans_work_center, idx_production_orders_company_status, idx_production_orders_product, idx_production_orders_planned_start, idx_production_issues_order, idx_production_confirmations_order, idx_production_costs_order, idx_manufacturing_serial_genealogy_serial, idx_manufacturing_batch_genealogy_batch, idx_quality_inspections_order, idx_quality_inspections_status, idx_manufacturing_events_type, idx_manufacturing_events_reference, idx_manufacturing_events_occurred, idx_mv_production_dashboard, idx_mv_wip_valuation, idx_mv_oee_summary, idx_mv_manufacturing_variances
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
-- ============================================================
-- V85__create_manufacturing_enterprise_module.sql
-- PLUS33 ERP — Enterprise Manufacturing (MRP II), MES & Cost Accounting
-- ============================================================

-- ============================================================
-- SECTION 1: PRODUCT ENGINEERING — BOM Headers, Lines, Substitutes
-- ============================================================

CREATE TABLE bom_headers (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL REFERENCES companies(id),
    product_id BIGINT NOT NULL REFERENCES products(id),
    bom_number VARCHAR(50) NOT NULL,
    description VARCHAR(255),
    bom_type VARCHAR(30) NOT NULL DEFAULT 'MANUFACTURING', -- MANUFACTURING, ENGINEERING, PHANTOM, SALES
    revision VARCHAR(20) NOT NULL DEFAULT '00',
    status VARCHAR(30) NOT NULL DEFAULT 'DRAFT',           -- DRAFT, PENDING_APPROVAL, ACTIVE, SUPERSEDED, OBSOLETE
    effective_from DATE NOT NULL,
    effective_to DATE,
    base_quantity DECIMAL(18,6) NOT NULL DEFAULT 1.00,
    base_unit_id BIGINT NOT NULL REFERENCES units_of_measure(id),
    approved_by BIGINT REFERENCES users(id),
    approved_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_bom_header UNIQUE (company_id, product_id, bom_number, revision)
);

CREATE TABLE bom_lines (
    id BIGSERIAL PRIMARY KEY,
    bom_header_id BIGINT NOT NULL REFERENCES bom_headers(id),
    line_number INT NOT NULL,
    component_product_id BIGINT NOT NULL REFERENCES products(id),
    quantity DECIMAL(18,6) NOT NULL,
    unit_id BIGINT NOT NULL REFERENCES units_of_measure(id),
    component_type VARCHAR(30) NOT NULL DEFAULT 'MATERIAL', -- MATERIAL, PHANTOM, BY_PRODUCT, CO_PRODUCT, REFERENCE
    scrap_percentage DECIMAL(7,4) NOT NULL DEFAULT 0.00,
    yield_percentage DECIMAL(7,4) NOT NULL DEFAULT 100.00,
    backflush BOOLEAN NOT NULL DEFAULT FALSE,
    reference_designator VARCHAR(100),
    effective_from DATE,
    effective_to DATE,
    warehouse_id BIGINT REFERENCES warehouses(id),
    issue_method VARCHAR(30) NOT NULL DEFAULT 'MANUAL',     -- MANUAL, BACKFLUSH, PRE_ISSUE
    sort_sequence INT NOT NULL DEFAULT 10,
    notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_bom_line UNIQUE (bom_header_id, line_number)
);

CREATE TABLE bom_substitutes (
    id BIGSERIAL PRIMARY KEY,
    bom_line_id BIGINT NOT NULL REFERENCES bom_lines(id),
    substitute_product_id BIGINT NOT NULL REFERENCES products(id),
    substitute_quantity DECIMAL(18,6) NOT NULL,
    unit_id BIGINT NOT NULL REFERENCES units_of_measure(id),
    priority INT NOT NULL DEFAULT 1,
    effective_from DATE,
    effective_to DATE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- SECTION 2: ENGINEERING CHANGE MANAGEMENT (ECM / ECO)
-- ============================================================

CREATE TABLE engineering_change_orders (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL REFERENCES companies(id),
    eco_number VARCHAR(50) NOT NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    reason VARCHAR(255),
    status VARCHAR(30) NOT NULL DEFAULT 'DRAFT',           -- DRAFT, SUBMITTED, UNDER_REVIEW, APPROVED, IMPLEMENTED, CANCELLED
    priority VARCHAR(20) NOT NULL DEFAULT 'NORMAL',        -- LOW, NORMAL, HIGH, CRITICAL, SAFETY
    effective_date DATE,
    requested_by BIGINT NOT NULL REFERENCES users(id),
    reviewed_by BIGINT REFERENCES users(id),
    approved_by BIGINT REFERENCES users(id),
    reviewed_at TIMESTAMP,
    approved_at TIMESTAMP,
    implemented_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_eco_number UNIQUE (company_id, eco_number)
);

CREATE TABLE engineering_change_lines (
    id BIGSERIAL PRIMARY KEY,
    eco_id BIGINT NOT NULL REFERENCES engineering_change_orders(id),
    change_type VARCHAR(30) NOT NULL,                      -- BOM_ADD, BOM_REMOVE, BOM_MODIFY, ROUTING_ADD, ROUTING_REMOVE, ROUTING_MODIFY
    reference_type VARCHAR(30) NOT NULL,                   -- BOM_HEADER, BOM_LINE, ROUTING_HEADER, ROUTING_OPERATION
    reference_id BIGINT NOT NULL,
    before_snapshot JSONB,
    after_snapshot JSONB,
    effective_from DATE,
    sort_sequence INT NOT NULL DEFAULT 10,
    notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- SECTION 3: MANUFACTURING ENGINEERING — Work Centers, Machines, Routings
-- ============================================================

CREATE TABLE work_centers (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL REFERENCES companies(id),
    code VARCHAR(50) NOT NULL,
    name VARCHAR(150) NOT NULL,
    description VARCHAR(255),
    department VARCHAR(100),
    work_center_type VARCHAR(30) NOT NULL DEFAULT 'MACHINE', -- MACHINE, LABOR, MIXED, SUBCONTRACT
    machine_capacity DECIMAL(10,2) NOT NULL DEFAULT 1.00,    -- number of parallel machines
    labor_capacity DECIMAL(10,2) NOT NULL DEFAULT 1.00,      -- number of parallel workers
    hourly_machine_rate DECIMAL(18,4) NOT NULL DEFAULT 0.00,
    hourly_labor_rate DECIMAL(18,4) NOT NULL DEFAULT 0.00,
    overhead_rate DECIMAL(18,4) NOT NULL DEFAULT 0.00,
    overhead_rate_type VARCHAR(30) NOT NULL DEFAULT 'MACHINE_HOUR', -- MACHINE_HOUR, LABOR_HOUR, DIRECT_LABOR_COST
    queue_time_hours DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    move_time_hours DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    efficiency_factor DECIMAL(7,4) NOT NULL DEFAULT 100.00,
    gl_account_id BIGINT REFERENCES chart_of_accounts(id),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_work_center_code UNIQUE (company_id, code)
);

CREATE TABLE machines (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL REFERENCES companies(id),
    work_center_id BIGINT NOT NULL REFERENCES work_centers(id),
    asset_id BIGINT REFERENCES fixed_assets(id),
    code VARCHAR(50) NOT NULL,
    name VARCHAR(150) NOT NULL,
    description VARCHAR(255),
    machine_type VARCHAR(50),
    max_speed_units DECIMAL(10,2),
    cycle_time_seconds DECIMAL(10,2),
    setup_time_minutes DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    hourly_run_cost DECIMAL(18,4) NOT NULL DEFAULT 0.00,
    hourly_idle_cost DECIMAL(18,4) NOT NULL DEFAULT 0.00,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_machine_code UNIQUE (company_id, code)
);

CREATE TABLE labor_groups (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL REFERENCES companies(id),
    code VARCHAR(50) NOT NULL,
    name VARCHAR(100) NOT NULL,
    skill_level VARCHAR(30),
    hourly_rate DECIMAL(18,4) NOT NULL DEFAULT 0.00,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_labor_group_code UNIQUE (company_id, code)
);

CREATE TABLE routing_headers (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL REFERENCES companies(id),
    product_id BIGINT NOT NULL REFERENCES products(id),
    routing_number VARCHAR(50) NOT NULL,
    description VARCHAR(255),
    revision VARCHAR(20) NOT NULL DEFAULT '00',
    status VARCHAR(30) NOT NULL DEFAULT 'DRAFT',           -- DRAFT, ACTIVE, SUPERSEDED, OBSOLETE
    effective_from DATE NOT NULL,
    effective_to DATE,
    lead_time_hours DECIMAL(10,2),
    approved_by BIGINT REFERENCES users(id),
    approved_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_routing_header UNIQUE (company_id, product_id, routing_number, revision)
);

CREATE TABLE routing_operations (
    id BIGSERIAL PRIMARY KEY,
    routing_header_id BIGINT NOT NULL REFERENCES routing_headers(id),
    operation_number INT NOT NULL,
    operation_code VARCHAR(50) NOT NULL,
    description VARCHAR(255),
    work_center_id BIGINT NOT NULL REFERENCES work_centers(id),
    machine_id BIGINT REFERENCES machines(id),
    labor_group_id BIGINT REFERENCES labor_groups(id),
    setup_time_hours DECIMAL(10,4) NOT NULL DEFAULT 0.00,
    run_time_per_unit_hours DECIMAL(10,6) NOT NULL DEFAULT 0.00,
    queue_time_hours DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    move_time_hours DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    transfer_batch_size DECIMAL(10,2),
    tool_requirements TEXT,
    skill_requirements TEXT,
    instruction_notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_routing_operation UNIQUE (routing_header_id, operation_number)
);

-- ============================================================
-- SECTION 4: MANUFACTURING RESOURCE CALENDARS
-- ============================================================

CREATE TABLE manufacturing_calendars (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL REFERENCES companies(id),
    calendar_type VARCHAR(30) NOT NULL,                     -- PLANT, WORK_CENTER, MACHINE, SHIFT, HOLIDAY, OVERTIME
    reference_type VARCHAR(30),                             -- WORK_CENTER, MACHINE
    reference_id BIGINT,
    code VARCHAR(50) NOT NULL,
    name VARCHAR(100) NOT NULL,
    timezone VARCHAR(60) NOT NULL DEFAULT 'UTC',
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_manufacturing_calendar UNIQUE (company_id, calendar_type, code)
);

CREATE TABLE manufacturing_calendar_shifts (
    id BIGSERIAL PRIMARY KEY,
    calendar_id BIGINT NOT NULL REFERENCES manufacturing_calendars(id),
    day_of_week INT NOT NULL,                               -- 1=Monday .. 7=Sunday
    shift_name VARCHAR(50) NOT NULL,
    start_time TIME NOT NULL,
    end_time TIME NOT NULL,
    break_minutes INT NOT NULL DEFAULT 0,
    available_hours DECIMAL(6,2) NOT NULL DEFAULT 8.00,
    shift_type VARCHAR(20) NOT NULL DEFAULT 'REGULAR',      -- REGULAR, OVERTIME, SHUTDOWN
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE manufacturing_calendar_exceptions (
    id BIGSERIAL PRIMARY KEY,
    calendar_id BIGINT NOT NULL REFERENCES manufacturing_calendars(id),
    exception_date DATE NOT NULL,
    exception_type VARCHAR(30) NOT NULL,                    -- HOLIDAY, MAINTENANCE_WINDOW, OVERTIME, SHUTDOWN
    available_hours DECIMAL(6,2) NOT NULL DEFAULT 0.00,
    description VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- SECTION 5: MRP II — MRP Runs, Planned Orders, Pegging, Capacity
-- ============================================================

CREATE TABLE mrp_runs (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL REFERENCES companies(id),
    run_number VARCHAR(50) NOT NULL,
    run_type VARCHAR(30) NOT NULL DEFAULT 'REGENERATIVE',   -- REGENERATIVE, NET_CHANGE, NET_CHANGE_PLANNED
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',          -- PENDING, RUNNING, COMPLETED, FAILED
    planning_horizon_days INT NOT NULL DEFAULT 90,
    include_forecasts BOOLEAN NOT NULL DEFAULT TRUE,
    include_sales_orders BOOLEAN NOT NULL DEFAULT TRUE,
    include_safety_stock BOOLEAN NOT NULL DEFAULT TRUE,
    planned_orders_generated INT NOT NULL DEFAULT 0,
    purchase_reqs_generated INT NOT NULL DEFAULT 0,
    started_at TIMESTAMP,
    completed_at TIMESTAMP,
    error_message TEXT,
    run_by BIGINT NOT NULL REFERENCES users(id),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_mrp_run_number UNIQUE (company_id, run_number)
);

CREATE TABLE planned_orders (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL REFERENCES companies(id),
    mrp_run_id BIGINT NOT NULL REFERENCES mrp_runs(id),
    product_id BIGINT NOT NULL REFERENCES products(id),
    order_type VARCHAR(30) NOT NULL,                        -- PRODUCTION, PURCHASE, TRANSFER
    status VARCHAR(30) NOT NULL DEFAULT 'OPEN',             -- OPEN, FIRMED, RELEASED, CANCELLED
    required_quantity DECIMAL(18,6) NOT NULL,
    unit_id BIGINT NOT NULL REFERENCES units_of_measure(id),
    required_date DATE NOT NULL,
    planned_start_date DATE NOT NULL,
    planned_end_date DATE NOT NULL,
    demand_source VARCHAR(50),                              -- SALES_ORDER, FORECAST, SAFETY_STOCK, DEPENDENT
    demand_source_id BIGINT,
    bom_header_id BIGINT REFERENCES bom_headers(id),
    routing_header_id BIGINT REFERENCES routing_headers(id),
    firmed BOOLEAN NOT NULL DEFAULT FALSE,
    released_production_order_id BIGINT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);


CREATE TABLE mrp_pegging_links (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL REFERENCES companies(id),
    mrp_run_id BIGINT NOT NULL REFERENCES mrp_runs(id),
    supply_type VARCHAR(30) NOT NULL,                       -- PLANNED_PRODUCTION, PLANNED_PURCHASE, PRODUCTION_ORDER, PURCHASE_ORDER, STOCK
    supply_id BIGINT NOT NULL,
    demand_type VARCHAR(30) NOT NULL,                       -- SALES_ORDER, FORECAST, PRODUCTION_ORDER, SAFETY_STOCK
    demand_id BIGINT NOT NULL,
    pegged_quantity DECIMAL(18,6) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE capacity_plans (
    id BIGSERIAL PRIMARY KEY,
    mrp_run_id BIGINT NOT NULL REFERENCES mrp_runs(id),
    work_center_id BIGINT NOT NULL REFERENCES work_centers(id),
    planning_date DATE NOT NULL,
    available_hours DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    required_hours DECIMAL(10,2) NOT NULL DEFAULT 0.00,
    overloaded BOOLEAN NOT NULL DEFAULT FALSE,
    machine_utilization_pct DECIMAL(7,4),
    labor_utilization_pct DECIMAL(7,4),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- SECTION 6: MES — Production Orders, Operations, Full State Machine
-- ============================================================

CREATE TABLE production_orders (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL REFERENCES companies(id),
    order_number VARCHAR(50) NOT NULL,
    product_id BIGINT NOT NULL REFERENCES products(id),
    bom_header_id BIGINT NOT NULL REFERENCES bom_headers(id),
    routing_header_id BIGINT REFERENCES routing_headers(id),
    planned_order_id BIGINT REFERENCES planned_orders(id),
    status VARCHAR(30) NOT NULL DEFAULT 'DRAFT',
    -- Full State Machine:
    -- DRAFT → PLANNED → RELEASED → MATERIAL_ALLOCATED → IN_PROGRESS
    -- → PARTIALLY_COMPLETED → COMPLETED → QUALITY_PENDING → CLOSED
    -- Exception: HOLD, CANCELLED, SCRAPPED, REWORK, REVERSED
    order_type VARCHAR(30) NOT NULL DEFAULT 'STANDARD',     -- STANDARD, REWORK, OFF_CYCLE, CORRECTION
    target_quantity DECIMAL(18,6) NOT NULL,
    completed_quantity DECIMAL(18,6) NOT NULL DEFAULT 0.00,
    scrapped_quantity DECIMAL(18,6) NOT NULL DEFAULT 0.00,
    unit_id BIGINT NOT NULL REFERENCES units_of_measure(id),
    warehouse_id BIGINT REFERENCES warehouses(id),
    planned_start_date DATE NOT NULL,
    planned_end_date DATE NOT NULL,
    actual_start_date DATE,
    actual_end_date DATE,
    priority INT NOT NULL DEFAULT 5,
    standard_cost DECIMAL(22,6) NOT NULL DEFAULT 0.00,
    actual_cost DECIMAL(22,6) NOT NULL DEFAULT 0.00,
    costing_method VARCHAR(30) NOT NULL DEFAULT 'STANDARD', -- STANDARD, ACTUAL, FIFO, WEIGHTED_AVERAGE
    released_by BIGINT REFERENCES users(id),
    released_at TIMESTAMP,
    closed_by BIGINT REFERENCES users(id),
    closed_at TIMESTAMP,
    notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_production_order_number UNIQUE (company_id, order_number)
);

CREATE TABLE production_order_operations (
    id BIGSERIAL PRIMARY KEY,
    production_order_id BIGINT NOT NULL REFERENCES production_orders(id),
    routing_operation_id BIGINT REFERENCES routing_operations(id),
    operation_number INT NOT NULL,
    operation_code VARCHAR(50) NOT NULL,
    description VARCHAR(255),
    work_center_id BIGINT NOT NULL REFERENCES work_centers(id),
    machine_id BIGINT REFERENCES machines(id),
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',          -- PENDING, QUEUED, SETUP, IN_PROGRESS, COMPLETED, SKIPPED, SCRAPPED
    planned_quantity DECIMAL(18,6) NOT NULL,
    completed_quantity DECIMAL(18,6) NOT NULL DEFAULT 0.00,
    scrapped_quantity DECIMAL(18,6) NOT NULL DEFAULT 0.00,
    planned_setup_hours DECIMAL(10,4) NOT NULL DEFAULT 0.00,
    actual_setup_hours DECIMAL(10,4) NOT NULL DEFAULT 0.00,
    planned_run_hours DECIMAL(10,4) NOT NULL DEFAULT 0.00,
    actual_run_hours DECIMAL(10,4) NOT NULL DEFAULT 0.00,
    planned_start_datetime TIMESTAMP,
    planned_end_datetime TIMESTAMP,
    actual_start_datetime TIMESTAMP,
    actual_end_datetime TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_prod_order_operation UNIQUE (production_order_id, operation_number)
);

CREATE TABLE production_material_issues (
    id BIGSERIAL PRIMARY KEY,
    production_order_id BIGINT NOT NULL REFERENCES production_orders(id),
    bom_line_id BIGINT REFERENCES bom_lines(id),
    component_product_id BIGINT NOT NULL REFERENCES products(id),
    issue_type VARCHAR(30) NOT NULL DEFAULT 'ISSUE',        -- ISSUE, RETURN, SCRAP, BACKFLUSH
    issue_number VARCHAR(50) NOT NULL,
    issued_quantity DECIMAL(18,6) NOT NULL,
    unit_id BIGINT NOT NULL REFERENCES units_of_measure(id),
    lot_number VARCHAR(100),
    serial_number VARCHAR(100),
    warehouse_id BIGINT NOT NULL REFERENCES warehouses(id),
    stock_movement_id BIGINT,
    unit_cost DECIMAL(22,6) NOT NULL DEFAULT 0.00,
    total_cost DECIMAL(22,6) NOT NULL DEFAULT 0.00,
    journal_entry_id BIGINT REFERENCES journal_entries(id),
    issued_by BIGINT NOT NULL REFERENCES users(id),
    issued_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_production_issue_number UNIQUE (production_order_id, issue_number)
        DEFERRABLE INITIALLY DEFERRED
);

CREATE TABLE production_confirmations (
    id BIGSERIAL PRIMARY KEY,
    production_order_id BIGINT NOT NULL REFERENCES production_orders(id),
    production_order_operation_id BIGINT REFERENCES production_order_operations(id),
    confirmation_number VARCHAR(50) NOT NULL,
    confirmation_type VARCHAR(30) NOT NULL DEFAULT 'PARTIAL', -- PARTIAL, FINAL, REVERSAL
    confirmed_quantity DECIMAL(18,6) NOT NULL,
    scrapped_quantity DECIMAL(18,6) NOT NULL DEFAULT 0.00,
    rework_quantity DECIMAL(18,6) NOT NULL DEFAULT 0.00,
    unit_id BIGINT NOT NULL REFERENCES units_of_measure(id),
    actual_labor_hours DECIMAL(10,4) NOT NULL DEFAULT 0.00,
    actual_machine_hours DECIMAL(10,4) NOT NULL DEFAULT 0.00,
    labor_group_id BIGINT REFERENCES labor_groups(id),
    machine_id BIGINT REFERENCES machines(id),
    work_center_id BIGINT NOT NULL REFERENCES work_centers(id),
    finished_goods_received BOOLEAN NOT NULL DEFAULT FALSE,
    fg_warehouse_id BIGINT REFERENCES warehouses(id),
    lot_number VARCHAR(100),
    serial_number VARCHAR(100),
    journal_entry_id BIGINT REFERENCES journal_entries(id),
    confirmed_by BIGINT NOT NULL REFERENCES users(id),
    confirmed_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE production_scrap (
    id BIGSERIAL PRIMARY KEY,
    production_order_id BIGINT NOT NULL REFERENCES production_orders(id),
    production_order_operation_id BIGINT REFERENCES production_order_operations(id),
    scrap_number VARCHAR(50) NOT NULL,
    product_id BIGINT NOT NULL REFERENCES products(id),
    scrap_quantity DECIMAL(18,6) NOT NULL,
    unit_id BIGINT NOT NULL REFERENCES units_of_measure(id),
    defect_code VARCHAR(50),
    defect_description TEXT,
    scrap_disposition VARCHAR(30) NOT NULL DEFAULT 'DISCARD', -- DISCARD, REWORK, SALVAGE, RETURN_TO_SUPPLIER
    unit_cost DECIMAL(22,6) NOT NULL DEFAULT 0.00,
    total_scrap_cost DECIMAL(22,6) NOT NULL DEFAULT 0.00,
    journal_entry_id BIGINT REFERENCES journal_entries(id),
    recorded_by BIGINT NOT NULL REFERENCES users(id),
    recorded_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE production_rework (
    id BIGSERIAL PRIMARY KEY,
    original_production_order_id BIGINT NOT NULL REFERENCES production_orders(id),
    rework_production_order_id BIGINT REFERENCES production_orders(id),
    rework_number VARCHAR(50) NOT NULL,
    product_id BIGINT NOT NULL REFERENCES products(id),
    rework_quantity DECIMAL(18,6) NOT NULL,
    unit_id BIGINT NOT NULL REFERENCES units_of_measure(id),
    rework_reason TEXT,
    rework_cost DECIMAL(22,6) NOT NULL DEFAULT 0.00,
    status VARCHAR(30) NOT NULL DEFAULT 'OPEN',             -- OPEN, IN_PROGRESS, COMPLETED, SCRAPPED
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- SECTION 7: MANUFACTURING COST ACCOUNTING & WIP
-- ============================================================

CREATE TABLE production_costs (
    id BIGSERIAL PRIMARY KEY,
    production_order_id BIGINT NOT NULL UNIQUE REFERENCES production_orders(id),
    costing_method VARCHAR(30) NOT NULL DEFAULT 'STANDARD',
    -- Standard Costs
    standard_material_cost DECIMAL(22,6) NOT NULL DEFAULT 0.00,
    standard_labor_cost DECIMAL(22,6) NOT NULL DEFAULT 0.00,
    standard_machine_cost DECIMAL(22,6) NOT NULL DEFAULT 0.00,
    standard_overhead_cost DECIMAL(22,6) NOT NULL DEFAULT 0.00,
    standard_subcontract_cost DECIMAL(22,6) NOT NULL DEFAULT 0.00,
    standard_total_cost DECIMAL(22,6) NOT NULL DEFAULT 0.00,
    -- Actual Costs
    actual_material_cost DECIMAL(22,6) NOT NULL DEFAULT 0.00,
    actual_labor_cost DECIMAL(22,6) NOT NULL DEFAULT 0.00,
    actual_machine_cost DECIMAL(22,6) NOT NULL DEFAULT 0.00,
    actual_overhead_cost DECIMAL(22,6) NOT NULL DEFAULT 0.00,
    actual_subcontract_cost DECIMAL(22,6) NOT NULL DEFAULT 0.00,
    actual_total_cost DECIMAL(22,6) NOT NULL DEFAULT 0.00,
    -- By-product & Co-product credits
    byproduct_credit DECIMAL(22,6) NOT NULL DEFAULT 0.00,
    coproduct_allocation DECIMAL(22,6) NOT NULL DEFAULT 0.00,
    -- Variances
    material_variance DECIMAL(22,6) NOT NULL DEFAULT 0.00,
    labor_variance DECIMAL(22,6) NOT NULL DEFAULT 0.00,
    machine_variance DECIMAL(22,6) NOT NULL DEFAULT 0.00,
    overhead_variance DECIMAL(22,6) NOT NULL DEFAULT 0.00,
    total_variance DECIMAL(22,6) NOT NULL DEFAULT 0.00,
    -- WIP balance
    wip_balance DECIMAL(22,6) NOT NULL DEFAULT 0.00,
    status VARCHAR(30) NOT NULL DEFAULT 'IN_PROGRESS',      -- IN_PROGRESS, FINALIZED, REVERSED
    finalized_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE cost_roll_up_snapshots (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL REFERENCES companies(id),
    product_id BIGINT NOT NULL REFERENCES products(id),
    bom_header_id BIGINT NOT NULL REFERENCES bom_headers(id),
    snapshot_date DATE NOT NULL,
    rolled_material_cost DECIMAL(22,6) NOT NULL DEFAULT 0.00,
    rolled_labor_cost DECIMAL(22,6) NOT NULL DEFAULT 0.00,
    rolled_machine_cost DECIMAL(22,6) NOT NULL DEFAULT 0.00,
    rolled_overhead_cost DECIMAL(22,6) NOT NULL DEFAULT 0.00,
    rolled_subcontract_cost DECIMAL(22,6) NOT NULL DEFAULT 0.00,
    rolled_total_cost DECIMAL(22,6) NOT NULL DEFAULT 0.00,
    run_by BIGINT REFERENCES users(id),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- SECTION 8: SERIAL & LOT TRACEABILITY GENEALOGY
-- ============================================================

CREATE TABLE manufacturing_serial_genealogy (
    id BIGSERIAL PRIMARY KEY,
    production_order_id BIGINT NOT NULL REFERENCES production_orders(id),
    product_id BIGINT NOT NULL REFERENCES products(id),
    serial_number VARCHAR(100) NOT NULL,
    parent_serial_number VARCHAR(100),
    parent_product_id BIGINT REFERENCES products(id),
    genealogy_type VARCHAR(20) NOT NULL DEFAULT 'CHILD',    -- PARENT, CHILD, SIBLING
    produced_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    lot_number VARCHAR(100),
    notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE manufacturing_batch_genealogy (
    id BIGSERIAL PRIMARY KEY,
    production_order_id BIGINT NOT NULL REFERENCES production_orders(id),
    product_id BIGINT NOT NULL REFERENCES products(id),
    batch_number VARCHAR(100) NOT NULL,
    parent_batch_number VARCHAR(100),
    parent_product_id BIGINT REFERENCES products(id),
    quantity DECIMAL(18,6) NOT NULL,
    unit_id BIGINT NOT NULL REFERENCES units_of_measure(id),
    genealogy_type VARCHAR(20) NOT NULL DEFAULT 'OUTPUT',   -- INPUT, OUTPUT, BY_PRODUCT
    produced_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expiry_date DATE,
    recall_status VARCHAR(20) NOT NULL DEFAULT 'CLEAR',     -- CLEAR, UNDER_REVIEW, RECALLED
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- SECTION 9: QUALITY INSPECTIONS
-- ============================================================

CREATE TABLE quality_inspections (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL REFERENCES companies(id),
    production_order_id BIGINT REFERENCES production_orders(id),
    inspection_number VARCHAR(50) NOT NULL,
    inspection_type VARCHAR(30) NOT NULL,                   -- INCOMING, IN_PROCESS, FINAL, RECEIVING
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',          -- PENDING, IN_PROGRESS, PASSED, FAILED, CONDITIONALLY_PASSED, SCRAPPED
    product_id BIGINT NOT NULL REFERENCES products(id),
    lot_number VARCHAR(100),
    serial_number VARCHAR(100),
    inspected_quantity DECIMAL(18,6) NOT NULL,
    passed_quantity DECIMAL(18,6) NOT NULL DEFAULT 0.00,
    failed_quantity DECIMAL(18,6) NOT NULL DEFAULT 0.00,
    sample_size DECIMAL(18,6),
    sampling_plan VARCHAR(100),
    disposition VARCHAR(30),                                -- ACCEPT, REJECT, REWORK, CONDITIONAL_ACCEPT
    hold_production BOOLEAN NOT NULL DEFAULT FALSE,
    non_conformance_report TEXT,
    corrective_action TEXT,
    inspected_by BIGINT REFERENCES users(id),
    approved_by BIGINT REFERENCES users(id),
    inspected_at TIMESTAMP,
    approved_at TIMESTAMP,
    notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_quality_inspection_number UNIQUE (company_id, inspection_number)
);


-- ============================================================
-- SECTION 10: MANUFACTURING EVENTS — Immutable Audit Trail
-- ============================================================

CREATE TABLE manufacturing_events (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL REFERENCES companies(id),
    event_type VARCHAR(80) NOT NULL,
    -- PRODUCTION_ORDER_CREATED, PRODUCTION_ORDER_RELEASED, MATERIAL_ISSUED,
    -- OPERATION_STARTED, OPERATION_COMPLETED, SCRAP_RECORDED, REWORK_INITIATED,
    -- PRODUCTION_COMPLETED, WIP_UPDATED, COST_CALCULATED, MRP_RUN_COMPLETED,
    -- QUALITY_INSPECTION_COMPLETED, ECO_APPROVED, ECO_IMPLEMENTED
    reference_type VARCHAR(30) NOT NULL,
    reference_id BIGINT NOT NULL,
    event_data JSONB,
    triggered_by BIGINT REFERENCES users(id),
    occurred_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- ============================================================
-- SECTION 11: PERFORMANCE INDEXES
-- ============================================================

CREATE INDEX idx_bom_headers_company_product ON bom_headers(company_id, product_id);
CREATE INDEX idx_bom_headers_status ON bom_headers(status);
CREATE INDEX idx_bom_headers_effective ON bom_headers(effective_from, effective_to);
CREATE INDEX idx_bom_lines_header ON bom_lines(bom_header_id);
CREATE INDEX idx_bom_lines_component ON bom_lines(component_product_id);
CREATE INDEX idx_eco_company_status ON engineering_change_orders(company_id, status);
CREATE INDEX idx_routing_headers_product ON routing_headers(company_id, product_id);
CREATE INDEX idx_mrp_runs_company_status ON mrp_runs(company_id, status);
CREATE INDEX idx_planned_orders_company_product ON planned_orders(company_id, product_id);
CREATE INDEX idx_planned_orders_required_date ON planned_orders(required_date);
CREATE INDEX idx_mrp_pegging_links_run ON mrp_pegging_links(mrp_run_id);
CREATE INDEX idx_capacity_plans_work_center ON capacity_plans(work_center_id, planning_date);
CREATE INDEX idx_production_orders_company_status ON production_orders(company_id, status);
CREATE INDEX idx_production_orders_product ON production_orders(product_id);
CREATE INDEX idx_production_orders_planned_start ON production_orders(planned_start_date);
CREATE INDEX idx_production_issues_order ON production_material_issues(production_order_id);
CREATE INDEX idx_production_confirmations_order ON production_confirmations(production_order_id);
CREATE INDEX idx_production_costs_order ON production_costs(production_order_id);
CREATE INDEX idx_manufacturing_serial_genealogy_serial ON manufacturing_serial_genealogy(serial_number);
CREATE INDEX idx_manufacturing_batch_genealogy_batch ON manufacturing_batch_genealogy(batch_number);
CREATE INDEX idx_quality_inspections_order ON quality_inspections(production_order_id);
CREATE INDEX idx_quality_inspections_status ON quality_inspections(company_id, status);
CREATE INDEX idx_manufacturing_events_type ON manufacturing_events(company_id, event_type);
CREATE INDEX idx_manufacturing_events_reference ON manufacturing_events(reference_type, reference_id);
CREATE INDEX idx_manufacturing_events_occurred ON manufacturing_events(occurred_at);

-- ============================================================
-- SECTION 12: MATERIALIZED REPORTING VIEWS
-- ============================================================

CREATE MATERIALIZED VIEW mv_production_dashboard AS
SELECT
    po.company_id,
    po.status,
    COUNT(po.id)                                        AS order_count,
    SUM(po.target_quantity)                             AS total_target_qty,
    SUM(po.completed_quantity)                          AS total_completed_qty,
    SUM(po.scrapped_quantity)                           AS total_scrapped_qty,
    ROUND(SUM(po.actual_cost), 2)                       AS total_actual_cost,
    ROUND(SUM(po.standard_cost), 2)                     AS total_standard_cost,
    DATE_TRUNC('month', po.planned_start_date)          AS planning_month
FROM production_orders po
GROUP BY po.company_id, po.status, DATE_TRUNC('month', po.planned_start_date);

CREATE UNIQUE INDEX idx_mv_production_dashboard ON mv_production_dashboard(company_id, status, planning_month);

CREATE MATERIALIZED VIEW mv_wip_valuation AS
SELECT
    po.company_id,
    po.product_id,
    COUNT(po.id)                                        AS wip_orders,
    SUM(pc.actual_material_cost)                        AS total_material_cost,
    SUM(pc.actual_labor_cost)                           AS total_labor_cost,
    SUM(pc.actual_machine_cost)                         AS total_machine_cost,
    SUM(pc.actual_overhead_cost)                        AS total_overhead_cost,
    SUM(pc.wip_balance)                                 AS total_wip_balance,
    SUM(pc.total_variance)                              AS total_variance
FROM production_orders po
JOIN production_costs pc ON pc.production_order_id = po.id
WHERE po.status IN ('MATERIAL_ALLOCATED', 'IN_PROGRESS', 'PARTIALLY_COMPLETED', 'QUALITY_PENDING')
GROUP BY po.company_id, po.product_id;

CREATE UNIQUE INDEX idx_mv_wip_valuation ON mv_wip_valuation(company_id, product_id);

CREATE MATERIALIZED VIEW mv_oee_summary AS
SELECT
    poo.work_center_id,
    DATE_TRUNC('week', poo.actual_start_datetime)       AS week_start,
    SUM(poo.actual_run_hours)                           AS total_run_hours,
    SUM(poo.planned_run_hours)                          AS total_planned_hours,
    SUM(poo.completed_quantity)                         AS completed_qty,
    SUM(poo.scrapped_quantity)                          AS scrapped_qty,
    CASE
        WHEN SUM(poo.planned_run_hours) > 0
        THEN ROUND(100.0 * SUM(poo.actual_run_hours) / SUM(poo.planned_run_hours), 2)
        ELSE 0
    END                                                 AS availability_pct,
    CASE
        WHEN SUM(poo.completed_quantity) + SUM(poo.scrapped_quantity) > 0
        THEN ROUND(100.0 * SUM(poo.completed_quantity) / (SUM(poo.completed_quantity) + SUM(poo.scrapped_quantity)), 2)
        ELSE 0
    END                                                 AS first_pass_yield_pct
FROM production_order_operations poo
WHERE poo.actual_start_datetime IS NOT NULL
GROUP BY poo.work_center_id, DATE_TRUNC('week', poo.actual_start_datetime);

CREATE INDEX idx_mv_oee_summary ON mv_oee_summary(work_center_id, week_start);

CREATE MATERIALIZED VIEW mv_manufacturing_variances AS
SELECT
    po.company_id,
    po.product_id,
    DATE_TRUNC('month', po.actual_end_date)             AS posting_month,
    COUNT(po.id)                                        AS order_count,
    SUM(pc.material_variance)                           AS material_variance,
    SUM(pc.labor_variance)                              AS labor_variance,
    SUM(pc.machine_variance)                            AS machine_variance,
    SUM(pc.overhead_variance)                           AS overhead_variance,
    SUM(pc.total_variance)                              AS total_variance
FROM production_orders po
JOIN production_costs pc ON pc.production_order_id = po.id
WHERE po.status IN ('COMPLETED', 'CLOSED')
GROUP BY po.company_id, po.product_id, DATE_TRUNC('month', po.actual_end_date);

CREATE INDEX idx_mv_manufacturing_variances ON mv_manufacturing_variances(company_id, posting_month);
