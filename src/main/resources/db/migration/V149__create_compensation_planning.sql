-- V149: Compensation Planning and Salaries History
CREATE TABLE IF NOT EXISTS hcm_compensation_history (
    id BIGSERIAL PRIMARY KEY,
    employee_id BIGINT NOT NULL,
    effective_date DATE NOT NULL,
    base_salary NUMERIC(18, 2) NOT NULL,
    notes TEXT,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
