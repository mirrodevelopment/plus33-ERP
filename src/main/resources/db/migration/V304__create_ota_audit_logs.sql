-- V304: OTA Audit Logs
CREATE TABLE IF NOT EXISTS platform_ota_audit_log (
    id                      BIGSERIAL PRIMARY KEY,
    campaign_id             BIGINT NOT NULL,
    operator                VARCHAR(100) NOT NULL,
    action_type             VARCHAR(100) NOT NULL, -- CREATE_CAMPAIGN, START_CAMPAIGN, CANCEL_CAMPAIGN, FORCE_ROLLBACK
    previous_config         TEXT,
    new_config              TEXT,
    approval_id             VARCHAR(100),
    trace_id                VARCHAR(100) NOT NULL,
    reason                  VARCHAR(500),
    audited_at              TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
