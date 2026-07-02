-- V362: EV Battery Health Diagnostics
CREATE TABLE IF NOT EXISTS platform_ev_battery_health_log (
    id                          BIGSERIAL PRIMARY KEY,
    vehicle_id                  BIGINT NOT NULL,
    degradation_percentage      NUMERIC(5,2) NOT NULL,
    internal_resistance_mohm    NUMERIC(7,2) NOT NULL,
    cell_voltage_variance       NUMERIC(5,3) NOT NULL,
    thermal_balance_score       NUMERIC(5,2) NOT NULL,
    estimated_remaining_cycles  INT NOT NULL,
    estimated_end_of_life       TIMESTAMP NOT NULL,
    health_score                NUMERIC(5,2) NOT NULL,
    diagnostic_version          VARCHAR(50) NOT NULL,
    prediction_confidence       NUMERIC(5,2) NOT NULL,
    logged_at                   TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
