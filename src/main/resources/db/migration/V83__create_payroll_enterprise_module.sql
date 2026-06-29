-- ============================================================
-- V83__create_payroll_enterprise_module.sql
-- PLUS33 ERP — Enterprise Payroll & Human Capital Finance
-- ============================================================

-- 1. Payroll Policies & Versions
CREATE TABLE payroll_policies (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL REFERENCES companies(id),
    code VARCHAR(50) NOT NULL,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_payroll_policy_code UNIQUE (company_id, code)
);

CREATE TABLE payroll_policy_versions (
    id BIGSERIAL PRIMARY KEY,
    policy_id BIGINT NOT NULL REFERENCES payroll_policies(id),
    version_number INT NOT NULL,
    effective_from DATE NOT NULL,
    effective_to DATE,
    proration_rule VARCHAR(50) NOT NULL DEFAULT 'CALENDAR_DAYS',
    overtime_multiplier DECIMAL(5,2) NOT NULL DEFAULT 1.50,
    holiday_pay_multiplier DECIMAL(5,2) NOT NULL DEFAULT 2.00,
    rounding_rule VARCHAR(50) NOT NULL DEFAULT 'HALF_EVEN',
    allow_negative_payroll BOOLEAN NOT NULL DEFAULT FALSE,
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_payroll_policy_version UNIQUE (policy_id, version_number)
);

-- 2. Salary Components & Effective-Dated Structures
CREATE TABLE salary_components (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL REFERENCES companies(id),
    code VARCHAR(50) NOT NULL,
    name VARCHAR(100) NOT NULL,
    component_type VARCHAR(50) NOT NULL, -- EARNING, DEDUCTION, STATUTORY, EMPLOYER_CONTRIBUTION
    calculation_method VARCHAR(50) NOT NULL, -- FIXED, PERCENTAGE, FORMULA, PROGRESSIVE_SLAB
    gl_account_id BIGINT REFERENCES accounts(id),
    active BOOLEAN NOT NULL DEFAULT TRUE,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_salary_component_code UNIQUE (company_id, code)
);

