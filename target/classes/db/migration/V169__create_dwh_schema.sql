-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 169
-- File              : V169__create_dwh_schema.sql
-- Operation Type    : Schema Creation
-- Purpose           : create dwh schema
--
-- Tables Created    : IF, IF, IF, IF, IF, IF, IF, IF, IF, IF
-- Tables Altered    : N/A
-- Seed Data For     : bi_configuration, bi_warehouse_version
-- Indexes           : N/A
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
-- V169: Enterprise Data Warehouse Core Schema
-- Purpose: Core DWH infrastructure: warehouse versioning, build manifests,
--          ETL job state machine, job dependency graph, data source registry,
--          fact load auditing, and warehouse capacity forecasting.

-- -----------------------------------------------------------------------------
-- WAREHOUSE VERSION: schema checksum per build
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS bi_warehouse_version (
    id                  BIGSERIAL PRIMARY KEY,
    warehouse_version   VARCHAR(50) NOT NULL UNIQUE,
    migration_version   VARCHAR(50) NOT NULL,
    schema_checksum     VARCHAR(200),
    generated_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    generated_by        VARCHAR(100) NOT NULL DEFAULT 'system'
);

-- -----------------------------------------------------------------------------
-- WAREHOUSE BUILD MANIFEST: full audit of each analytical build
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS bi_warehouse_build_manifest (
    id                  BIGSERIAL PRIMARY KEY,
    warehouse_version   VARCHAR(50) NOT NULL,
    migration_range     VARCHAR(100),
    etl_version         VARCHAR(50),
    cube_version        VARCHAR(50),
    build_timestamp     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    build_duration_ms   BIGINT,
    build_status        VARCHAR(30) NOT NULL DEFAULT 'IN_PROGRESS',
    checksum            VARCHAR(200),
    notes               TEXT
);

