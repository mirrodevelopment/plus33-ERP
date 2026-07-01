-- V247: Simulation scenarios
CREATE TABLE IF NOT EXISTS platform_twin_simulation_scenario (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    scenario_code       VARCHAR(100) NOT NULL UNIQUE,
    scenario_name       VARCHAR(150) NOT NULL,
    config_variables    TEXT NOT NULL
);

CREATE TABLE IF NOT EXISTS platform_twin_simulation_result (
    id                  BIGSERIAL PRIMARY KEY,
    scenario_id         BIGINT NOT NULL,
    simulation_output   TEXT NOT NULL,
    confidence          NUMERIC(5,2) NOT NULL,
    simulated_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
