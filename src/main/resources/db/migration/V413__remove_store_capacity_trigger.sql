-- Remove store capacity limit constraint trigger
DROP TRIGGER IF EXISTS trg_check_store_capacity ON user_stores;
DROP FUNCTION IF EXISTS check_store_capacity();
