-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 406
-- File              : V406__seed_shifts_fallback.sql
-- Operation Type    : Seed Data / Fallback Fix
-- Purpose           : Seed default shifts if missing
-- ============================================================================

-- Seed default shifts if they don't exist for PLUS33_COFFEE
INSERT INTO shifts (code, name, company_id, start_time, end_time, break_minutes, overnight, active, created_at, updated_at)
SELECT 'SHIFT_MORN', 'Morning Shift', c.id, '06:00'::TIME, '14:00'::TIME, 30, FALSE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM companies c
WHERE c.code = 'PLUS33_COFFEE' AND NOT EXISTS (SELECT 1 FROM shifts WHERE code = 'SHIFT_MORN');

INSERT INTO shifts (code, name, company_id, start_time, end_time, break_minutes, overnight, active, created_at, updated_at)
SELECT 'SHIFT_AFT', 'Afternoon Shift', c.id, '14:00'::TIME, '22:00'::TIME, 30, FALSE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM companies c
WHERE c.code = 'PLUS33_COFFEE' AND NOT EXISTS (SELECT 1 FROM shifts WHERE code = 'SHIFT_AFT');

INSERT INTO shifts (code, name, company_id, start_time, end_time, break_minutes, overnight, active, created_at, updated_at)
SELECT 'SHIFT_NGHT', 'Night Shift', c.id, '22:00'::TIME, '06:00'::TIME, 30, TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM companies c
WHERE c.code = 'PLUS33_COFFEE' AND NOT EXISTS (SELECT 1 FROM shifts WHERE code = 'SHIFT_NGHT');

-- Fallback shifts seed for any active company if shifts table is still empty
INSERT INTO shifts (code, name, company_id, start_time, end_time, break_minutes, overnight, active, created_at, updated_at)
SELECT 'SHIFT_MORN', 'Morning Shift', c.id, '06:00'::TIME, '14:00'::TIME, 30, FALSE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM companies c
WHERE NOT EXISTS (SELECT 1 FROM shifts WHERE code = 'SHIFT_MORN')
LIMIT 1;

INSERT INTO shifts (code, name, company_id, start_time, end_time, break_minutes, overnight, active, created_at, updated_at)
SELECT 'SHIFT_AFT', 'Afternoon Shift', c.id, '14:00'::TIME, '22:00'::TIME, 30, FALSE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM companies c
WHERE NOT EXISTS (SELECT 1 FROM shifts WHERE code = 'SHIFT_AFT')
LIMIT 1;

INSERT INTO shifts (code, name, company_id, start_time, end_time, break_minutes, overnight, active, created_at, updated_at)
SELECT 'SHIFT_NGHT', 'Night Shift', c.id, '22:00'::TIME, '06:00'::TIME, 30, TRUE, TRUE, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP
FROM companies c
WHERE NOT EXISTS (SELECT 1 FROM shifts WHERE code = 'SHIFT_NGHT')
LIMIT 1;