-- -----------------------------------------------------------------------------
-- DATA SOURCE REGISTRY: configurable extraction source catalog
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS bi_data_source_registry (
    id                  BIGSERIAL PRIMARY KEY,
    source_name         VARCHAR(100) NOT NULL UNIQUE,
    module              VARCHAR(100) NOT NULL,
    table_name          VARCHAR(100) NOT NULL,
    event_store         VARCHAR(100),
    refresh_mode        VARCHAR(30) NOT NULL DEFAULT 'INCREMENTAL',
    priority            INTEGER NOT NULL DEFAULT 50,
    enabled             BOOLEAN NOT NULL DEFAULT TRUE,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- -----------------------------------------------------------------------------
-- ETL JOB: formal job definitions with state machine lifecycle
-- States: CREATED ? SCHEDULED ? QUEUED ? EXTRACTING ? VALIDATING ?
--         TRANSFORMING ? LOADING ? REFRESHING_CUBES ? COMPLETED
-- Exception: FAILED | CANCELLED | PAUSED | RETRYING
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS bi_etl_job (
    id                  BIGSERIAL PRIMARY KEY,
    job_name            VARCHAR(200) NOT NULL UNIQUE,
    job_type            VARCHAR(50) NOT NULL DEFAULT 'ETL',
    source_module       VARCHAR(100) NOT NULL,
    source_table        VARCHAR(100),
    target_table        VARCHAR(100),
    schedule_cron       VARCHAR(100),
    status              VARCHAR(30) NOT NULL DEFAULT 'CREATED',
    priority            INTEGER NOT NULL DEFAULT 50,
    max_retries         INTEGER NOT NULL DEFAULT 3,
    timeout_minutes     INTEGER NOT NULL DEFAULT 60,
    enabled             BOOLEAN NOT NULL DEFAULT TRUE,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- -----------------------------------------------------------------------------
-- ETL JOB RUN: persists each execution instance
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS bi_etl_job_run (
    id                  BIGSERIAL PRIMARY KEY,
    job_id              BIGINT NOT NULL REFERENCES bi_etl_job(id),
    run_number          INTEGER NOT NULL DEFAULT 1,
    status              VARCHAR(30) NOT NULL DEFAULT 'CREATED',
    started_at          TIMESTAMP,
    completed_at        TIMESTAMP,
    duration_ms         BIGINT,
    records_extracted   INTEGER DEFAULT 0,
    records_processed   INTEGER DEFAULT 0,
    records_rejected    INTEGER DEFAULT 0,
    watermark_from      BIGINT,
    watermark_to        BIGINT,
    batch_id            VARCHAR(100),
    error_message       TEXT,
    retry_count         INTEGER NOT NULL DEFAULT 0,
    triggered_by        VARCHAR(100),
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- -----------------------------------------------------------------------------
-- ETL JOB DEPENDENCY: DAG for pipeline execution ordering
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS bi_etl_job_dependency (
    id                  BIGSERIAL PRIMARY KEY,
    parent_job_id       BIGINT NOT NULL REFERENCES bi_etl_job(id),
    child_job_id        BIGINT NOT NULL REFERENCES bi_etl_job(id),
    dependency_type     VARCHAR(20) NOT NULL DEFAULT 'MANDATORY',
    execution_order     INTEGER NOT NULL DEFAULT 1,
    CONSTRAINT uk_etl_dependency UNIQUE (parent_job_id, child_job_id)
);

-- -----------------------------------------------------------------------------
-- FACT LOAD AUDIT: row-level reconciliation stats per fact table load
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS bi_fact_load_audit (
    id                  BIGSERIAL PRIMARY KEY,
    job_run_id          BIGINT REFERENCES bi_etl_job_run(id),
    fact_table          VARCHAR(100) NOT NULL,
    batch_id            VARCHAR(100),
    rows_inserted       INTEGER DEFAULT 0,
    rows_updated        INTEGER DEFAULT 0,
    rows_rejected       INTEGER DEFAULT 0,
    load_duration_ms    BIGINT,
    checksum            VARCHAR(200),
    status              VARCHAR(30) NOT NULL DEFAULT 'COMPLETED',
    loaded_at           TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- -----------------------------------------------------------------------------
-- BI CONFIGURATION REGISTRY: runtime tuning parameters
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS bi_configuration (
    id                  BIGSERIAL PRIMARY KEY,
    config_key          VARCHAR(100) NOT NULL UNIQUE,
    config_value        VARCHAR(500) NOT NULL,
    config_group        VARCHAR(50) NOT NULL DEFAULT 'SYSTEM',
    description         TEXT,
    is_encrypted        BOOLEAN NOT NULL DEFAULT FALSE,
    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_by          VARCHAR(100)
);

-- -----------------------------------------------------------------------------
-- WAREHOUSE CAPACITY FORECAST: storage planning metrics
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS bi_warehouse_capacity_forecast (
    id                          BIGSERIAL PRIMARY KEY,
    snapshot_date               DATE NOT NULL,
    current_storage_gb          NUMERIC(12,3) DEFAULT 0,
    growth_rate_gb_per_month    NUMERIC(10,3) DEFAULT 0,
    estimated_exhaustion_date   DATE,
    partition_count             INTEGER DEFAULT 0,
    partition_recommendations   TEXT,
    created_at                  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- -----------------------------------------------------------------------------
-- BI OPERATIONAL METRICS: system performance telemetry
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS bi_operational_metrics (
    id                      BIGSERIAL PRIMARY KEY,
    metric_date             DATE NOT NULL,
    etl_throughput_rps      NUMERIC(10,2) DEFAULT 0,
    cube_refresh_duration_s NUMERIC(10,2) DEFAULT 0,
    dashboard_latency_ms    NUMERIC(10,2) DEFAULT 0,
    cache_hit_ratio         NUMERIC(5,2) DEFAULT 0,
    warehouse_growth_gb     NUMERIC(12,3) DEFAULT 0,
    partition_count         INTEGER DEFAULT 0,
    forecast_exec_time_s    NUMERIC(10,2) DEFAULT 0,
    recorded_at             TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- -----------------------------------------------------------------------------
-- DEFAULT CONFIGURATION SEEDS
-- -----------------------------------------------------------------------------
INSERT INTO bi_configuration (config_key, config_value, config_group, description) VALUES
('CDC_BATCH_SIZE',          '5000',         'ETL',      'Max records per CDC extraction batch'),
('MAX_ETL_THREADS',         '4',            'ETL',      'Maximum concurrent ETL execution threads'),
('DEFAULT_PARTITION_SIZE',  'MONTHLY',      'WAREHOUSE','Default time partition granularity'),
('DEFAULT_REFRESH_MODE',    'INCREMENTAL',  'CUBE',     'Default cube refresh strategy'),
('DEFAULT_RETENTION_DAYS',  '1825',         'WAREHOUSE','Warehouse data retention in days (5 years)'),
('MAX_RETRY_COUNT',         '3',            'ETL',      'Maximum ETL job retry attempts'),
('DEFAULT_FORECAST_MODEL',  'LINEAR',       'FORECAST', 'Default forecasting model type'),
('DEFAULT_KPI_PRECISION',   '4',            'KPI',      'Decimal precision for KPI calculations'),
('STAGING_RETENTION_DAYS',  '30',           'STAGING',  'Staging table retention in days'),
('ETL_LOG_RETENTION_DAYS',  '180',          'ETL',      'ETL log retention in days')
ON CONFLICT (config_key) DO NOTHING;

-- Initial warehouse version seed
INSERT INTO bi_warehouse_version (warehouse_version, migration_version, schema_checksum, generated_by)
VALUES ('40.0.0', 'V169', 'v40-initial', 'migration')
ON CONFLICT (warehouse_version) DO NOTHING;
