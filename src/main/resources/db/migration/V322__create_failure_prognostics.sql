-- V322: Failure Prognostics
CREATE TABLE IF NOT EXISTS platform_failure_prognostics_log (
    id                              BIGSERIAL PRIMARY KEY,
    asset_id                        BIGINT NOT NULL,
    prediction_time                 TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    predicted_failure_time          TIMESTAMP,
    remaining_useful_life_hours     NUMERIC(10,2) NOT NULL,
    failure_probability             NUMERIC(5,2) NOT NULL,
    confidence_score                NUMERIC(5,2) NOT NULL,
    prediction_model_version        VARCHAR(50) NOT NULL,
    trigger_reason                  VARCHAR(500),
    recommended_action              VARCHAR(500)
);
