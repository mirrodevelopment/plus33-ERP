-- ============================================================================
-- PLUS33 Coffee ERP — Database Migration V439
-- Description: Create user_activity_logs table for login auditing with trigger-level immutability
-- NOTE: Uses PostgreSQL syntax
-- ============================================================================

CREATE TABLE IF NOT EXISTS user_activity_logs (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NULL,
    username VARCHAR(100) NOT NULL,
    login_time TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ip_address VARCHAR(45) NOT NULL,
    location VARCHAR(100),
    user_agent VARCHAR(255),
    status VARCHAR(20) NOT NULL,
    failure_reason VARCHAR(255) NULL,
    logout_time TIMESTAMP NULL,
    last_active_time TIMESTAMP NULL
);

CREATE INDEX IF NOT EXISTS idx_ual_username ON user_activity_logs (username);
CREATE INDEX IF NOT EXISTS idx_ual_login_time ON user_activity_logs (login_time DESC);
CREATE INDEX IF NOT EXISTS idx_ual_status ON user_activity_logs (status);

-- Immutability Trigger Function
CREATE OR REPLACE FUNCTION prevent_modification()
RETURNS TRIGGER AS $$
BEGIN
    IF TG_OP = 'DELETE' THEN
        RAISE EXCEPTION 'Deletions are strictly prohibited on user_activity_logs for compliance integrity.';
    ELSIF TG_OP = 'UPDATE' THEN
        -- Allow updates only for last_active_time and logout_time, block all other columns
        IF NEW.id <> OLD.id OR
           NEW.user_id IS DISTINCT FROM OLD.user_id OR
           NEW.username <> OLD.username OR
           NEW.login_time <> OLD.login_time OR
           NEW.ip_address <> OLD.ip_address OR
           NEW.location IS DISTINCT FROM OLD.location OR
           NEW.user_agent IS DISTINCT FROM OLD.user_agent OR
           NEW.status <> OLD.status OR
           NEW.failure_reason IS DISTINCT FROM OLD.failure_reason THEN
            RAISE EXCEPTION 'Only session heartbeat (last_active_time) and logout (logout_time) can be modified. All other columns are immutable.';
        END IF;
    END IF;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_prevent_update_delete
BEFORE UPDATE OR DELETE ON user_activity_logs
FOR EACH ROW
EXECUTE FUNCTION prevent_modification();
