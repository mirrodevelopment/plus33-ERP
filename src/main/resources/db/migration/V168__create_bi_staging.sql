-- V168: BI Analytical Staging Layer
-- Purpose: Transient staging tables (stg_*) for raw extraction from operational modules.
-- Data lands here first, is validated, then promoted to DWH dimension/fact tables.

-- -----------------------------------------------------------------------------
-- CDC WATERMARK: tracks incremental extraction progress per source module/table
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS bi_cdc_watermark (
    id                  BIGSERIAL PRIMARY KEY,
    source_module       VARCHAR(100) NOT NULL,
    source_table        VARCHAR(100) NOT NULL,
    last_event_id       BIGINT,
    last_timestamp      TIMESTAMP,
    last_run            TIMESTAMP,
    status              VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_cdc_watermark UNIQUE (source_module, source_table)
);

-- -----------------------------------------------------------------------------
-- STAGING: Finance (GL transactions, invoices, payments)
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS stg_finance (
    id                  BIGSERIAL PRIMARY KEY,
    company_id          BIGINT NOT NULL,
    source_event_id     BIGINT,
    source_table        VARCHAR(100) NOT NULL,
    transaction_date    DATE NOT NULL,
    account_code        VARCHAR(50),
    account_name        VARCHAR(200),
    debit_amount        NUMERIC(19,4) DEFAULT 0,
    credit_amount       NUMERIC(19,4) DEFAULT 0,
    currency_code       VARCHAR(10) DEFAULT 'USD',
    reference_number    VARCHAR(100),
    cost_center         VARCHAR(100),
    department          VARCHAR(100),
    project_code        VARCHAR(100),
    raw_payload         TEXT,
    load_status         VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    validation_errors   TEXT,
    loaded_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    batch_id            VARCHAR(100)
);

-- -----------------------------------------------------------------------------
-- STAGING: Sales (orders, invoices, returns)
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS stg_sales (
    id                  BIGSERIAL PRIMARY KEY,
    company_id          BIGINT NOT NULL,
    source_event_id     BIGINT,
    source_table        VARCHAR(100) NOT NULL,
    order_date          DATE,
    customer_id         BIGINT,
    customer_code       VARCHAR(100),
    customer_name       VARCHAR(200),
    product_code        VARCHAR(100),
    product_name        VARCHAR(200),
    quantity            NUMERIC(19,4) DEFAULT 0,
    unit_price          NUMERIC(19,4) DEFAULT 0,
    discount_amount     NUMERIC(19,4) DEFAULT 0,
    net_amount          NUMERIC(19,4) DEFAULT 0,
    tax_amount          NUMERIC(19,4) DEFAULT 0,
    gross_amount        NUMERIC(19,4) DEFAULT 0,
    currency_code       VARCHAR(10) DEFAULT 'USD',
    sales_region        VARCHAR(100),
    sales_channel       VARCHAR(100),
    salesperson_id      BIGINT,
    raw_payload         TEXT,
    load_status         VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    validation_errors   TEXT,
    loaded_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    batch_id            VARCHAR(100)
);

-- -----------------------------------------------------------------------------
-- STAGING: Inventory (stock movements, adjustments, transfers)
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS stg_inventory (
    id                  BIGSERIAL PRIMARY KEY,
    company_id          BIGINT NOT NULL,
    source_event_id     BIGINT,
    source_table        VARCHAR(100) NOT NULL,
    movement_date       DATE,
    product_code        VARCHAR(100),
    product_name        VARCHAR(200),
    warehouse_code      VARCHAR(100),
    location_code       VARCHAR(100),
    movement_type       VARCHAR(50),
    quantity            NUMERIC(19,4) DEFAULT 0,
    unit_cost           NUMERIC(19,4) DEFAULT 0,
    total_cost          NUMERIC(19,4) DEFAULT 0,
    currency_code       VARCHAR(10) DEFAULT 'USD',
    reference_number    VARCHAR(100),
    raw_payload         TEXT,
    load_status         VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    validation_errors   TEXT,
    loaded_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    batch_id            VARCHAR(100)
);

