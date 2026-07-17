-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Migration Version : 418
-- File              : V418__add_targeting_to_announcements.sql
-- Operation Type    : Alter Table
-- Purpose           : Add target region and target store columns to announcements table
-- ============================================================================

ALTER TABLE announcements ADD COLUMN target_region_id BIGINT REFERENCES regions(id) ON DELETE CASCADE;
ALTER TABLE announcements ADD COLUMN target_store_id BIGINT REFERENCES stores(id) ON DELETE CASCADE;
