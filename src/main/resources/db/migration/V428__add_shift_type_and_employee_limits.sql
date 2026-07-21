-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Migration Version : 428
-- File              : V428__add_shift_type_and_employee_limits.sql
-- Purpose           : Add shift_type, min_employees, and max_employees columns to 'shifts'
-- ============================================================================

ALTER TABLE shifts ADD COLUMN IF NOT EXISTS shift_type VARCHAR(30) DEFAULT 'CUSTOM';
ALTER TABLE shifts ADD COLUMN IF NOT EXISTS min_employees INT DEFAULT 2;
ALTER TABLE shifts ADD COLUMN IF NOT EXISTS max_employees INT DEFAULT 8;

-- Update existing default shifts with appropriate shift_type and employee headcount targets
UPDATE shifts SET shift_type = 'MORNING', min_employees = 2, max_employees = 6 WHERE LOWER(code) LIKE '%mrn%' OR LOWER(name) LIKE '%morning%';
UPDATE shifts SET shift_type = 'MID', min_employees = 2, max_employees = 8 WHERE LOWER(code) LIKE '%mid%' OR LOWER(name) LIKE '%mid%';
UPDATE shifts SET shift_type = 'LATE', min_employees = 2, max_employees = 6 WHERE LOWER(code) LIKE '%lte%' OR LOWER(name) LIKE '%late%' OR LOWER(name) LIKE '%afternoon%';
UPDATE shifts SET shift_type = 'NIGHT', min_employees = 1, max_employees = 4 WHERE LOWER(code) LIKE '%ngt%' OR LOWER(name) LIKE '%night%';
