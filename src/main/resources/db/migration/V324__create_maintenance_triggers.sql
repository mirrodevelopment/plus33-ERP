-- V324: Maintenance Triggers & Auto-Repair Logs
CREATE TABLE IF NOT EXISTS platform_maintenance_trigger_log (
    id                              BIGSERIAL PRIMARY KEY,
    trigger_source                  VARCHAR(100) NOT NULL, -- PREDICTIVE_ENGINE, MANUAL, THRESHOLD
    predicted_failure_id            BIGINT,
    work_order_reference            VARCHAR(100) NOT NULL,
    maintenance_status              VARCHAR(50) NOT NULL, -- SCHEDULED, ASSIGNED, COMPLETED, CANCELLED
    scheduled_time                  TIMESTAMP NOT NULL,
    completion_time                 TIMESTAMP,
    technician_assignment           VARCHAR(100),
    automatic_execution             BOOLEAN NOT NULL DEFAULT TRUE,
    rollback_status                 VARCHAR(50),
    triggered_at                    TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
