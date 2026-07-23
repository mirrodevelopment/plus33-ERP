-- =============================================================================
-- V432: Audit Logs for Policy Configuration Changes
--
-- Creates the leave_policy_change_log table to track policy modifications
-- (group, type, field, old/new values, actor user, timestamp, ip).
-- =============================================================================

CREATE TABLE IF NOT EXISTS leave_policy_change_log (
    id                 BIGSERIAL PRIMARY KEY,
    policy_group_id    BIGINT REFERENCES leave_policy_groups(id) ON DELETE SET NULL,
    leave_type_id      BIGINT REFERENCES leave_types(id) ON DELETE SET NULL,
    field_changed      VARCHAR(100) NOT NULL,
    old_value          TEXT,
    new_value          TEXT,
    changed_by_user_id BIGINT REFERENCES users(id) ON DELETE SET NULL,
    changed_at         TIMESTAMP NOT NULL DEFAULT NOW(),
    reason             TEXT,
    ip_address         VARCHAR(50)
);

COMMENT ON TABLE leave_policy_change_log IS 'Logs administrative changes to leave policy configurations for audit trails.';
