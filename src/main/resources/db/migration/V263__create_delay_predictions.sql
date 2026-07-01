-- V263: Delay Prediction
CREATE TABLE IF NOT EXISTS platform_logistics_delay_prediction (
    id                  BIGSERIAL PRIMARY KEY,
    transit_route_id    BIGINT NOT NULL,
    prediction_model    VARCHAR(100) NOT NULL,
    prediction_confidence NUMERIC(5,2) NOT NULL,
    predicted_arrival   TIMESTAMP NOT NULL,
    actual_arrival      TIMESTAMP,
    generated_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
