-- V257: Optimization Strategies
CREATE TABLE IF NOT EXISTS platform_optimization_strategy (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    strategy_code       VARCHAR(100) NOT NULL UNIQUE,
    strategy_name       VARCHAR(150) NOT NULL,
    parameters_json     TEXT NOT NULL,
    active              BOOLEAN NOT NULL DEFAULT TRUE
);
