-- 1. Create Organizational Dimensions
CREATE TABLE departments (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    code VARCHAR(50) NOT NULL,
    name VARCHAR(150) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    
    CONSTRAINT fk_departments_company FOREIGN KEY (company_id) REFERENCES companies(id),
    CONSTRAINT uk_departments_company_code UNIQUE (company_id, code)
);

CREATE TABLE cost_centers (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    code VARCHAR(50) NOT NULL,
    name VARCHAR(150) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    
    CONSTRAINT fk_cost_centers_company FOREIGN KEY (company_id) REFERENCES companies(id),
    CONSTRAINT uk_cost_centers_company_code UNIQUE (company_id, code)
);

CREATE TABLE projects (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    code VARCHAR(50) NOT NULL,
    name VARCHAR(150) NOT NULL,
    start_date DATE,
    end_date DATE,
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE', -- ACTIVE, COMPLETED, SUSPENDED
    active BOOLEAN NOT NULL DEFAULT TRUE,
    
    CONSTRAINT fk_projects_company FOREIGN KEY (company_id) REFERENCES companies(id),
    CONSTRAINT uk_projects_company_code UNIQUE (company_id, code)
);

-- 2. Create Reusable Dimension Set Table
CREATE TABLE budget_dimension_sets (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    department_id BIGINT,
    cost_center_id BIGINT,
    project_id BIGINT,
    warehouse_id BIGINT,
    asset_category_id BIGINT,
    region_id BIGINT,
    store_id BIGINT,

    CONSTRAINT fk_dim_sets_company FOREIGN KEY (company_id) REFERENCES companies(id),
    CONSTRAINT fk_dim_sets_dept FOREIGN KEY (department_id) REFERENCES departments(id),
    CONSTRAINT fk_dim_sets_cc FOREIGN KEY (cost_center_id) REFERENCES cost_centers(id),
    CONSTRAINT fk_dim_sets_proj FOREIGN KEY (project_id) REFERENCES projects(id),
    CONSTRAINT fk_dim_sets_wh FOREIGN KEY (warehouse_id) REFERENCES warehouses(id),
    CONSTRAINT fk_dim_sets_ac FOREIGN KEY (asset_category_id) REFERENCES asset_categories(id),
    CONSTRAINT fk_dim_sets_region FOREIGN KEY (region_id) REFERENCES regions(id),
    CONSTRAINT fk_dim_sets_store FOREIGN KEY (store_id) REFERENCES stores(id)
);

CREATE UNIQUE INDEX uk_dim_sets_combination ON budget_dimension_sets (
    company_id,
    COALESCE(department_id, 0),
    COALESCE(cost_center_id, 0),
    COALESCE(project_id, 0),
    COALESCE(warehouse_id, 0),
    COALESCE(asset_category_id, 0),
    COALESCE(region_id, 0),
    COALESCE(store_id, 0)
);

-- 3. Link Dimension Sets to Transaction Tables
ALTER TABLE purchase_request_items ADD COLUMN dimension_set_id BIGINT;
ALTER TABLE purchase_request_items ADD CONSTRAINT fk_pri_dim_set FOREIGN KEY (dimension_set_id) REFERENCES budget_dimension_sets(id);

ALTER TABLE purchase_order_items ADD COLUMN dimension_set_id BIGINT;
ALTER TABLE purchase_order_items ADD CONSTRAINT fk_poi_dim_set FOREIGN KEY (dimension_set_id) REFERENCES budget_dimension_sets(id);

ALTER TABLE supplier_invoice_items ADD COLUMN dimension_set_id BIGINT;
ALTER TABLE supplier_invoice_items ADD CONSTRAINT fk_sii_dim_set FOREIGN KEY (dimension_set_id) REFERENCES budget_dimension_sets(id);

ALTER TABLE journal_entry_lines ADD COLUMN dimension_set_id BIGINT;
ALTER TABLE journal_entry_lines ADD CONSTRAINT fk_jel_dim_set FOREIGN KEY (dimension_set_id) REFERENCES budget_dimension_sets(id);

-- 4. Workflow Configurations, Budget Policies, and Templates
CREATE TABLE budget_workflow_templates (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    code VARCHAR(50) NOT NULL,
    name VARCHAR(150) NOT NULL,
    active BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT fk_wf_templates_company FOREIGN KEY (company_id) REFERENCES companies(id),
    CONSTRAINT uk_wf_templates_company_code UNIQUE (company_id, code)
);

