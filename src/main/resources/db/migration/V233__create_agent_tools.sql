-- V233: Tool registry & execution history
CREATE TABLE IF NOT EXISTS platform_agent_tool (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    tool_code           VARCHAR(100) NOT NULL UNIQUE,
    description         TEXT NOT NULL,
    module_name         VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS platform_agent_tool_execution (
    id                  BIGSERIAL PRIMARY KEY,
    tool_id             BIGINT NOT NULL,
    executor_node       VARCHAR(100) NOT NULL,
    input_parameters    TEXT NOT NULL,
    output_response     TEXT,
    success             BOOLEAN NOT NULL,
    elapsed_ms          BIGINT NOT NULL,
    executed_at         TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
