-- Remove shift capacity limit constraint trigger
DROP TRIGGER IF EXISTS trg_check_shift_capacity ON employee_shifts;
DROP FUNCTION IF EXISTS check_shift_capacity();
