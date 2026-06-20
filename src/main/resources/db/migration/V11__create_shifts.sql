-- ============================================================
-- V11__create_shifts.sql
-- PLUS33 ERP — Shift and Employee Shift Tables
-- ============================================================

CREATE TABLE shifts (
    id BIGSERIAL PRIMARY KEY,

    code VARCHAR(50) NOT NULL UNIQUE,
    name VARCHAR(100) NOT NULL,

    company_id BIGINT NOT NULL,

    start_time TIME NOT NULL,
    end_time TIME NOT NULL,

    break_minutes INTEGER NOT NULL DEFAULT 0,

    overnight BOOLEAN NOT NULL DEFAULT FALSE,

    active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_shifts_company
        FOREIGN KEY (company_id)
        REFERENCES companies(id)
);

CREATE TABLE employee_shifts (
    employee_id BIGINT NOT NULL,
    shift_id BIGINT NOT NULL,

    effective_from DATE NOT NULL,
    effective_to DATE,

    PRIMARY KEY (employee_id, shift_id, effective_from),

    CONSTRAINT fk_employee_shifts_employee
        FOREIGN KEY (employee_id)
        REFERENCES employees(id)
        ON DELETE CASCADE,

    CONSTRAINT fk_employee_shifts_shift
        FOREIGN KEY (shift_id)
        REFERENCES shifts(id)
        ON DELETE CASCADE
);
