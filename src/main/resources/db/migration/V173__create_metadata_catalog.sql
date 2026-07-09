-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 173
-- File              : V173__create_metadata_catalog.sql
-- Operation Type    : Schema Creation
-- Purpose           : create metadata catalog
--
-- Tables Created    : IF, IF, IF, IF, IF, IF, IF, IF, IF, IF, IF, IF, IF, IF
-- Tables Altered    : N/A
-- Seed Data For     : bi_scheduled_job
-- Indexes           : N/A
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
-- V173: BI Metadata Catalog
-- Purpose: Dataset registry, KPI catalog with formula versioning and dependency
--          tracking, cube refresh history, dashboard cache, and semantic models.

-- -----------------------------------------------------------------------------
-- BI DATASET CATALOG: registered analytical datasets
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS bi_dataset (
    id                  BIGSERIAL PRIMARY KEY,
    dataset_name        VARCHAR(200) NOT NULL UNIQUE,
    dataset_type        VARCHAR(50) NOT NULL DEFAULT 'FACT',
    source_table        VARCHAR(100) NOT NULL,
    description         TEXT,
    owner               VARCHAR(100),
    refresh_mode        VARCHAR(30) NOT NULL DEFAULT 'INCREMENTAL',
    is_published        BOOLEAN NOT NULL DEFAULT FALSE,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- -----------------------------------------------------------------------------
-- KPI DEFINITION: executive KPI catalog with approval lifecycle
-- Lifecycle: DRAFT -> UNDER_REVIEW -> APPROVED -> ACTIVE -> SUPERSEDED -> ARCHIVED
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS bi_kpi_definition (
    id                  BIGSERIAL PRIMARY KEY,
    kpi_code            VARCHAR(100) NOT NULL UNIQUE,
    kpi_name            VARCHAR(200) NOT NULL,
    kpi_category        VARCHAR(100),
    description         TEXT,
    unit                VARCHAR(50),
    direction           VARCHAR(10) NOT NULL DEFAULT 'HIGHER',
    target_value        NUMERIC(19,4),
    threshold_warning   NUMERIC(19,4),
    threshold_critical  NUMERIC(19,4),
    status              VARCHAR(30) NOT NULL DEFAULT 'DRAFT',
    owner               VARCHAR(100),
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- -----------------------------------------------------------------------------
-- KPI FORMULA VERSION: immutable compiled expression history
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS bi_kpi_formula_version (
    id                  BIGSERIAL PRIMARY KEY,
    kpi_id              BIGINT NOT NULL REFERENCES bi_kpi_definition(id),
    version_number      INTEGER NOT NULL DEFAULT 1,
    formula_expression  TEXT NOT NULL,
    compiled_expression TEXT,
    effective_from      DATE NOT NULL,
    effective_to        DATE,
    is_current          BOOLEAN NOT NULL DEFAULT TRUE,
    published_by        VARCHAR(100),
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_kpi_formula_version UNIQUE (kpi_id, version_number)
);

-- -----------------------------------------------------------------------------
-- KPI DEPENDENCY: derived KPI graph for cascading recalculations
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS bi_kpi_dependency (
    id                  BIGSERIAL PRIMARY KEY,
    kpi_id              BIGINT NOT NULL REFERENCES bi_kpi_definition(id),
    depends_on_kpi_id   BIGINT NOT NULL REFERENCES bi_kpi_definition(id),
    dependency_type     VARCHAR(30) NOT NULL DEFAULT 'DIRECT',
    CONSTRAINT uk_kpi_dependency UNIQUE (kpi_id, depends_on_kpi_id)
);

-- -----------------------------------------------------------------------------
-- CUBE REFRESH HISTORY: state machine for materialized view/OLAP cube lifecycle
-- States: CREATED -> REFRESH_PENDING -> REFRESHING -> VALIDATING -> AVAILABLE
-- Exception states: FAILED, STALE
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS bi_cube_refresh_history (
    id                  BIGSERIAL PRIMARY KEY,
    cube_name           VARCHAR(200) NOT NULL,
    status              VARCHAR(30) NOT NULL DEFAULT 'CREATED',
    trigger_type        VARCHAR(30) NOT NULL DEFAULT 'SCHEDULED',
    started_at          TIMESTAMP,
    completed_at        TIMESTAMP,
    duration_ms         BIGINT,
    rows_refreshed      INTEGER DEFAULT 0,
    error_message       TEXT,
    triggered_by        VARCHAR(100),
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- -----------------------------------------------------------------------------
-- DASHBOARD CACHE: company-isolated response cache
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS bi_dashboard_cache (
    id                  BIGSERIAL PRIMARY KEY,
    company_id          BIGINT NOT NULL,
    cache_key           VARCHAR(500) NOT NULL,
    cache_group         VARCHAR(100),
    cached_value        TEXT,
    ttl_seconds         INTEGER NOT NULL DEFAULT 300,
    hit_count           INTEGER NOT NULL DEFAULT 0,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at          TIMESTAMP,
    last_accessed_at    TIMESTAMP,
    CONSTRAINT uk_dashboard_cache UNIQUE (company_id, cache_key)
);

-- -----------------------------------------------------------------------------
-- SEMANTIC MODEL: versioned business-friendly layer over warehouse
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS bi_semantic_model (
    id                  BIGSERIAL PRIMARY KEY,
    model_name          VARCHAR(200) NOT NULL UNIQUE,
    model_description   TEXT,
    is_published        BOOLEAN NOT NULL DEFAULT FALSE,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS bi_semantic_model_version (
    id                  BIGSERIAL PRIMARY KEY,
    semantic_model_id   BIGINT NOT NULL REFERENCES bi_semantic_model(id),
    version_number      INTEGER NOT NULL DEFAULT 1,
    effective_from      DATE NOT NULL,
    effective_to        DATE,
    is_current          BOOLEAN NOT NULL DEFAULT TRUE,
    published_by        VARCHAR(100),
    checksum            VARCHAR(200),
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_semantic_model_version UNIQUE (semantic_model_id, version_number)
);

-- -----------------------------------------------------------------------------
-- BUSINESS VIEW: semantic model sub-views with calculated measures
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS bi_business_view (
    id                  BIGSERIAL PRIMARY KEY,
    semantic_model_id   BIGINT NOT NULL REFERENCES bi_semantic_model(id),
    view_name           VARCHAR(200) NOT NULL,
    source_table        VARCHAR(100) NOT NULL,
    description         TEXT,
    is_active           BOOLEAN NOT NULL DEFAULT TRUE,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS bi_calculated_measure (
    id                  BIGSERIAL PRIMARY KEY,
    business_view_id    BIGINT NOT NULL REFERENCES bi_business_view(id),
    measure_name        VARCHAR(200) NOT NULL,
    measure_expression  TEXT NOT NULL,
    measure_type        VARCHAR(50) NOT NULL DEFAULT 'SUM',
    format_string       VARCHAR(50),
    description         TEXT,
    is_active           BOOLEAN NOT NULL DEFAULT TRUE,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- -----------------------------------------------------------------------------
-- BI SCHEDULED JOB: unified scheduler for all BI workload types
-- Types: ETL | ELT | CUBE_REFRESH | FORECAST | EXPORT | MAINTENANCE | SNAPSHOT | VALIDATION
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS bi_scheduled_job (
    id                  BIGSERIAL PRIMARY KEY,
    job_name            VARCHAR(200) NOT NULL UNIQUE,
    job_type            VARCHAR(50) NOT NULL,
    schedule_cron       VARCHAR(100) NOT NULL,
    target_ref          VARCHAR(200),
    priority            INTEGER NOT NULL DEFAULT 50,
    is_enabled          BOOLEAN NOT NULL DEFAULT TRUE,
    last_run_at         TIMESTAMP,
    next_run_at         TIMESTAMP,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- -----------------------------------------------------------------------------
-- DASHBOARD DEFINITION: versioned executive dashboard metadata
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS bi_dashboard_definition (
    id                  BIGSERIAL PRIMARY KEY,
    dashboard_code      VARCHAR(100) NOT NULL UNIQUE,
    dashboard_name      VARCHAR(200) NOT NULL,
    category            VARCHAR(100),
    description         TEXT,
    is_published        BOOLEAN NOT NULL DEFAULT FALSE,
    created_by          VARCHAR(100),
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS bi_dashboard_version (
    id                  BIGSERIAL PRIMARY KEY,
    dashboard_id        BIGINT NOT NULL REFERENCES bi_dashboard_definition(id),
    version_number      INTEGER NOT NULL DEFAULT 1,
    layout_json         TEXT,
    status              VARCHAR(20) NOT NULL DEFAULT 'DRAFT',
    published_by        VARCHAR(100),
    published_at        TIMESTAMP,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_dashboard_version UNIQUE (dashboard_id, version_number)
);

CREATE TABLE IF NOT EXISTS bi_dashboard_widget (
    id                  BIGSERIAL PRIMARY KEY,
    dashboard_version_id BIGINT NOT NULL REFERENCES bi_dashboard_version(id),
    widget_code         VARCHAR(100) NOT NULL,
    widget_type         VARCHAR(50) NOT NULL,
    title               VARCHAR(200) NOT NULL,
    kpi_id              BIGINT REFERENCES bi_kpi_definition(id),
    dataset_id          BIGINT REFERENCES bi_dataset(id),
    config_json         TEXT,
    position_x          INTEGER NOT NULL DEFAULT 0,
    position_y          INTEGER NOT NULL DEFAULT 0,
    width               INTEGER NOT NULL DEFAULT 4,
    height              INTEGER NOT NULL DEFAULT 3,
    is_active           BOOLEAN NOT NULL DEFAULT TRUE,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Seed initial scheduled jobs for major ETL pipelines
INSERT INTO bi_scheduled_job (job_name, job_type, schedule_cron, target_ref, priority) VALUES
('etl_finance_daily',       'ETL',          '0 2 * * *',    'stg_finance',          90),
('etl_sales_daily',         'ETL',          '0 2 * * *',    'stg_sales',            90),
('etl_inventory_daily',     'ETL',          '0 3 * * *',    'stg_inventory',        85),
('etl_payroll_monthly',     'ETL',          '0 4 1 * *',    'stg_payroll',          70),
('etl_manufacturing_daily', 'ETL',          '0 3 * * *',    'stg_manufacturing',    85),
('etl_procurement_daily',   'ETL',          '0 3 * * *',    'stg_procurement',      85),
('etl_hcm_weekly',          'ETL',          '0 4 * * 1',    'stg_hcm',              70),
('etl_grc_weekly',          'ETL',          '0 4 * * 1',    'stg_grc',              70),
('cube_refresh_nightly',    'CUBE_REFRESH',  '0 5 * * *',    'all_cubes',            80),
('maintenance_weekly',      'MAINTENANCE',  '0 1 * * 0',    'warehouse_vacuum',     50),
('snapshot_daily',          'SNAPSHOT',     '0 6 * * *',    'analytics_snapshot',   60)
ON CONFLICT (job_name) DO NOTHING;
