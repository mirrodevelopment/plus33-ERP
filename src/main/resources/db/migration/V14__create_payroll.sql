-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 14
-- File              : V14__create_payroll.sql
-- Operation Type    : Schema Creation
-- Purpose           : create payroll
--
-- Tables Created    : payroll_periods, employee_payrolls, overtime_rules
-- Tables Altered    : N/A
-- Seed Data For     : N/A
-- Indexes           : N/A
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
-- ============================================================
-- V14__create_payroll.sql
-- PLUS33 ERP — Payroll Management Tables
-- ============================================================

CREATE TABLE payroll_periods (
    id BIGSERIAL PRIMARY KEY,

    company_id BIGINT NOT NULL,

    period_name VARCHAR(100) NOT NULL,

    start_date DATE NOT NULL,
    end_date DATE NOT NULL,

    status VARCHAR(30) NOT NULL DEFAULT 'OPEN',

    processed_at TIMESTAMP,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_payroll_periods_company
        FOREIGN KEY (company_id)
        REFERENCES companies(id)
);

CREATE TABLE employee_payrolls (
    id BIGSERIAL PRIMARY KEY,

    payroll_period_id BIGINT NOT NULL,
    employee_id BIGINT NOT NULL,

    base_salary DECIMAL(12,2) NOT NULL DEFAULT 0,

    overtime_amount DECIMAL(12,2) NOT NULL DEFAULT 0,

    allowances DECIMAL(12,2) NOT NULL DEFAULT 0,

    deductions DECIMAL(12,2) NOT NULL DEFAULT 0,

    gross_salary DECIMAL(12,2) NOT NULL DEFAULT 0,

    net_salary DECIMAL(12,2) NOT NULL DEFAULT 0,

    payment_status VARCHAR(30) NOT NULL DEFAULT 'PENDING',

    generated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_employee_payrolls_period
        FOREIGN KEY (payroll_period_id)
        REFERENCES payroll_periods(id),

    CONSTRAINT fk_employee_payrolls_employee
        FOREIGN KEY (employee_id)
        REFERENCES employees(id),

    CONSTRAINT uk_employee_payroll
        UNIQUE (payroll_period_id, employee_id)
);

CREATE TABLE overtime_rules (
    id BIGSERIAL PRIMARY KEY,

    company_id BIGINT NOT NULL,

    name VARCHAR(100) NOT NULL,

    multiplier DECIMAL(5,2) NOT NULL,

    active BOOLEAN NOT NULL DEFAULT TRUE,

    CONSTRAINT fk_overtime_rules_company
        FOREIGN KEY (company_id)
        REFERENCES companies(id)
);
