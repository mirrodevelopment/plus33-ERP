-- V274: SCADA Alarms
CREATE TABLE IF NOT EXISTS platform_scada_alarm_policy (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    policy_code         VARCHAR(100) NOT NULL UNIQUE,
    severity            VARCHAR(50) NOT NULL, -- Critical, High, Medium, Low
    threshold_value     NUMERIC(19,4) NOT NULL
);

CREATE TABLE IF NOT EXISTS platform_scada_alarm_event (
    id                  BIGSERIAL PRIMARY KEY,
    device_id           BIGINT NOT NULL,
    policy_id           BIGINT NOT NULL,
    alarm_status        VARCHAR(50) NOT NULL DEFAULT 'ACTIVE', -- Acknowledged, Shelved, Suppressed, Returned To Normal
    message             TEXT NOT NULL,
    triggered_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    resolved_at         TIMESTAMP
);