CREATE TABLE employee_salary_structures (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL REFERENCES companies(id),
    employee_id BIGINT NOT NULL REFERENCES employees(id),
    version INT NOT NULL DEFAULT 1,
    effective_from DATE NOT NULL,
    effective_to DATE,
    currency_code VARCHAR(3) NOT NULL DEFAULT 'USD',
    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE employee_salary_structure_items (
    id BIGSERIAL PRIMARY KEY,
    structure_id BIGINT NOT NULL REFERENCES employee_salary_structures(id) ON DELETE CASCADE,
    component_id BIGINT NOT NULL REFERENCES salary_components(id),
    amount DECIMAL(14,4) NOT NULL DEFAULT 0.0000,
    percentage DECIMAL(7,4),
    formula_expression VARCHAR(255),
    active BOOLEAN NOT NULL DEFAULT TRUE
);

-- 3. Payroll Runs, Items & Itemized Breakdowns
CREATE TABLE payroll_runs (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL REFERENCES companies(id),
    payroll_period_id BIGINT REFERENCES payroll_periods(id),
    run_number VARCHAR(100) NOT NULL,
    payroll_calendar_type VARCHAR(50) NOT NULL DEFAULT 'MONTHLY',
    country_code VARCHAR(10) NOT NULL DEFAULT 'US',
    run_type VARCHAR(50) NOT NULL DEFAULT 'REGULAR', -- REGULAR, OFF_CYCLE, BONUS, CORRECTION, FINAL_SETTLEMENT
    status VARCHAR(50) NOT NULL DEFAULT 'DRAFT', -- DRAFT, CALCULATED, VALIDATED, APPROVED, POSTED, PAID, ARCHIVED
    total_gross DECIMAL(14,2) NOT NULL DEFAULT 0.00,
    total_net DECIMAL(14,2) NOT NULL DEFAULT 0.00,
    total_employer_contributions DECIMAL(14,2) NOT NULL DEFAULT 0.00,
    total_taxes DECIMAL(14,2) NOT NULL DEFAULT 0.00,
    executed_by VARCHAR(100),
    approved_by VARCHAR(100),
    posted_at TIMESTAMP,
    paid_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_payroll_run_number UNIQUE (company_id, run_number)
);

CREATE TABLE payroll_run_items (
    id BIGSERIAL PRIMARY KEY,
    payroll_run_id BIGINT NOT NULL REFERENCES payroll_runs(id) ON DELETE CASCADE,
    employee_id BIGINT NOT NULL REFERENCES employees(id),
    gross_pay DECIMAL(14,2) NOT NULL DEFAULT 0.00,
    net_pay DECIMAL(14,2) NOT NULL DEFAULT 0.00,
    total_deductions DECIMAL(14,2) NOT NULL DEFAULT 0.00,
    employer_contributions DECIMAL(14,2) NOT NULL DEFAULT 0.00,
    tax_withheld DECIMAL(14,2) NOT NULL DEFAULT 0.00,
    status VARCHAR(30) NOT NULL DEFAULT 'CALCULATED'
);

CREATE TABLE payroll_item_breakdowns (
    id BIGSERIAL PRIMARY KEY,
    payroll_run_item_id BIGINT NOT NULL REFERENCES payroll_run_items(id) ON DELETE CASCADE,
    component_id BIGINT NOT NULL REFERENCES salary_components(id),
    amount DECIMAL(14,2) NOT NULL DEFAULT 0.00,
    description VARCHAR(255)
);

-- 4. Multi-Dimensional Cost Allocations
CREATE TABLE payroll_cost_allocations (
    id BIGSERIAL PRIMARY KEY,
    payroll_run_item_id BIGINT NOT NULL REFERENCES payroll_run_items(id) ON DELETE CASCADE,
    department_id BIGINT,
    cost_center_id BIGINT,
    project_id BIGINT,
    allocation_percentage DECIMAL(5,2) NOT NULL DEFAULT 100.00
);

-- 5. Accruals & Attendance Integration Sync Logs
CREATE TABLE leave_accrual_logs (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL REFERENCES companies(id),
    employee_id BIGINT NOT NULL REFERENCES employees(id),
    accrual_date DATE NOT NULL,
    leave_type VARCHAR(50) NOT NULL,
    accrued_hours DECIMAL(7,2) NOT NULL DEFAULT 0.00,
    monetary_value DECIMAL(14,2) NOT NULL DEFAULT 0.00,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE attendance_sync_logs (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL REFERENCES companies(id),
    employee_id BIGINT NOT NULL REFERENCES employees(id),
    sync_date DATE NOT NULL,
    attendance_source VARCHAR(50) NOT NULL, -- BIOMETRIC, RFID, MOBILE, WEB
    hours_worked DECIMAL(5,2) NOT NULL DEFAULT 0.00,
    overtime_hours DECIMAL(5,2) NOT NULL DEFAULT 0.00,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 6. Workflows & Audit Timeline
CREATE TABLE payroll_approval_workflows (
    id BIGSERIAL PRIMARY KEY,
    payroll_run_id BIGINT NOT NULL REFERENCES payroll_runs(id) ON DELETE CASCADE,
    step_number INT NOT NULL,
    approver_role VARCHAR(50) NOT NULL,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    comments VARCHAR(255),
    actioned_at TIMESTAMP
);

CREATE TABLE payroll_audit_events (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL REFERENCES companies(id),
    payroll_run_id BIGINT REFERENCES payroll_runs(id),
    event_type VARCHAR(100) NOT NULL,
    description TEXT NOT NULL,
    actor VARCHAR(100) NOT NULL,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 7. Reporting Materialized Views
CREATE MATERIALIZED VIEW mv_payroll_dashboard AS
SELECT 
    r.company_id,
    COUNT(DISTINCT r.id) AS total_payroll_runs,
    COALESCE(SUM(r.total_gross), 0) AS aggregate_gross,
    COALESCE(SUM(r.total_net), 0) AS aggregate_net,
    COALESCE(SUM(r.total_employer_contributions), 0) AS aggregate_employer_contributions,
    COALESCE(SUM(r.total_taxes), 0) AS aggregate_taxes
FROM payroll_runs r
GROUP BY r.company_id;

CREATE UNIQUE INDEX idx_mv_payroll_dashboard ON mv_payroll_dashboard(company_id);

CREATE MATERIALIZED VIEW mv_payroll_department AS
SELECT 
    r.company_id,
    a.department_id,
    COALESCE(SUM(i.gross_pay * (a.allocation_percentage / 100.0)), 0) AS allocated_gross,
    COALESCE(SUM(i.net_pay * (a.allocation_percentage / 100.0)), 0) AS allocated_net
FROM payroll_runs r
JOIN payroll_run_items i ON i.payroll_run_id = r.id
JOIN payroll_cost_allocations a ON a.payroll_run_item_id = i.id
WHERE a.department_id IS NOT NULL
GROUP BY r.company_id, a.department_id;

CREATE UNIQUE INDEX idx_mv_payroll_dept ON mv_payroll_department(company_id, department_id);
