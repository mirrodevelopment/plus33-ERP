-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 175
-- File              : V175__create_dashboard_security.sql
-- Operation Type    : Schema Creation
-- Purpose           : create dashboard security
--
-- Tables Created    : IF, IF, IF, IF, IF, IF, IF
-- Tables Altered    : N/A
-- Seed Data For     : bi_analytics_role, bi_export_permission, bi_feature_flag
-- Indexes           : N/A
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
-- V175: BI Dashboard Security
-- Purpose: Row-level access filters, dataset permissions, KPI permissions,
--          export permissions, and cube/drill-through permissions.

-- -----------------------------------------------------------------------------
-- DASHBOARD ROLE: analytical role definitions
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS bi_analytics_role (
    id                  BIGSERIAL PRIMARY KEY,
    role_code           VARCHAR(100) NOT NULL UNIQUE,
    role_name           VARCHAR(200) NOT NULL,
    description         TEXT,
    is_active           BOOLEAN NOT NULL DEFAULT TRUE,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- -----------------------------------------------------------------------------
-- ROW LEVEL FILTER: company/department/region isolation per role
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS bi_row_level_filter (
    id                  BIGSERIAL PRIMARY KEY,
    role_id             BIGINT NOT NULL REFERENCES bi_analytics_role(id),
    filter_dimension    VARCHAR(50) NOT NULL,
    filter_operator     VARCHAR(20) NOT NULL DEFAULT 'IN',
    filter_values       TEXT NOT NULL,
    is_active           BOOLEAN NOT NULL DEFAULT TRUE,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- -----------------------------------------------------------------------------
-- DATASET PERMISSION: which roles can access which datasets
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS bi_dataset_permission (
    id                  BIGSERIAL PRIMARY KEY,
    role_id             BIGINT NOT NULL REFERENCES bi_analytics_role(id),
    dataset_id          BIGINT NOT NULL REFERENCES bi_dataset(id),
    can_read            BOOLEAN NOT NULL DEFAULT TRUE,
    can_export          BOOLEAN NOT NULL DEFAULT FALSE,
    can_drill           BOOLEAN NOT NULL DEFAULT FALSE,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_dataset_permission UNIQUE (role_id, dataset_id)
);

-- -----------------------------------------------------------------------------
-- KPI PERMISSION: which roles can view/edit which KPIs
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS bi_kpi_permission (
    id                  BIGSERIAL PRIMARY KEY,
    role_id             BIGINT NOT NULL REFERENCES bi_analytics_role(id),
    kpi_id              BIGINT NOT NULL REFERENCES bi_kpi_definition(id),
    can_view            BOOLEAN NOT NULL DEFAULT TRUE,
    can_edit            BOOLEAN NOT NULL DEFAULT FALSE,
    can_approve         BOOLEAN NOT NULL DEFAULT FALSE,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_kpi_permission UNIQUE (role_id, kpi_id)
);

-- -----------------------------------------------------------------------------
-- CUBE PERMISSION: OLAP cube access controls
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS bi_cube_permission (
    id                  BIGSERIAL PRIMARY KEY,
    role_id             BIGINT NOT NULL REFERENCES bi_analytics_role(id),
    cube_name           VARCHAR(200) NOT NULL,
    can_query           BOOLEAN NOT NULL DEFAULT TRUE,
    can_drill_through   BOOLEAN NOT NULL DEFAULT FALSE,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_cube_permission UNIQUE (role_id, cube_name)
);

-- -----------------------------------------------------------------------------
-- EXPORT PERMISSION: which roles can export which formats
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS bi_export_permission (
    id                  BIGSERIAL PRIMARY KEY,
    role_id             BIGINT NOT NULL REFERENCES bi_analytics_role(id),
    export_format       VARCHAR(20) NOT NULL,
    is_allowed          BOOLEAN NOT NULL DEFAULT TRUE,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_export_permission UNIQUE (role_id, export_format)
);

-- -----------------------------------------------------------------------------
-- BI FEATURE FLAG: toggle analytical capabilities
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS bi_feature_flag (
    id                  BIGSERIAL PRIMARY KEY,
    feature_code        VARCHAR(100) NOT NULL UNIQUE,
    feature_name        VARCHAR(200) NOT NULL,
    is_enabled          BOOLEAN NOT NULL DEFAULT TRUE,
    updated_by          VARCHAR(100),
    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Seed standard analytics roles
INSERT INTO bi_analytics_role (role_code, role_name, description) VALUES
('BI_EXECUTIVE',        'Executive',            'Full access to all dashboards and KPIs'),
('BI_ANALYST',          'BI Analyst',           'Read access to datasets and cube drill-through'),
('BI_MANAGER',          'Department Manager',   'Filtered access scoped to own department'),
('BI_VIEWER',           'Report Viewer',        'Read-only access to published dashboards'),
('BI_DATA_STEWARD',     'Data Steward',         'Access to data quality and lineage tools'),
('BI_ADMIN',            'BI Administrator',     'Full platform administration access')
ON CONFLICT (role_code) DO NOTHING;

-- Seed feature flags
INSERT INTO bi_feature_flag (feature_code, feature_name, is_enabled) VALUES
('FORECASTING',         'Revenue & Demand Forecasting',     TRUE),
('AI_INSIGHTS',         'AI-Driven Narrative Insights',     TRUE),
('OLAP_EXPORT',         'OLAP Cube Data Export',            TRUE),
('CUBE_REFRESH',        'Materialized Cube Refresh',        TRUE),
('SELF_SERVICE',        'Self-Service Analytics',           TRUE),
('DATA_MASKING',        'Dynamic PII Data Masking',         TRUE),
('MDM',                 'Master Data Management',           TRUE),
('DASHBOARD_SHARE',     'Dashboard Sharing & Subscriptions',TRUE)
ON CONFLICT (feature_code) DO NOTHING;

-- Seed export permissions for executive role
INSERT INTO bi_export_permission (role_id, export_format, is_allowed)
SELECT r.id, f.fmt, TRUE FROM bi_analytics_role r
CROSS JOIN (VALUES ('PDF'),('EXCEL'),('CSV'),('PPTX')) AS f(fmt)
WHERE r.role_code = 'BI_EXECUTIVE'
ON CONFLICT (role_id, export_format) DO NOTHING;
