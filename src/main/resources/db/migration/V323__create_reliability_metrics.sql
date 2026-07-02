-- V323: Reliability Metrics & Failure Logs
CREATE TABLE IF NOT EXISTS platform_reliability_failure_log (
    id                              BIGSERIAL PRIMARY KEY,
    asset_id                        BIGINT NOT NULL,
    mtbf_hours                      NUMERIC(10,2) NOT NULL,
    mttr_hours                      NUMERIC(10,2) NOT NULL,
    availability_rate               NUMERIC(5,2) NOT NULL,
    failure_rate                    NUMERIC(7,5) NOT NULL,
    reliability_score               NUMERIC(5,2) NOT NULL,
    repair_duration_minutes         INT NOT NULL,
    downtime_duration_minutes       INT NOT NULL,
    root_cause_category             VARCHAR(200) NOT NULL,
    failure_mode                    VARCHAR(200) NOT NULL,
    corrective_action               VARCHAR(500),
    reported_at                     TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
