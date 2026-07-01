-- V146: Performance and Competency Management
CREATE TABLE IF NOT EXISTS hcm_goals (
    id BIGSERIAL PRIMARY KEY,
    employee_id BIGINT NOT NULL,
    description VARCHAR(255) NOT NULL,
    target_date DATE NOT NULL,
    progress_percentage NUMERIC(5, 2) NOT NULL DEFAULT 0.00,
    status VARCHAR(30) NOT NULL DEFAULT 'IN_PROGRESS'
);

CREATE TABLE IF NOT EXISTS hcm_competencies (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    description VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS hcm_employee_competencies (
    id BIGSERIAL PRIMARY KEY,
    employee_id BIGINT NOT NULL,
    competency_id BIGINT NOT NULL,
    rating NUMERIC(3, 2) NOT NULL DEFAULT 0.00
);
