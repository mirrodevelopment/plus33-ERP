-- V177: KPI Catalog Seed Data
-- Purpose: Seed the standard enterprise executive KPI definitions, their
--          formula expressions, and dependency graph edges.

-- Seed standard executive KPIs
INSERT INTO bi_kpi_definition (kpi_code, kpi_name, kpi_category, description, unit, direction, status) VALUES
('REVENUE',             'Total Revenue',                'FINANCE',      'Gross revenue from all sales',                     'CURRENCY', 'HIGHER',   'ACTIVE'),
('GROSS_MARGIN',        'Gross Margin',                 'FINANCE',      'Revenue minus cost of goods sold',                 'CURRENCY', 'HIGHER',   'ACTIVE'),
('GROSS_MARGIN_PCT',    'Gross Margin %',               'FINANCE',      'Gross margin as percentage of revenue',            'PERCENT',  'HIGHER',   'ACTIVE'),
('NET_PROFIT',          'Net Profit',                   'FINANCE',      'Revenue minus all expenses',                       'CURRENCY', 'HIGHER',   'ACTIVE'),
('EBITDA',              'EBITDA',                       'FINANCE',      'Earnings before interest taxes depreciation',      'CURRENCY', 'HIGHER',   'ACTIVE'),
('OPERATING_EXPENSE',   'Operating Expenses',           'FINANCE',      'Total operating cost',                             'CURRENCY', 'LOWER',    'ACTIVE'),
('PAYROLL_COST',        'Total Payroll Cost',           'WORKFORCE',    'Total gross payroll including employer cost',      'CURRENCY', 'LOWER',    'ACTIVE'),
('HEADCOUNT',           'Active Headcount',             'WORKFORCE',    'Total active employee count',                      'COUNT',    'NEUTRAL',  'ACTIVE'),
('ATTRITION_RATE',      'Attrition Rate',               'WORKFORCE',    'Employee attrition as percentage of headcount',   'PERCENT',  'LOWER',    'ACTIVE'),
('PROCUREMENT_SPEND',   'Total Procurement Spend',      'PROCUREMENT',  'Total supplier purchase spend',                   'CURRENCY', 'LOWER',    'ACTIVE'),
('SUPPLIER_OTD',        'Supplier On-Time Delivery',    'PROCUREMENT',  'Percentage of orders delivered on time',          'PERCENT',  'HIGHER',   'ACTIVE'),
('INVENTORY_TURNS',     'Inventory Turnover',           'INVENTORY',    'Cost of goods sold divided by avg inventory',     'RATIO',    'HIGHER',   'ACTIVE'),
('OEE',                 'Overall Equipment Effectiveness','MANUFACTURING','Actual vs planned production ratio',            'PERCENT',  'HIGHER',   'ACTIVE'),
('SCRAP_RATE',          'Scrap Rate',                   'MANUFACTURING','Scrap quantity as percentage of total output',    'PERCENT',  'LOWER',    'ACTIVE'),
('PIPELINE_VALUE',      'CRM Pipeline Value',           'SALES',        'Total weighted opportunity value in pipeline',    'CURRENCY', 'HIGHER',   'ACTIVE'),
('WIN_RATE',            'Deal Win Rate',                'SALES',        'Closed won deals as percentage of total deals',   'PERCENT',  'HIGHER',   'ACTIVE'),
('PROJECT_MARGIN',      'Project Gross Margin',         'PROJECTS',     'Revenue minus project direct costs',              'CURRENCY', 'HIGHER',   'ACTIVE'),
('RESIDUAL_RISK_SCORE', 'Avg Residual Risk Score',      'GRC',          'Average residual risk score across all risks',    'SCORE',    'LOWER',    'ACTIVE'),
('COMPLIANCE_RATE',     'Compliance Rate',              'GRC',          'Percentage of controls tested and passed',        'PERCENT',  'HIGHER',   'ACTIVE'),
('CASH_POSITION',       'Net Cash Position',            'TREASURY',     'Total liquid cash and equivalents',               'CURRENCY', 'HIGHER',   'ACTIVE')
ON CONFLICT (kpi_code) DO NOTHING;

-- Seed formula versions for key KPIs
INSERT INTO bi_kpi_formula_version (kpi_id, version_number, formula_expression, compiled_expression, effective_from, is_current, published_by)
SELECT k.id, 1, f.expr, f.compiled, CURRENT_DATE, TRUE, 'system'
FROM bi_kpi_definition k
JOIN (VALUES
    ('REVENUE',          'SUM(fact_sales.gross_amount)',                                     'SUM(gross_amount)'),
    ('GROSS_MARGIN',     'SUM(fact_sales.gross_margin)',                                     'SUM(gross_margin)'),
    ('GROSS_MARGIN_PCT', '(GROSS_MARGIN / REVENUE) * 100',                                  '(gross_margin_sum / revenue_sum) * 100'),
    ('NET_PROFIT',       'REVENUE - OPERATING_EXPENSE',                                     'revenue - operating_expense'),
    ('PAYROLL_COST',     'SUM(fact_payroll.gross_salary) + SUM(fact_payroll.employer_cost)','SUM(gross_salary + employer_cost)'),
    ('HEADCOUNT',        'SUM(fact_hcm.headcount)',                                         'SUM(headcount)'),
    ('ATTRITION_RATE',   '(SUM(fact_hcm.attrition_count) / SUM(fact_hcm.headcount)) * 100','(attrition_count / headcount) * 100'),
    ('OEE',              '(SUM(fact_manufacturing.actual_qty) / SUM(fact_manufacturing.planned_qty)) * 100', '(actual_qty / planned_qty) * 100'),
    ('INVENTORY_TURNS',  'SUM(fact_inventory.quantity_out) / AVG(fact_inventory.closing_stock)', 'quantity_out / avg_closing_stock')
) AS f(code, expr, compiled) ON k.kpi_code = f.code
ON CONFLICT (kpi_id, version_number) DO NOTHING;

-- Seed KPI dependency graph
INSERT INTO bi_kpi_dependency (kpi_id, depends_on_kpi_id, dependency_type)
SELECT k.id, d.id, 'DIRECT'
FROM bi_kpi_definition k
JOIN bi_kpi_definition d ON 1=1
WHERE (k.kpi_code = 'GROSS_MARGIN_PCT' AND d.kpi_code = 'GROSS_MARGIN')
   OR (k.kpi_code = 'GROSS_MARGIN_PCT' AND d.kpi_code = 'REVENUE')
   OR (k.kpi_code = 'NET_PROFIT'       AND d.kpi_code = 'REVENUE')
   OR (k.kpi_code = 'NET_PROFIT'       AND d.kpi_code = 'OPERATING_EXPENSE')
   OR (k.kpi_code = 'ATTRITION_RATE'   AND d.kpi_code = 'HEADCOUNT')
ON CONFLICT (kpi_id, depends_on_kpi_id) DO NOTHING;
