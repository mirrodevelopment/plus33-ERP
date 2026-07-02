-- V354: Fuel Efficiency Advisor
CREATE TABLE IF NOT EXISTS platform_fuel_efficiency_advisor (
    id                          BIGSERIAL PRIMARY KEY,
    recommendation_type         VARCHAR(100) NOT NULL, -- EcoSpeedLimit, ReduceIdle, CruiseControl
    priority                    VARCHAR(50) NOT NULL,
    expected_fuel_saving_l      NUMERIC(10,2) NOT NULL,
    expected_cost_saving        NUMERIC(12,2) NOT NULL,
    expected_emission_reduce    NUMERIC(10,2) NOT NULL,
    generated_by                VARCHAR(100) NOT NULL,
    acknowledged                BOOLEAN NOT NULL DEFAULT FALSE,
    applied_at                  TIMESTAMP,
    created_at                  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
