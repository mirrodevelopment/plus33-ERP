-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 179
-- File              : V179__create_analytics_snapshots.sql
-- Operation Type    : Schema Creation
-- Purpose           : create analytics snapshots
--
-- Tables Created    : IF, IF, IF, IF, IF, IF
-- Tables Altered    : N/A
-- Seed Data For     : dim_currency, dim_date
-- Indexes           : N/A
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
-- V179: Analytics Snapshots
-- Purpose: Periodic analytical metric snapshots for trending and historical replay.

-- -----------------------------------------------------------------------------
-- ANALYTICS SNAPSHOT: time-series KPI snapshots per company
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS bi_analytics_snapshot (
    id                  BIGSERIAL PRIMARY KEY,
    company_id          BIGINT NOT NULL,
    snapshot_date       DATE NOT NULL,
    snapshot_period     VARCHAR(20) NOT NULL DEFAULT 'MONTHLY',
    kpi_code            VARCHAR(100) NOT NULL,
    kpi_value           NUMERIC(19,4),
    kpi_unit            VARCHAR(50),
    dimension_filters   TEXT,
    source_job_run_id   BIGINT REFERENCES bi_etl_job_run(id),
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_analytics_snapshot UNIQUE (company_id, snapshot_date, snapshot_period, kpi_code)
);

-- -----------------------------------------------------------------------------
-- DASHBOARD USAGE: executive dashboard access tracking
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS bi_dashboard_usage (
    id                  BIGSERIAL PRIMARY KEY,
    company_id          BIGINT,
    dashboard_id        BIGINT REFERENCES bi_dashboard_definition(id),
    user_name           VARCHAR(100) NOT NULL,
    session_id          VARCHAR(100),
    view_count          INTEGER NOT NULL DEFAULT 1,
    duration_seconds    INTEGER,
    accessed_at         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS bi_dashboard_favorite (
    id                  BIGSERIAL PRIMARY KEY,
    company_id          BIGINT,
    dashboard_id        BIGINT NOT NULL REFERENCES bi_dashboard_definition(id),
    user_name           VARCHAR(100) NOT NULL,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_dashboard_favorite UNIQUE (company_id, dashboard_id, user_name)
);

-- -----------------------------------------------------------------------------
-- WAREHOUSE MAINTENANCE LOG: partition, vacuum, and reindex execution log
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS bi_maintenance_log (
    id                  BIGSERIAL PRIMARY KEY,
    operation_type      VARCHAR(50) NOT NULL,
    target_table        VARCHAR(100),
    target_partition    VARCHAR(100),
    started_at          TIMESTAMP NOT NULL,
    completed_at        TIMESTAMP,
    duration_ms         BIGINT,
    rows_affected       BIGINT,
    status              VARCHAR(20) NOT NULL DEFAULT 'COMPLETED',
    error_message       TEXT,
    executed_by         VARCHAR(100) NOT NULL DEFAULT 'scheduler',
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- -----------------------------------------------------------------------------
-- BUSINESS CALENDAR: fiscal, holiday, manufacturing, payroll calendars
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS bi_business_calendar (
    id                  BIGSERIAL PRIMARY KEY,
    company_id          BIGINT NOT NULL,
    calendar_type       VARCHAR(50) NOT NULL,
    calendar_date       DATE NOT NULL,
    is_working_day      BOOLEAN NOT NULL DEFAULT TRUE,
    is_holiday          BOOLEAN NOT NULL DEFAULT FALSE,
    holiday_name        VARCHAR(200),
    fiscal_year         INTEGER,
    fiscal_period       INTEGER,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_business_calendar UNIQUE (company_id, calendar_type, calendar_date)
);

-- -----------------------------------------------------------------------------
-- BI SYSTEM HEALTH: warehouse health status snapshot
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS bi_system_health (
    id                  BIGSERIAL PRIMARY KEY,
    check_timestamp     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    running_jobs        INTEGER NOT NULL DEFAULT 0,
    failed_jobs_24h     INTEGER NOT NULL DEFAULT 0,
    queue_depth         INTEGER NOT NULL DEFAULT 0,
    stale_cubes         INTEGER NOT NULL DEFAULT 0,
    cache_hit_ratio     NUMERIC(5,2) DEFAULT 0,
    warehouse_size_gb   NUMERIC(12,3) DEFAULT 0,
    partition_count     INTEGER DEFAULT 0,
    overall_status      VARCHAR(20) NOT NULL DEFAULT 'HEALTHY'
);

-- Pre-populate dim_date for 5 years (2020�2027) using a sequence
-- We use a series so tests can rely on date dimension being available
INSERT INTO dim_date (date_key, full_date, day_of_week, day_name, day_of_month, day_of_year,
                      week_of_year, month_number, month_name, quarter_number, quarter_name,
                      year_number, fiscal_year, fiscal_quarter, fiscal_month,
                      is_weekend, is_holiday, is_working_day)
SELECT
    TO_CHAR(d, 'YYYYMMDD')::INTEGER,
    d,
    EXTRACT(DOW FROM d)::INTEGER,
    TO_CHAR(d, 'Day'),
    EXTRACT(DAY FROM d)::INTEGER,
    EXTRACT(DOY FROM d)::INTEGER,
    EXTRACT(WEEK FROM d)::INTEGER,
    EXTRACT(MONTH FROM d)::INTEGER,
    TO_CHAR(d, 'Month'),
    EXTRACT(QUARTER FROM d)::INTEGER,
    'Q' || EXTRACT(QUARTER FROM d)::INTEGER,
    EXTRACT(YEAR FROM d)::INTEGER,
    EXTRACT(YEAR FROM d)::INTEGER,
    EXTRACT(QUARTER FROM d)::INTEGER,
    EXTRACT(MONTH FROM d)::INTEGER,
    EXTRACT(DOW FROM d) IN (0, 6),
    FALSE,
    EXTRACT(DOW FROM d) NOT IN (0, 6)
FROM GENERATE_SERIES('2020-01-01'::DATE, '2027-12-31'::DATE, '1 day'::INTERVAL) AS d
ON CONFLICT (date_key) DO NOTHING;

-- Insert USD as base currency
INSERT INTO dim_currency (currency_code, currency_name, rate_to_usd, rate_date, is_base_currency)
VALUES ('USD', 'US Dollar', 1.0, CURRENT_DATE, TRUE)
ON CONFLICT (currency_code, rate_date) DO NOTHING;