CREATE TABLE budget_workflow_steps (
    id BIGSERIAL PRIMARY KEY,
    template_id BIGINT NOT NULL,
    step_sequence INTEGER NOT NULL,
    role_code VARCHAR(50) NOT NULL,
    min_amount DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    active BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT fk_wf_steps_template FOREIGN KEY (template_id) REFERENCES budget_workflow_templates(id) ON DELETE CASCADE,
    CONSTRAINT uk_wf_steps_seq UNIQUE (template_id, step_sequence)
);

CREATE TABLE budget_policies (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    code VARCHAR(50) NOT NULL,
    name VARCHAR(150) NOT NULL,
    control_type VARCHAR(30) NOT NULL DEFAULT 'HARD', -- HARD, SOFT, NONE
    allow_negative BOOLEAN NOT NULL DEFAULT FALSE,
    allow_transfers BOOLEAN NOT NULL DEFAULT TRUE,
    allow_revisions BOOLEAN NOT NULL DEFAULT TRUE,
    auto_reserve BOOLEAN NOT NULL DEFAULT TRUE,
    auto_consume BOOLEAN NOT NULL DEFAULT TRUE,
    approval_required BOOLEAN NOT NULL DEFAULT TRUE,
    multi_currency_enabled BOOLEAN NOT NULL DEFAULT FALSE,
    active BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT fk_policies_company FOREIGN KEY (company_id) REFERENCES companies(id),
    CONSTRAINT uk_policies_company_code UNIQUE (company_id, code)
);

