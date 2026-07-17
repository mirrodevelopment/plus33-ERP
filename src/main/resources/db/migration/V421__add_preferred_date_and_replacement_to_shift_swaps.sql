-- V421: Add preferred date, approved date/shift, and replacement employee to shift swaps
-- This supports: employee sets a preferred date + supervisor overrides + supervisor fills vacancy

ALTER TABLE employee_shift_swaps
    ADD COLUMN IF NOT EXISTS preferred_date          DATE,
    ADD COLUMN IF NOT EXISTS approved_date           DATE,
    ADD COLUMN IF NOT EXISTS approved_shift_id       BIGINT REFERENCES shifts(id),
    ADD COLUMN IF NOT EXISTS replacement_employee_id BIGINT REFERENCES employees(id);

COMMENT ON COLUMN employee_shift_swaps.preferred_date           IS 'Date the employee prefers to work the new shift';
COMMENT ON COLUMN employee_shift_swaps.approved_date            IS 'Final date approved by supervisor (may differ from preferred_date)';
COMMENT ON COLUMN employee_shift_swaps.approved_shift_id        IS 'Final shift approved by supervisor (may differ from preferred_shift_id)';
COMMENT ON COLUMN employee_shift_swaps.replacement_employee_id  IS 'Employee assigned to fill the original vacated shift slot';
