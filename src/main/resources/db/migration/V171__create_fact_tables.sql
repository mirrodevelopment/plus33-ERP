-- V171: Enterprise Data Warehouse � Fact Tables
-- Purpose: Star schema fact tables for finance, sales, inventory, payroll,
--          manufacturing, projects, HCM, CRM, procurement, and GRC.

-- -----------------------------------------------------------------------------
-- FACT FINANCE: General ledger transactions
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS fact_finance (
    id                  BIGSERIAL PRIMARY KEY,
    company_id          BIGINT NOT NULL,
    date_key            INTEGER NOT NULL REFERENCES dim_date(date_key),
    account_dim_id      BIGINT REFERENCES dim_account(id),
    org_dim_id          BIGINT REFERENCES dim_organization(id),
    project_dim_id      BIGINT REFERENCES dim_project(id),
    currency_dim_id     BIGINT REFERENCES dim_currency(id),
    debit_amount        NUMERIC(19,4) NOT NULL DEFAULT 0,
    credit_amount       NUMERIC(19,4) NOT NULL DEFAULT 0,
    net_amount          NUMERIC(19,4) NOT NULL DEFAULT 0,
    transaction_count   INTEGER NOT NULL DEFAULT 1,
    reference_number    VARCHAR(100),
    source_table        VARCHAR(100),
    batch_id            VARCHAR(100),
    loaded_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- -----------------------------------------------------------------------------
-- FACT SALES: Sales order lines and invoices
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS fact_sales (
    id                  BIGSERIAL PRIMARY KEY,
    company_id          BIGINT NOT NULL,
    date_key            INTEGER NOT NULL REFERENCES dim_date(date_key),
    customer_dim_id     BIGINT REFERENCES dim_customer(id),
    product_dim_id      BIGINT REFERENCES dim_product(id),
    org_dim_id          BIGINT REFERENCES dim_organization(id),
    currency_dim_id     BIGINT REFERENCES dim_currency(id),
    salesperson_emp_id  BIGINT REFERENCES dim_employee(id),
    quantity            NUMERIC(19,4) NOT NULL DEFAULT 0,
    unit_price          NUMERIC(19,4) NOT NULL DEFAULT 0,
    discount_amount     NUMERIC(19,4) NOT NULL DEFAULT 0,
    net_amount          NUMERIC(19,4) NOT NULL DEFAULT 0,
    tax_amount          NUMERIC(19,4) NOT NULL DEFAULT 0,
    gross_amount        NUMERIC(19,4) NOT NULL DEFAULT 0,
    cost_of_goods       NUMERIC(19,4) NOT NULL DEFAULT 0,
    gross_margin        NUMERIC(19,4) NOT NULL DEFAULT 0,
    sales_channel       VARCHAR(100),
    sales_region        VARCHAR(100),
    source_table        VARCHAR(100),
    batch_id            VARCHAR(100),
    loaded_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- -----------------------------------------------------------------------------
-- FACT INVENTORY: Stock movements and valuation
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS fact_inventory (
    id                  BIGSERIAL PRIMARY KEY,
    company_id          BIGINT NOT NULL,
    date_key            INTEGER NOT NULL REFERENCES dim_date(date_key),
    product_dim_id      BIGINT REFERENCES dim_product(id),
    location_dim_id     BIGINT REFERENCES dim_location(id),
    currency_dim_id     BIGINT REFERENCES dim_currency(id),
    movement_type       VARCHAR(50) NOT NULL,
    quantity_in         NUMERIC(19,4) NOT NULL DEFAULT 0,
    quantity_out        NUMERIC(19,4) NOT NULL DEFAULT 0,
    closing_stock       NUMERIC(19,4) NOT NULL DEFAULT 0,
    unit_cost           NUMERIC(19,4) NOT NULL DEFAULT 0,
    total_value         NUMERIC(19,4) NOT NULL DEFAULT 0,
    source_table        VARCHAR(100),
    batch_id            VARCHAR(100),
    loaded_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- -----------------------------------------------------------------------------
-- FACT PAYROLL: Payroll runs by employee and period
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS fact_payroll (
    id                  BIGSERIAL PRIMARY KEY,
    company_id          BIGINT NOT NULL,
    date_key            INTEGER NOT NULL REFERENCES dim_date(date_key),
    employee_dim_id     BIGINT REFERENCES dim_employee(id),
    org_dim_id          BIGINT REFERENCES dim_organization(id),
    currency_dim_id     BIGINT REFERENCES dim_currency(id),
    gross_salary        NUMERIC(19,4) NOT NULL DEFAULT 0,
    deductions          NUMERIC(19,4) NOT NULL DEFAULT 0,
    net_salary          NUMERIC(19,4) NOT NULL DEFAULT 0,
    employer_cost       NUMERIC(19,4) NOT NULL DEFAULT 0,
    pay_period_start    DATE,
    pay_period_end      DATE,
    source_table        VARCHAR(100),
    batch_id            VARCHAR(100),
    loaded_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- -----------------------------------------------------------------------------
-- FACT MANUFACTURING: Production output and costs
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS fact_manufacturing (
    id                  BIGSERIAL PRIMARY KEY,
    company_id          BIGINT NOT NULL,
    date_key            INTEGER NOT NULL REFERENCES dim_date(date_key),
    product_dim_id      BIGINT REFERENCES dim_product(id),
    location_dim_id     BIGINT REFERENCES dim_location(id),
    currency_dim_id     BIGINT REFERENCES dim_currency(id),
    planned_qty         NUMERIC(19,4) NOT NULL DEFAULT 0,
    actual_qty          NUMERIC(19,4) NOT NULL DEFAULT 0,
    scrap_qty           NUMERIC(19,4) NOT NULL DEFAULT 0,
    labor_hours         NUMERIC(10,4) NOT NULL DEFAULT 0,
    machine_hours       NUMERIC(10,4) NOT NULL DEFAULT 0,
    material_cost       NUMERIC(19,4) NOT NULL DEFAULT 0,
    labor_cost          NUMERIC(19,4) NOT NULL DEFAULT 0,
    overhead_cost       NUMERIC(19,4) NOT NULL DEFAULT 0,
    total_production_cost NUMERIC(19,4) NOT NULL DEFAULT 0,
    work_center         VARCHAR(100),
    source_table        VARCHAR(100),
    batch_id            VARCHAR(100),
    loaded_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- -----------------------------------------------------------------------------
-- FACT PROCUREMENT: Purchase orders and supplier transactions
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS fact_procurement (
    id                  BIGSERIAL PRIMARY KEY,
    company_id          BIGINT NOT NULL,
    date_key            INTEGER NOT NULL REFERENCES dim_date(date_key),
    supplier_dim_id     BIGINT REFERENCES dim_supplier(id),
    product_dim_id      BIGINT REFERENCES dim_product(id),
    location_dim_id     BIGINT REFERENCES dim_location(id),
    currency_dim_id     BIGINT REFERENCES dim_currency(id),
    quantity            NUMERIC(19,4) NOT NULL DEFAULT 0,
    unit_price          NUMERIC(19,4) NOT NULL DEFAULT 0,
    net_amount          NUMERIC(19,4) NOT NULL DEFAULT 0,
    tax_amount          NUMERIC(19,4) NOT NULL DEFAULT 0,
    gross_amount        NUMERIC(19,4) NOT NULL DEFAULT 0,
    lead_time_days      INTEGER NOT NULL DEFAULT 0,
    source_table        VARCHAR(100),
    batch_id            VARCHAR(100),
    loaded_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- -----------------------------------------------------------------------------
-- FACT PROJECT FINANCIALS: Project cost and revenue
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS fact_project_financials (
    id                  BIGSERIAL PRIMARY KEY,
    company_id          BIGINT NOT NULL,
    date_key            INTEGER NOT NULL REFERENCES dim_date(date_key),
    project_dim_id      BIGINT REFERENCES dim_project(id),
    employee_dim_id     BIGINT REFERENCES dim_employee(id),
    customer_dim_id     BIGINT REFERENCES dim_customer(id),
    currency_dim_id     BIGINT REFERENCES dim_currency(id),
    hours_logged        NUMERIC(10,4) NOT NULL DEFAULT 0,
    labor_cost          NUMERIC(19,4) NOT NULL DEFAULT 0,
    material_cost       NUMERIC(19,4) NOT NULL DEFAULT 0,
    overhead_cost       NUMERIC(19,4) NOT NULL DEFAULT 0,
    total_cost          NUMERIC(19,4) NOT NULL DEFAULT 0,
    budgeted_cost       NUMERIC(19,4) NOT NULL DEFAULT 0,
    revenue_recognized  NUMERIC(19,4) NOT NULL DEFAULT 0,
    milestone_status    VARCHAR(50),
    source_table        VARCHAR(100),
    batch_id            VARCHAR(100),
    loaded_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- -----------------------------------------------------------------------------
-- FACT HCM: Headcount and workforce metrics
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS fact_hcm (
    id                  BIGSERIAL PRIMARY KEY,
    company_id          BIGINT NOT NULL,
    date_key            INTEGER NOT NULL REFERENCES dim_date(date_key),
    employee_dim_id     BIGINT REFERENCES dim_employee(id),
    org_dim_id          BIGINT REFERENCES dim_organization(id),
    headcount           INTEGER NOT NULL DEFAULT 0,
    attrition_count     INTEGER NOT NULL DEFAULT 0,
    new_hires           INTEGER NOT NULL DEFAULT 0,
    performance_score   NUMERIC(5,2),
    absence_days        NUMERIC(8,2) NOT NULL DEFAULT 0,
    source_table        VARCHAR(100),
    batch_id            VARCHAR(100),
    loaded_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- -----------------------------------------------------------------------------
-- FACT CRM: Pipeline and activity metrics
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS fact_crm (
    id                  BIGSERIAL PRIMARY KEY,
    company_id          BIGINT NOT NULL,
    date_key            INTEGER NOT NULL REFERENCES dim_date(date_key),
    customer_dim_id     BIGINT REFERENCES dim_customer(id),
    employee_dim_id     BIGINT REFERENCES dim_employee(id),
    currency_dim_id     BIGINT REFERENCES dim_currency(id),
    activity_type       VARCHAR(100),
    pipeline_stage      VARCHAR(100),
    deal_value          NUMERIC(19,4) NOT NULL DEFAULT 0,
    probability         NUMERIC(5,2) NOT NULL DEFAULT 0,
    weighted_value      NUMERIC(19,4) NOT NULL DEFAULT 0,
    activity_count      INTEGER NOT NULL DEFAULT 1,
    source_table        VARCHAR(100),
    batch_id            VARCHAR(100),
    loaded_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- -----------------------------------------------------------------------------
-- FACT GRC: Risk and compliance snapshot metrics
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS fact_grc (
    id                  BIGSERIAL PRIMARY KEY,
    company_id          BIGINT NOT NULL,
    date_key            INTEGER NOT NULL REFERENCES dim_date(date_key),
    org_dim_id          BIGINT REFERENCES dim_organization(id),
    risk_domain         VARCHAR(100),
    risk_category       VARCHAR(100),
    inherent_score      NUMERIC(5,2) NOT NULL DEFAULT 0,
    residual_score      NUMERIC(5,2) NOT NULL DEFAULT 0,
    open_findings       INTEGER NOT NULL DEFAULT 0,
    overdue_caps        INTEGER NOT NULL DEFAULT 0,
    compliance_rate     NUMERIC(5,2) NOT NULL DEFAULT 0,
    source_table        VARCHAR(100),
    batch_id            VARCHAR(100),
    loaded_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
