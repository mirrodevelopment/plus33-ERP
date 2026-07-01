-- V113: Work Order Tasks, Checklists, and technician notes
CREATE TABLE IF NOT EXISTS esm_work_order_tasks (
    id BIGSERIAL PRIMARY KEY,
    work_order_id BIGINT NOT NULL,
    task_sequence INT NOT NULL,
    task_description VARCHAR(255) NOT NULL,
    estimated_minutes INT NOT NULL,
    actual_minutes INT,
    status VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    required_skill VARCHAR(50)
);

CREATE TABLE IF NOT EXISTS esm_checklists (
    id BIGSERIAL PRIMARY KEY,
    task_id BIGINT NOT NULL,
    item_description VARCHAR(255) NOT NULL,
    checked BOOLEAN NOT NULL DEFAULT FALSE
);
