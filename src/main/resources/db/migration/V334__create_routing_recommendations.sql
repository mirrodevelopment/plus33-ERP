-- V334: Optimization Recommendations
CREATE TABLE IF NOT EXISTS platform_routing_optimization_recommendation (
    id                      BIGSERIAL PRIMARY KEY,
    recommended_route       TEXT NOT NULL,
    estimated_savings_cost  NUMERIC(12,2) NOT NULL,
    estimated_time_saved_m  INT NOT NULL,
    estimated_fuel_saved_l  NUMERIC(10,2) NOT NULL,
    estimated_co2_saved_kg  NUMERIC(10,2) NOT NULL,
    confidence_score        NUMERIC(5,2) NOT NULL,
    algorithm_version       VARCHAR(50) NOT NULL,
    accepted                BOOLEAN NOT NULL DEFAULT FALSE,
    implemented             BOOLEAN NOT NULL DEFAULT FALSE,
    created_at              TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
