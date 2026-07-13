-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Migration Version : 388
-- File              : V388__deactivate_duplicate_leave_types.sql
-- Operation Type    : Data Cleanup
-- Purpose           : Deactivate duplicate leave types with prefix 'LT_' from older seeds.
--                     This leaves only the clean, enterprise-policy-integrated types
--                     (ANNUAL, SICK, CASUAL, UNPAID, MATERNITY, PERSONAL, EMERGENCY,
--                     MARRIAGE, BEREAVEMENT, PATERNITY) active.
-- ============================================================================

UPDATE leave_types 
SET active = FALSE 
WHERE code IN ('LT_ANNUAL', 'LT_SICK', 'LT_MATERNITY', 'LT_PATERNITY', 'LT_UNPAID');