CREATE TABLE budget_templates (
    id BIGSERIAL PRIMARY KEY,
    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(150) NOT NULL,
    description VARCHAR(255),
    industry_type VARCHAR(50) NOT NULL, -- RETAIL, SERVICES, WAREHOUSE, MANUFACTURING, HEAD_OFFICE
    active BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE budget_template_lines (
    id BIGSERIAL PRIMARY KEY,
    template_id BIGINT NOT NULL,
    account_code VARCHAR(50) NOT NULL,
    dimension_type VARCHAR(50),
    distribution_percent DECIMAL(5,2) NOT NULL DEFAULT 0.00,

    CONSTRAINT fk_template_lines_temp FOREIGN KEY (template_id) REFERENCES budget_templates(id) ON DELETE CASCADE
);

-- 5. Budget Drivers & Formula Modeling
CREATE TABLE budget_drivers (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    fiscal_year_id BIGINT NOT NULL,
    code VARCHAR(50) NOT NULL,
    name VARCHAR(150) NOT NULL,
    driver_value DECIMAL(15,4) NOT NULL DEFAULT 0.0000,
    unit VARCHAR(30) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_drivers_company FOREIGN KEY (company_id) REFERENCES companies(id),
    CONSTRAINT fk_drivers_fy FOREIGN KEY (fiscal_year_id) REFERENCES fiscal_years(id),
    CONSTRAINT uk_drivers_company_fy_code UNIQUE (company_id, fiscal_year_id, code)
);

-- 6. Core Budget Master
CREATE TABLE budgets (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    fiscal_year_id BIGINT NOT NULL,
    budget_policy_id BIGINT NOT NULL,
    workflow_template_id BIGINT,
    code VARCHAR(50) NOT NULL,
    name VARCHAR(150) NOT NULL,
    budget_type VARCHAR(30) NOT NULL, -- OPERATING, CAPITAL
    period_type VARCHAR(30) NOT NULL DEFAULT 'MONTHLY', -- ANNUAL, QUARTERLY, MONTHLY, WEEKLY, CUSTOM
    scenario VARCHAR(30) NOT NULL DEFAULT 'EXPECTED', -- EXPECTED, BEST_CASE, WORST_CASE
    status VARCHAR(30) NOT NULL DEFAULT 'DRAFT', -- DRAFT, SUBMITTED, REVIEWED, APPROVED, LOCKED, PARTIALLY_LOCKED, ARCHIVED
    version_number INTEGER NOT NULL DEFAULT 1,
    is_forecast BOOLEAN NOT NULL DEFAULT FALSE,
    forecast_type VARCHAR(30),
    forecast_cycle_code VARCHAR(50),
    is_frozen BOOLEAN NOT NULL DEFAULT FALSE,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    
    rate_lock_type VARCHAR(30) NOT NULL DEFAULT 'SPOT', -- HISTORICAL, BUDGET_RATE, MONTHLY_AVERAGE, SPOT
    budget_exchange_rate DECIMAL(18,6) DEFAULT 1.000000,

    created_by VARCHAR(100) NOT NULL,
    approved_by VARCHAR(100),
    approved_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_budgets_company FOREIGN KEY (company_id) REFERENCES companies(id),
    CONSTRAINT fk_budgets_fy FOREIGN KEY (fiscal_year_id) REFERENCES fiscal_years(id),
    CONSTRAINT fk_budgets_policy FOREIGN KEY (budget_policy_id) REFERENCES budget_policies(id),
    CONSTRAINT fk_budgets_wf FOREIGN KEY (workflow_template_id) REFERENCES budget_workflow_templates(id),
    CONSTRAINT uk_budgets_company_fy_code UNIQUE (company_id, fiscal_year_id, code, is_forecast)
);

CREATE TABLE budget_versions (
    id BIGSERIAL PRIMARY KEY,
    budget_id BIGINT NOT NULL,
    version_code VARCHAR(30) NOT NULL,
    description VARCHAR(255),
    status VARCHAR(30) NOT NULL DEFAULT 'DRAFT',
    created_by VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_versions_budget FOREIGN KEY (budget_id) REFERENCES budgets(id) ON DELETE CASCADE,
    CONSTRAINT uk_versions_budget_code UNIQUE (budget_id, version_code)
);

-- 7. Budget Lines
CREATE TABLE budget_lines (
    id BIGSERIAL PRIMARY KEY,
    budget_id BIGINT NOT NULL,
    budget_version_id BIGINT NOT NULL,
    account_id BIGINT NOT NULL,
    dimension_set_id BIGINT NOT NULL,
    period_start_date DATE NOT NULL,
    period_end_date DATE NOT NULL,
    allocated_amount DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    reserved_amount DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    consumed_amount DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    is_locked BOOLEAN NOT NULL DEFAULT FALSE,
    
    distribution_method VARCHAR(30) NOT NULL DEFAULT 'MANUAL', -- EQUAL, MANUAL, SEASONAL, PREV_YEAR_ACTUAL, PERCENTAGE, FORMULA
    formula_expression VARCHAR(255),
    
    forecast_confidence DECIMAL(5,2),
    predicted_spend DECIMAL(15,2),
    predicted_revenue DECIMAL(15,2),
    ai_recommendation TEXT,
    ai_generated_at TIMESTAMP,

    notes VARCHAR(255),

    CONSTRAINT fk_lines_budget FOREIGN KEY (budget_id) REFERENCES budgets(id) ON DELETE CASCADE,
    CONSTRAINT fk_lines_version FOREIGN KEY (budget_version_id) REFERENCES budget_versions(id) ON DELETE CASCADE,
    CONSTRAINT fk_lines_account FOREIGN KEY (account_id) REFERENCES chart_of_accounts(id),
    CONSTRAINT fk_lines_dim_set FOREIGN KEY (dimension_set_id) REFERENCES budget_dimension_sets(id)
);

-- 8. Budget Control Cache Table (For Real-Time O(1) available checks)
CREATE TABLE budget_control_caches (
    budget_line_id BIGINT PRIMARY KEY,
    allocated_amount DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    reserved_amount DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    consumed_amount DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    available_amount DECIMAL(15,2) NOT NULL DEFAULT 0.00,
    last_updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_bcc_line FOREIGN KEY (budget_line_id) REFERENCES budget_lines(id) ON DELETE CASCADE
);

-- 9. Revisions, Reservations, and Consumptions
CREATE TABLE budget_revisions (
    id BIGSERIAL PRIMARY KEY,
    budget_id BIGINT NOT NULL,
    budget_line_id BIGINT NOT NULL,
    revision_date DATE NOT NULL,
    previous_amount DECIMAL(15,2) NOT NULL,
    new_amount DECIMAL(15,2) NOT NULL,
    change_amount DECIMAL(15,2) NOT NULL,
    reason VARCHAR(255),
    performed_by VARCHAR(100) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'APPROVED',
    
    CONSTRAINT fk_revisions_budget FOREIGN KEY (budget_id) REFERENCES budgets(id) ON DELETE CASCADE,
    CONSTRAINT fk_revisions_line FOREIGN KEY (budget_line_id) REFERENCES budget_lines(id) ON DELETE CASCADE
);

CREATE TABLE budget_reservations (
    id BIGSERIAL PRIMARY KEY,
    budget_line_id BIGINT NOT NULL,
    source_module VARCHAR(50) NOT NULL,
    source_reference_id BIGINT NOT NULL,
    reference_number VARCHAR(100) NOT NULL,
    reserved_amount DECIMAL(15,2) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    expiry_date DATE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_reservations_line FOREIGN KEY (budget_line_id) REFERENCES budget_lines(id) ON DELETE CASCADE
);

CREATE TABLE budget_consumptions (
    id BIGSERIAL PRIMARY KEY,
    budget_line_id BIGINT NOT NULL,
    source_module VARCHAR(50) NOT NULL,
    source_reference_id BIGINT NOT NULL,
    reference_number VARCHAR(100) NOT NULL,
    consumed_amount DECIMAL(15,2) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_consumptions_line FOREIGN KEY (budget_line_id) REFERENCES budget_lines(id) ON DELETE CASCADE
);

-- 10. Snapshots, Approvals, and Audit Logs
CREATE TABLE budget_snapshots (
    id BIGSERIAL PRIMARY KEY,
    budget_id BIGINT NOT NULL,
    version_number INTEGER NOT NULL,
    snapshot_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_by VARCHAR(100) NOT NULL,
    trigger_event VARCHAR(50) NOT NULL,
    notes VARCHAR(255),

    CONSTRAINT fk_snapshots_budget FOREIGN KEY (budget_id) REFERENCES budgets(id) ON DELETE CASCADE
);

CREATE TABLE budget_snapshot_lines (
    id BIGSERIAL PRIMARY KEY,
    snapshot_id BIGINT NOT NULL,
    account_id BIGINT NOT NULL,
    dimension_set_id BIGINT NOT NULL,
    period_start_date DATE NOT NULL,
    period_end_date DATE NOT NULL,
    allocated_amount DECIMAL(15,2) NOT NULL,
    reserved_amount DECIMAL(15,2) NOT NULL,
    consumed_amount DECIMAL(15,2) NOT NULL,

    CONSTRAINT fk_snap_lines_snapshot FOREIGN KEY (snapshot_id) REFERENCES budget_snapshots(id) ON DELETE CASCADE
);

CREATE TABLE budget_approvals (
    id BIGSERIAL PRIMARY KEY,
    budget_id BIGINT NOT NULL,
    approval_step INTEGER NOT NULL,
    role_code VARCHAR(50) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    approver_username VARCHAR(100),
    approved_at TIMESTAMP,
    remarks VARCHAR(255),

    CONSTRAINT fk_approvals_budget FOREIGN KEY (budget_id) REFERENCES budgets(id) ON DELETE CASCADE,
    CONSTRAINT uk_approvals_budget_step UNIQUE (budget_id, approval_step)
);

CREATE TABLE budget_audit_logs (
    id BIGSERIAL PRIMARY KEY,
    budget_id BIGINT NOT NULL,
    event_type VARCHAR(50) NOT NULL,
    description VARCHAR(255) NOT NULL,
    performed_by VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_audit_budget FOREIGN KEY (budget_id) REFERENCES budgets(id) ON DELETE CASCADE
);

-- 11. Materialized Views
CREATE MATERIALIZED VIEW mv_budget_vs_actual AS
SELECT 
    b.company_id,
    b.fiscal_year_id,
    b.id AS budget_id,
    bl.account_id,
    bl.dimension_set_id,
    SUM(bl.allocated_amount) AS total_allocated,
    SUM(bl.reserved_amount) AS total_reserved,
    SUM(bl.consumed_amount) AS total_consumed,
    SUM(bl.allocated_amount - bl.reserved_amount - bl.consumed_amount) AS total_available,
    CASE 
        WHEN SUM(bl.allocated_amount) > 0 THEN ROUND((SUM(bl.consumed_amount) / SUM(bl.allocated_amount)) * 100, 2)
        ELSE 0.00
    END AS utilization_percentage
FROM budgets b
JOIN budget_lines bl ON b.id = bl.budget_id
WHERE b.is_active = TRUE
GROUP BY b.company_id, b.fiscal_year_id, b.id, bl.account_id, bl.dimension_set_id;

CREATE UNIQUE INDEX idx_mv_bva ON mv_budget_vs_actual (budget_id, account_id, dimension_set_id);

-- 12. Seed Permissions
INSERT INTO permissions (code, name) VALUES
('BUDGET_VIEW', 'View budget plans and allocations'),
('BUDGET_CREATE', 'Create and modify budget plans'),
('BUDGET_APPROVE', 'Approve budget plans and revisions'),
('BUDGET_REVISE', 'Request budget revisions and transfers'),
('BUDGET_CONTROL_BYPASS', 'Bypass budget control limits (Soft warning override)'),
('BUDGET_FREEZE', 'Freeze or unfreeze budget plans'),
('BUDGET_LOCK', 'Lock or unlock budget periods/lines')
ON CONFLICT (code) DO NOTHING;

INSERT INTO role_permissions (role_id, permission_id)
SELECT r.id, p.id
FROM roles r
CROSS JOIN permissions p
WHERE r.code IN ('ULTIMATE_ADMIN', 'FINANCE_MANAGER')
  AND p.code IN ('BUDGET_VIEW', 'BUDGET_CREATE', 'BUDGET_APPROVE', 'BUDGET_REVISE', 'BUDGET_CONTROL_BYPASS', 'BUDGET_FREEZE', 'BUDGET_LOCK')
ON CONFLICT (role_id, permission_id) DO NOTHING;