-- -----------------------------------------------------------------------------
-- STAGING: Payroll (runs, slips, deductions, allowances)
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS stg_payroll (
    id                  BIGSERIAL PRIMARY KEY,
    company_id          BIGINT NOT NULL,
    source_event_id     BIGINT,
    source_table        VARCHAR(100) NOT NULL,
    pay_period_start    DATE,
    pay_period_end      DATE,
    employee_id         BIGINT,
    employee_code       VARCHAR(100),
    department          VARCHAR(100),
    cost_center         VARCHAR(100),
    gross_salary        NUMERIC(19,4) DEFAULT 0,
    deductions          NUMERIC(19,4) DEFAULT 0,
    net_salary          NUMERIC(19,4) DEFAULT 0,
    employer_cost       NUMERIC(19,4) DEFAULT 0,
    currency_code       VARCHAR(10) DEFAULT 'USD',
    raw_payload         TEXT,
    load_status         VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    validation_errors   TEXT,
    loaded_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    batch_id            VARCHAR(100)
);

-- -----------------------------------------------------------------------------
-- STAGING: Manufacturing (production orders, confirmations, quality)
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS stg_manufacturing (
    id                  BIGSERIAL PRIMARY KEY,
    company_id          BIGINT NOT NULL,
    source_event_id     BIGINT,
    source_table        VARCHAR(100) NOT NULL,
    production_date     DATE,
    work_center         VARCHAR(100),
    product_code        VARCHAR(100),
    product_name        VARCHAR(200),
    planned_qty         NUMERIC(19,4) DEFAULT 0,
    actual_qty          NUMERIC(19,4) DEFAULT 0,
    scrap_qty           NUMERIC(19,4) DEFAULT 0,
    labor_hours         NUMERIC(10,4) DEFAULT 0,
    machine_hours       NUMERIC(10,4) DEFAULT 0,
    material_cost       NUMERIC(19,4) DEFAULT 0,
    labor_cost          NUMERIC(19,4) DEFAULT 0,
    overhead_cost       NUMERIC(19,4) DEFAULT 0,
    raw_payload         TEXT,
    load_status         VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    validation_errors   TEXT,
    loaded_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    batch_id            VARCHAR(100)
);

-- -----------------------------------------------------------------------------
-- STAGING: CRM (leads, opportunities, activities)
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS stg_crm (
    id                  BIGSERIAL PRIMARY KEY,
    company_id          BIGINT NOT NULL,
    source_event_id     BIGINT,
    source_table        VARCHAR(100) NOT NULL,
    activity_date       DATE,
    customer_id         BIGINT,
    customer_code       VARCHAR(100),
    customer_name       VARCHAR(200),
    activity_type       VARCHAR(100),
    stage               VARCHAR(100),
    deal_value          NUMERIC(19,4) DEFAULT 0,
    probability         NUMERIC(5,2) DEFAULT 0,
    currency_code       VARCHAR(10) DEFAULT 'USD',
    owner_id            BIGINT,
    raw_payload         TEXT,
    load_status         VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    validation_errors   TEXT,
    loaded_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    batch_id            VARCHAR(100)
);

-- -----------------------------------------------------------------------------
-- STAGING: Procurement (POs, GRNs, supplier invoices)
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS stg_procurement (
    id                  BIGSERIAL PRIMARY KEY,
    company_id          BIGINT NOT NULL,
    source_event_id     BIGINT,
    source_table        VARCHAR(100) NOT NULL,
    transaction_date    DATE,
    supplier_id         BIGINT,
    supplier_code       VARCHAR(100),
    supplier_name       VARCHAR(200),
    product_code        VARCHAR(100),
    product_name        VARCHAR(200),
    quantity            NUMERIC(19,4) DEFAULT 0,
    unit_price          NUMERIC(19,4) DEFAULT 0,
    net_amount          NUMERIC(19,4) DEFAULT 0,
    tax_amount          NUMERIC(19,4) DEFAULT 0,
    gross_amount        NUMERIC(19,4) DEFAULT 0,
    currency_code       VARCHAR(10) DEFAULT 'USD',
    lead_time_days      INTEGER DEFAULT 0,
    raw_payload         TEXT,
    load_status         VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    validation_errors   TEXT,
    loaded_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    batch_id            VARCHAR(100)
);

