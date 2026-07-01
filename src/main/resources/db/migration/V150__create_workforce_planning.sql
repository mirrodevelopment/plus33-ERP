-- V150: Workforce Planning shift rosters
CREATE TABLE IF NOT EXISTS hcm_shift_patterns (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL UNIQUE,
    weekly_hours NUMERIC(5, 2) NOT NULL DEFAULT 40.00
);

CREATE TABLE IF NOT EXISTS hcm_rosters (
    id BIGSERIAL PRIMARY KEY,
    employee_id BIGINT NOT NULL,
    shift_date DATE NOT NULL,
    shift_pattern_id BIGINT NOT NULL
);
