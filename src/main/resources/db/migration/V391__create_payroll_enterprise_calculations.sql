-- 1. Create country_benefit_policies table
CREATE TABLE IF NOT EXISTS country_benefit_policies (
    id BIGSERIAL PRIMARY KEY,
    country_code VARCHAR(10) NOT NULL UNIQUE,
    employee_pf_rate NUMERIC(5,4) NOT NULL DEFAULT 0.1200,
    employee_esi_rate NUMERIC(5,4) NOT NULL DEFAULT 0.0075,
    employee_pension_rate NUMERIC(5,4) NOT NULL DEFAULT 0.0800,
    employee_insurance_rate NUMERIC(5,4) NOT NULL DEFAULT 0.0100,
    employer_pf_rate NUMERIC(5,4) NOT NULL DEFAULT 0.1300,
    employer_esi_rate NUMERIC(5,4) NOT NULL DEFAULT 0.0325,
    employer_pension_rate NUMERIC(5,4) NOT NULL DEFAULT 0.0833,
    employer_insurance_rate NUMERIC(5,4) NOT NULL DEFAULT 0.0150,
    employer_social_security_rate NUMERIC(5,4) NOT NULL DEFAULT 0.2000,
    employer_health_insurance_rate NUMERIC(5,4) NOT NULL DEFAULT 0.1000,
    employer_gratuity_rate NUMERIC(5,4) NOT NULL DEFAULT 0.0481,
    employer_end_of_service_rate NUMERIC(5,4) NOT NULL DEFAULT 0.0417,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Seed benefit policy for France (FR)
INSERT INTO country_benefit_policies (
    country_code, employee_pf_rate, employee_esi_rate, employee_pension_rate, employee_insurance_rate,
    employer_pf_rate, employer_esi_rate, employer_pension_rate, employer_insurance_rate,
    employer_social_security_rate, employer_health_insurance_rate, employer_gratuity_rate, employer_end_of_service_rate
)
VALUES (
    'FR', 0.1200, 0.0075, 0.0800, 0.0100,
    0.1300, 0.0325, 0.0833, 0.0150,
    0.2000, 0.1000, 0.0481, 0.0417
)
ON CONFLICT (country_code) DO NOTHING;

-- 2. Alter payroll_run_items table to add snapshot columns
ALTER TABLE payroll_run_items ADD COLUMN IF NOT EXISTS attendance_snapshot TEXT;
ALTER TABLE payroll_run_items ADD COLUMN IF NOT EXISTS leave_snapshot TEXT;
ALTER TABLE payroll_run_items ADD COLUMN IF NOT EXISTS salary_snapshot TEXT;
ALTER TABLE payroll_run_items ADD COLUMN IF NOT EXISTS working_hour_snapshot TEXT;
ALTER TABLE payroll_run_items ADD COLUMN IF NOT EXISTS overtime_snapshot TEXT;
ALTER TABLE payroll_run_items ADD COLUMN IF NOT EXISTS benefit_snapshot TEXT;
ALTER TABLE payroll_run_items ADD COLUMN IF NOT EXISTS tax_snapshot TEXT;
ALTER TABLE payroll_run_items ADD COLUMN IF NOT EXISTS employer_contribution_snapshot TEXT;
ALTER TABLE payroll_run_items ADD COLUMN IF NOT EXISTS payroll_audit TEXT;
