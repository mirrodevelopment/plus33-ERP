-- V198: Saga DDL
CREATE TABLE IF NOT EXISTS integration_saga (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT DEFAULT 0,
    saga_code           VARCHAR(100) NOT NULL UNIQUE,
    saga_type           VARCHAR(100) NOT NULL,
    status              VARCHAR(50) NOT NULL DEFAULT 'CREATED',
    payload_json        TEXT,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS integration_saga_step (
    id                  BIGSERIAL PRIMARY KEY,
    saga_code           VARCHAR(100) NOT NULL,
    step_name           VARCHAR(100) NOT NULL,
    status              VARCHAR(50) NOT NULL,
    action_payload      TEXT,
    compensation_payload TEXT,
    execution_order     INT NOT NULL,
    executed_at         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
