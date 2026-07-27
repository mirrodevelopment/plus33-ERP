-- ============================================================================
-- PLUS33 Coffee ERP — Database Migration V438
-- Description: Add Target Role, Custom Category, and Escalation fields to Complaints & Support Tickets
-- ============================================================================

-- 1. Add fields to anonymous_complaints table
ALTER TABLE anonymous_complaints
  ADD COLUMN IF NOT EXISTS target_role VARCHAR(50) DEFAULT 'ULTIMATE_ADMIN',
  ADD COLUMN IF NOT EXISTS custom_category VARCHAR(150),
  ADD COLUMN IF NOT EXISTS escalation_level INT DEFAULT 0,
  ADD COLUMN IF NOT EXISTS is_escalated BOOLEAN DEFAULT FALSE;

-- 2. Add fields to support_tickets table
ALTER TABLE support_tickets
  ADD COLUMN IF NOT EXISTS target_role VARCHAR(50) DEFAULT 'STORE_ADMIN',
  ADD COLUMN IF NOT EXISTS custom_category VARCHAR(150),
  ADD COLUMN IF NOT EXISTS escalation_level INT DEFAULT 0,
  ADD COLUMN IF NOT EXISTS is_escalated BOOLEAN DEFAULT FALSE;

-- 3. Add Indexes for fast query filtering
CREATE INDEX IF NOT EXISTS idx_ac_target_role ON anonymous_complaints (target_role);
CREATE INDEX IF NOT EXISTS idx_ac_is_escalated ON anonymous_complaints (is_escalated);
CREATE INDEX IF NOT EXISTS idx_st_target_role ON support_tickets (target_role);
CREATE INDEX IF NOT EXISTS idx_st_is_escalated ON support_tickets (is_escalated);
