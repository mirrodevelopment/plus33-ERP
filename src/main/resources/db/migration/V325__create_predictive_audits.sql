-- V325: Predictive Audit Logs
CREATE TABLE IF NOT EXISTS platform_predictive_audit_log (
    id                              BIGSERIAL PRIMARY KEY,
    prediction_id                   BIGINT NOT NULL,
    operator                        VARCHAR(100) NOT NULL,
    action_type                     VARCHAR(100) NOT NULL, -- UPDATE_THRESHOLD, MODEL_REDEPLOY, STRATEGY_CHANGE
    previous_threshold_config       TEXT,
    new_threshold_config            TEXT,
    approval_reference              VARCHAR(100),
    trace_id                        VARCHAR(100) NOT NULL,
    reason                          VARCHAR(500),
    audited_at                      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
