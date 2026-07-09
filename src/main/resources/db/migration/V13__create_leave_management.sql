-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 13
-- File              : V13__create_leave_management.sql
-- Operation Type    : Schema Creation
-- Purpose           : create leave management
--
-- Tables Created    : leave_types, employee_leaves
-- Tables Altered    : N/A
-- Seed Data For     : leave_types
-- Indexes           : idx_employee_leaves_employee, idx_employee_leaves_status, idx_employee_leaves_dates
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
-- ============================================================
-- V13__create_leave_management.sql
-- PLUS33 ERP — Leave Management Tables
-- ============================================================

CREATE TABLE leave_types (
    id BIGSERIAL PRIMARY KEY,

    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,

    paid BOOLEAN NOT NULL DEFAULT TRUE,

    annual_limit INTEGER,

    carry_forward BOOLEAN NOT NULL DEFAULT FALSE,

    active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE employee_leaves (
    id BIGSERIAL PRIMARY KEY,

    employee_id BIGINT NOT NULL,
    leave_type_id BIGINT NOT NULL,

    start_date DATE NOT NULL,
    end_date DATE NOT NULL,

    total_days DECIMAL(5,2) NOT NULL,

    reason TEXT,

    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',

    approved_by BIGINT,

    approved_at TIMESTAMP,

    rejection_reason TEXT,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_employee_leaves_employee
        FOREIGN KEY (employee_id)
        REFERENCES employees(id),

    CONSTRAINT fk_employee_leaves_leave_type
        FOREIGN KEY (leave_type_id)
        REFERENCES leave_types(id),

    CONSTRAINT fk_employee_leaves_approver
        FOREIGN KEY (approved_by)
        REFERENCES users(id)
);

CREATE INDEX idx_employee_leaves_employee ON employee_leaves(employee_id);
CREATE INDEX idx_employee_leaves_status ON employee_leaves(status);
CREATE INDEX idx_employee_leaves_dates ON employee_leaves(start_date, end_date);

-- Seed recommended leave types
INSERT INTO leave_types (code, name, paid, annual_limit, carry_forward, active)
VALUES
('ANNUAL',    'Annual Leave',    TRUE,  30,   TRUE,  TRUE),
('SICK',      'Sick Leave',      TRUE,  12,   FALSE, TRUE),
('CASUAL',    'Casual Leave',    TRUE,  12,   FALSE, TRUE),
('UNPAID',    'Unpaid Leave',    FALSE, NULL, FALSE, TRUE),
('MATERNITY', 'Maternity Leave', TRUE,  NULL, FALSE, TRUE);
