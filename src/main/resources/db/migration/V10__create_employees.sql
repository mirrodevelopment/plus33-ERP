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