-- -----------------------------------------------------------------------------
-- STAGING: Projects (timesheets, milestones, financials)
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS stg_projects (
    id                  BIGSERIAL PRIMARY KEY,
    company_id          BIGINT NOT NULL,
    source_event_id     BIGINT,
    source_table        VARCHAR(100) NOT NULL,
    transaction_date    DATE,
    project_id          BIGINT,
    project_code        VARCHAR(100),
    project_name        VARCHAR(200),
    employee_id         BIGINT,
    hours_logged        NUMERIC(10,4) DEFAULT 0,
    labor_cost          NUMERIC(19,4) DEFAULT 0,
    material_cost       NUMERIC(19,4) DEFAULT 0,
    overhead_cost       NUMERIC(19,4) DEFAULT 0,
    budgeted_cost       NUMERIC(19,4) DEFAULT 0,
    milestone_status    VARCHAR(50),
    raw_payload         TEXT,
    load_status         VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    validation_errors   TEXT,
    loaded_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    batch_id            VARCHAR(100)
);

-- -----------------------------------------------------------------------------
-- STAGING: HCM (headcount, attrition, performance, recruitment)
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS stg_hcm (
    id                  BIGSERIAL PRIMARY KEY,
    company_id          BIGINT NOT NULL,
    source_event_id     BIGINT,
    source_table        VARCHAR(100) NOT NULL,
    snapshot_date       DATE,
    employee_id         BIGINT,
    employee_code       VARCHAR(100),
    department          VARCHAR(100),
    position_title      VARCHAR(200),
    employment_type     VARCHAR(50),
    hire_date           DATE,
    termination_date    DATE,
    performance_score   NUMERIC(5,2),
    headcount_flag      BOOLEAN DEFAULT TRUE,
    attrition_flag      BOOLEAN DEFAULT FALSE,
    raw_payload         TEXT,
    load_status         VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    validation_errors   TEXT,
    loaded_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    batch_id            VARCHAR(100)
);

-- -----------------------------------------------------------------------------
-- STAGING: GRC (risks, compliance, audit findings)
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS stg_grc (
    id                  BIGSERIAL PRIMARY KEY,
    company_id          BIGINT NOT NULL,
    source_event_id     BIGINT,
    source_table        VARCHAR(100) NOT NULL,
    snapshot_date       DATE,
    domain              VARCHAR(100),
    risk_category       VARCHAR(100),
    inherent_score      NUMERIC(5,2) DEFAULT 0,
    residual_score      NUMERIC(5,2) DEFAULT 0,
    open_findings       INTEGER DEFAULT 0,
    overdue_caps        INTEGER DEFAULT 0,
    compliance_rate     NUMERIC(5,2) DEFAULT 0,
    raw_payload         TEXT,
    load_status         VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    validation_errors   TEXT,
    loaded_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    batch_id            VARCHAR(100)
);

-- -----------------------------------------------------------------------------
-- ETL CONTROL: batch tracking for all staging loads
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS bi_etl_batch_log (
    id                  BIGSERIAL PRIMARY KEY,
    batch_id            VARCHAR(100) NOT NULL UNIQUE,
    source_module       VARCHAR(100) NOT NULL,
    source_table        VARCHAR(100) NOT NULL,
    job_type            VARCHAR(50) NOT NULL DEFAULT 'FULL',
    records_extracted   INTEGER DEFAULT 0,
    records_staged      INTEGER DEFAULT 0,
    records_rejected    INTEGER DEFAULT 0,
    started_at          TIMESTAMP,
    completed_at        TIMESTAMP,
    status              VARCHAR(30) NOT NULL DEFAULT 'RUNNING',
    error_message       TEXT,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
