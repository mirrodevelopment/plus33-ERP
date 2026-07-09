-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 10
-- File              : V10__create_employees.sql
-- Operation Type    : Schema Creation
-- Purpose           : create employees
--
-- Tables Created    : employees
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
-- V10__create_employees.sql
-- PLUS33 ERP — Employee Master Table
-- ============================================================

CREATE TABLE employees (
    id BIGSERIAL PRIMARY KEY,

    employee_code VARCHAR(50) NOT NULL UNIQUE,

    user_id BIGINT UNIQUE,

    company_id BIGINT NOT NULL,

    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100),

    email VARCHAR(150),
    phone VARCHAR(30),

    designation VARCHAR(100) NOT NULL,
    department VARCHAR(100),

    employment_type VARCHAR(50) NOT NULL,

    hire_date DATE NOT NULL,

    status VARCHAR(30) NOT NULL DEFAULT 'ACTIVE',

    active BOOLEAN NOT NULL DEFAULT TRUE,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_employees_user
        FOREIGN KEY (user_id)
        REFERENCES users(id),

    CONSTRAINT fk_employees_company
        FOREIGN KEY (company_id)
        REFERENCES companies(id)
);
