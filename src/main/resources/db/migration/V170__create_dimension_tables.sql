-- V170: Enterprise Data Warehouse � Dimension Tables
-- Purpose: Conformed dimensions for the star schema. All dimensions support
--          SCD Type 2 (effective_from / effective_to / is_current).

-- -----------------------------------------------------------------------------
-- DIM DATE: fiscal and calendar date attributes
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS dim_date (
    id                  BIGSERIAL PRIMARY KEY,
    date_key            INTEGER NOT NULL UNIQUE,
    full_date           DATE NOT NULL UNIQUE,
    day_of_week         INTEGER,
    day_name            VARCHAR(20),
    day_of_month        INTEGER,
    day_of_year         INTEGER,
    week_of_year        INTEGER,
    month_number        INTEGER,
    month_name          VARCHAR(20),
    quarter_number      INTEGER,
    quarter_name        VARCHAR(10),
    year_number         INTEGER,
    fiscal_year         INTEGER,
    fiscal_quarter      INTEGER,
    fiscal_month        INTEGER,
    is_weekend          BOOLEAN NOT NULL DEFAULT FALSE,
    is_holiday          BOOLEAN NOT NULL DEFAULT FALSE,
    is_working_day      BOOLEAN NOT NULL DEFAULT TRUE
);

-- -----------------------------------------------------------------------------
-- DIM COMPANY: multi-company isolation dimension
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS dim_company (
    id                  BIGSERIAL PRIMARY KEY,
    company_id          BIGINT NOT NULL,
    company_code        VARCHAR(50) NOT NULL,
    company_name        VARCHAR(200) NOT NULL,
    country             VARCHAR(100),
    currency_code       VARCHAR(10),
    industry            VARCHAR(100),
    effective_from      DATE NOT NULL,
    effective_to        DATE,
    is_current          BOOLEAN NOT NULL DEFAULT TRUE,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- -----------------------------------------------------------------------------
-- DIM CUSTOMER: CRM / Sales customers with SCD Type 2
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS dim_customer (
    id                  BIGSERIAL PRIMARY KEY,
    company_id          BIGINT NOT NULL,
    customer_id         BIGINT NOT NULL,
    customer_code       VARCHAR(100) NOT NULL,
    customer_name       VARCHAR(200) NOT NULL,
    customer_type       VARCHAR(50),
    segment             VARCHAR(100),
    industry            VARCHAR(100),
    region              VARCHAR(100),
    country             VARCHAR(100),
    credit_limit        NUMERIC(19,4) DEFAULT 0,
    currency_code       VARCHAR(10) DEFAULT 'USD',
    is_active           BOOLEAN NOT NULL DEFAULT TRUE,
    effective_from      DATE NOT NULL,
    effective_to        DATE,
    is_current          BOOLEAN NOT NULL DEFAULT TRUE,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- -----------------------------------------------------------------------------
-- DIM PRODUCT: inventory and sales products with SCD Type 2
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS dim_product (
    id                  BIGSERIAL PRIMARY KEY,
    company_id          BIGINT NOT NULL,
    product_id          BIGINT NOT NULL,
    product_code        VARCHAR(100) NOT NULL,
    product_name        VARCHAR(200) NOT NULL,
    product_category    VARCHAR(100),
    product_subcategory VARCHAR(100),
    uom                 VARCHAR(20),
    standard_cost       NUMERIC(19,4) DEFAULT 0,
    list_price          NUMERIC(19,4) DEFAULT 0,
    currency_code       VARCHAR(10) DEFAULT 'USD',
    is_active           BOOLEAN NOT NULL DEFAULT TRUE,
    effective_from      DATE NOT NULL,
    effective_to        DATE,
    is_current          BOOLEAN NOT NULL DEFAULT TRUE,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- -----------------------------------------------------------------------------
-- DIM SUPPLIER: procurement suppliers with SCD Type 2
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS dim_supplier (
    id                  BIGSERIAL PRIMARY KEY,
    company_id          BIGINT NOT NULL,
    supplier_id         BIGINT NOT NULL,
    supplier_code       VARCHAR(100) NOT NULL,
    supplier_name       VARCHAR(200) NOT NULL,
    supplier_category   VARCHAR(100),
    country             VARCHAR(100),
    region              VARCHAR(100),
    currency_code       VARCHAR(10) DEFAULT 'USD',
    payment_terms       VARCHAR(50),
    risk_rating         VARCHAR(20),
    is_active           BOOLEAN NOT NULL DEFAULT TRUE,
    effective_from      DATE NOT NULL,
    effective_to        DATE,
    is_current          BOOLEAN NOT NULL DEFAULT TRUE,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- -----------------------------------------------------------------------------
-- DIM EMPLOYEE: HCM employees with SCD Type 2
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS dim_employee (
    id                  BIGSERIAL PRIMARY KEY,
    company_id          BIGINT NOT NULL,
    employee_id         BIGINT NOT NULL,
    employee_code       VARCHAR(100) NOT NULL,
    full_name           VARCHAR(200) NOT NULL,
    department          VARCHAR(100),
    position_title      VARCHAR(200),
    employment_type     VARCHAR(50),
    cost_center         VARCHAR(100),
    location            VARCHAR(100),
    manager_id          BIGINT,
    hire_date           DATE,
    termination_date    DATE,
    is_active           BOOLEAN NOT NULL DEFAULT TRUE,
    effective_from      DATE NOT NULL,
    effective_to        DATE,
    is_current          BOOLEAN NOT NULL DEFAULT TRUE,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- -----------------------------------------------------------------------------
-- DIM ORGANIZATION: organizational hierarchy
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS dim_organization (
    id                  BIGSERIAL PRIMARY KEY,
    company_id          BIGINT NOT NULL,
    org_id              BIGINT NOT NULL,
    org_code            VARCHAR(100) NOT NULL,
    org_name            VARCHAR(200) NOT NULL,
    org_type            VARCHAR(50),
    parent_org_id       BIGINT,
    cost_center         VARCHAR(100),
    effective_from      DATE NOT NULL,
    effective_to        DATE,
    is_current          BOOLEAN NOT NULL DEFAULT TRUE,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- -----------------------------------------------------------------------------
-- DIM ACCOUNT: chart of accounts for financial reporting
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS dim_account (
    id                  BIGSERIAL PRIMARY KEY,
    company_id          BIGINT NOT NULL,
    account_id          BIGINT NOT NULL,
    account_code        VARCHAR(50) NOT NULL,
    account_name        VARCHAR(200) NOT NULL,
    account_type        VARCHAR(50),
    account_category    VARCHAR(100),
    parent_account_code VARCHAR(50),
    is_active           BOOLEAN NOT NULL DEFAULT TRUE,
    effective_from      DATE NOT NULL,
    effective_to        DATE,
    is_current          BOOLEAN NOT NULL DEFAULT TRUE,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- -----------------------------------------------------------------------------
-- DIM LOCATION: warehouses, stores, sites, plants
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS dim_location (
    id                  BIGSERIAL PRIMARY KEY,
    company_id          BIGINT NOT NULL,
    location_id         BIGINT,
    location_code       VARCHAR(100) NOT NULL,
    location_name       VARCHAR(200) NOT NULL,
    location_type       VARCHAR(50),
    city                VARCHAR(100),
    country             VARCHAR(100),
    region              VARCHAR(100),
    is_active           BOOLEAN NOT NULL DEFAULT TRUE,
    effective_from      DATE NOT NULL,
    effective_to        DATE,
    is_current          BOOLEAN NOT NULL DEFAULT TRUE,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- -----------------------------------------------------------------------------
-- DIM CURRENCY: currency exchange rates by date
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS dim_currency (
    id                  BIGSERIAL PRIMARY KEY,
    currency_code       VARCHAR(10) NOT NULL,
    currency_name       VARCHAR(100) NOT NULL,
    rate_to_usd         NUMERIC(19,8) NOT NULL DEFAULT 1,
    rate_date           DATE NOT NULL,
    is_base_currency    BOOLEAN NOT NULL DEFAULT FALSE,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_currency_rate UNIQUE (currency_code, rate_date)
);

-- -----------------------------------------------------------------------------
-- DIM PROJECT: PPM projects with SCD Type 2
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS dim_project (
    id                  BIGSERIAL PRIMARY KEY,
    company_id          BIGINT NOT NULL,
    project_id          BIGINT NOT NULL,
    project_code        VARCHAR(100) NOT NULL,
    project_name        VARCHAR(200) NOT NULL,
    project_type        VARCHAR(50),
    project_manager_id  BIGINT,
    customer_id         BIGINT,
    department          VARCHAR(100),
    start_date          DATE,
    end_date            DATE,
    budget_amount       NUMERIC(19,4) DEFAULT 0,
    currency_code       VARCHAR(10) DEFAULT 'USD',
    status              VARCHAR(30),
    effective_from      DATE NOT NULL,
    effective_to        DATE,
    is_current          BOOLEAN NOT NULL DEFAULT TRUE,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- -----------------------------------------------------------------------------
-- DIMENSION HEALTH STATUS: conformance monitoring
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS bi_dimension_health_status (
    id                  BIGSERIAL PRIMARY KEY,
    dimension_table     VARCHAR(100) NOT NULL,
    check_date          DATE NOT NULL,
    missing_members     INTEGER DEFAULT 0,
    duplicate_members   INTEGER DEFAULT 0,
    expired_records     INTEGER DEFAULT 0,
    orphan_references   INTEGER DEFAULT 0,
    scd_violations      INTEGER DEFAULT 0,
    overall_health      VARCHAR(20) NOT NULL DEFAULT 'UNKNOWN',
    checked_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
