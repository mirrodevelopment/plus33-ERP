-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 180
-- File              : V180__seed_bi_permissions.sql
-- Operation Type    : Seed Data / Permission Grant
-- Purpose           : seed bi permissions
--
-- Tables Created    : N/A
-- Tables Altered    : N/A
-- Seed Data For     : permissions
-- Indexes           : N/A
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
-- V180: BI Permissions Seed
-- Purpose: Seeds system permissions for all BI analytical roles in the
--          main permissions table used throughout the ERP platform.

INSERT INTO permissions (code, name, description)
SELECT p.code, p.name, p.description
FROM (VALUES
    ('bi:dashboard:view',           'View executive dashboards',                            'View executive dashboards'),
    ('bi:dashboard:manage',         'Create and manage dashboard definitions',               'Create and manage dashboard definitions'),
    ('bi:dashboard:publish',        'Publish dashboard versions',                           'Publish dashboard versions'),
    ('bi:kpi:view',                 'View KPI definitions and current values',              'View KPI definitions and current values'),
    ('bi:kpi:manage',               'Create and edit KPI definitions',                      'Create and edit KPI definitions'),
    ('bi:kpi:approve',              'Approve KPI formula changes',                          'Approve KPI formula changes'),
    ('bi:dataset:view',             'View registered analytical datasets',                  'View registered analytical datasets'),
    ('bi:dataset:manage',           'Create and manage dataset definitions',                'Create and manage dataset definitions'),
    ('bi:olap:query',               'Execute OLAP queries and slice/dice operations',       'Execute OLAP queries and slice/dice operations'),
    ('bi:olap:drill',               'Drill-through OLAP cube to detail records',            'Drill-through OLAP cube to detail records'),
    ('bi:export:pdf',               'Export reports to PDF format',                         'Export reports to PDF format'),
    ('bi:export:excel',             'Export data to Excel format',                          'Export data to Excel format'),
    ('bi:export:csv',               'Export data to CSV format',                            'Export data to CSV format'),
    ('bi:forecast:view',            'View forecast predictions and models',                 'View forecast predictions and models'),
    ('bi:forecast:manage',          'Manage forecast models and run forecasts',             'Manage forecast models and run forecasts'),
    ('bi:etl:view',                 'View ETL job status and logs',                         'View ETL job status and logs'),
    ('bi:etl:manage',               'Manage and trigger ETL pipelines',                     'Manage and trigger ETL pipelines'),
    ('bi:quality:view',             'View data quality rules and results',                  'View data quality rules and results'),
    ('bi:quality:manage',           'Create and manage data quality rules',                 'Create and manage data quality rules'),
    ('bi:lineage:view',             'View data lineage and provenance',                     'View data lineage and provenance'),
    ('bi:maintenance:view',         'View warehouse maintenance logs',                      'View warehouse maintenance logs'),
    ('bi:maintenance:manage',       'Trigger warehouse maintenance operations',             'Trigger warehouse maintenance operations'),
    ('bi:alert:view',               'View alert rules and triggered alerts',                'View alert rules and triggered alerts'),
    ('bi:alert:manage',             'Create and manage BI alert rules',                     'Create and manage BI alert rules'),
    ('bi:security:manage',          'Manage BI roles and permissions',                      'Manage BI roles and permissions'),
    ('bi:system:health',            'View BI system health dashboard',                      'View BI system health dashboard'),
    ('bi:snapshot:view',            'View historical analytical snapshots',                 'View historical analytical snapshots'),
    ('bi:semantic:view',            'View semantic models and business views',              'View semantic models and business views'),
    ('bi:semantic:manage',          'Create and manage semantic models',                    'Create and manage semantic models'),
    ('bi:cache:manage',             'Manage dashboard cache invalidation',                  'Manage dashboard cache invalidation')
) AS p(code, name, description)
ON CONFLICT (code) DO NOTHING;