-- V331: Route Optimization Policies
CREATE TABLE IF NOT EXISTS platform_route_optimization_policy (
    id                      BIGSERIAL PRIMARY KEY,
    version                 INT NOT NULL DEFAULT 0,
    policy_code             VARCHAR(100) NOT NULL UNIQUE,
    policy_name             VARCHAR(200) NOT NULL,
    optimization_strategy   VARCHAR(100) NOT NULL, -- ShortestDistance, MinCost, EcoFriendly
    vehicle_constraints     TEXT,
    driver_constraints      TEXT,
    priority                INT NOT NULL DEFAULT 0,
    time_window_minutes     INT NOT NULL,
    maximum_distance_km     NUMERIC(10,2) NOT NULL,
    maximum_duration_mins   INT NOT NULL,
    maximum_load_kg         NUMERIC(10,2) NOT NULL,
    traffic_weight          NUMERIC(5,2) NOT NULL,
    fuel_weight             NUMERIC(5,2) NOT NULL,
    carbon_weight           NUMERIC(5,2) NOT NULL,
    cost_weight             NUMERIC(5,2) NOT NULL,
    enabled                 BOOLEAN NOT NULL DEFAULT TRUE,
    created_by              VARCHAR(100) NOT NULL,
    created_at              TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
