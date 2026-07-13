-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 386
-- File              : V386__add_store_and_shift_capacity_triggers.sql
-- Operation Type    : Create Triggers
-- Purpose           : Enforce store capacity (30 max) and shift capacity (10 max)
-- ============================================================================

CREATE OR REPLACE FUNCTION check_store_capacity()
RETURNS TRIGGER AS $$
BEGIN
    -- If the user is already assigned to this store, allow the update/re-assignment
    IF EXISTS (SELECT 1 FROM user_stores WHERE user_id = NEW.user_id AND store_id = NEW.store_id) THEN
        RETURN NEW;
    END IF;

    -- Otherwise, check capacity limit
    IF (SELECT COUNT(*) FROM user_stores WHERE store_id = NEW.store_id) >= 30 THEN
        RAISE EXCEPTION 'Store has reached its maximum capacity of 30 employees';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER trg_check_store_capacity
BEFORE INSERT ON user_stores
FOR EACH ROW
EXECUTE FUNCTION check_store_capacity();


CREATE OR REPLACE FUNCTION check_shift_capacity()
RETURNS TRIGGER AS $$
BEGIN
    -- If the employee is already assigned to this shift, allow it
    IF EXISTS (SELECT 1 FROM employee_shifts WHERE employee_id = NEW.employee_id AND shift_id = NEW.shift_id AND effective_from = NEW.effective_from) THEN
        RETURN NEW;
    END IF;

    -- Otherwise, check capacity limit
    IF (SELECT COUNT(*) FROM employee_shifts WHERE shift_id = NEW.shift_id AND (effective_to IS NULL OR effective_to >= NEW.effective_from)) >= 10 THEN
        RAISE EXCEPTION 'Shift has reached its maximum capacity of 10 employees';
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE OR REPLACE TRIGGER trg_check_shift_capacity
BEFORE INSERT ON employee_shifts
FOR EACH ROW
EXECUTE FUNCTION check_shift_capacity();
