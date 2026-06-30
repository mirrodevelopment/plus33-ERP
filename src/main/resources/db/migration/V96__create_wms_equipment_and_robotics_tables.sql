-- V96: Equipment Assets, Maintenance & Autonomous Robotics Tables
CREATE TABLE IF NOT EXISTS equipment_assets (
    id BIGSERIAL PRIMARY KEY,
    company_id BIGINT NOT NULL,
    warehouse_id BIGINT NOT NULL,
    asset_code VARCHAR(50) NOT NULL UNIQUE,
    equipment_type VARCHAR(30) NOT NULL, -- FORKLIFT, REACH_TRUCK, AGV, AMR, DRONE, CONVEYOR
    battery_level_pct INT DEFAULT 100,
    status VARCHAR(30) NOT NULL DEFAULT 'AVAILABLE',
    last_maintenance_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS robot_tasks (
    id BIGSERIAL PRIMARY KEY,
    equipment_id BIGINT REFERENCES equipment_assets(id),
    warehouse_task_id BIGINT NOT NULL,
    robot_provider_key VARCHAR(50) NOT NULL, -- AGV_MAIN, AMR_FETCH, DRONE_SCAN
    payload_json TEXT,
    status VARCHAR(30) NOT NULL DEFAULT 'DISPATCHED',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
