-- V259: Operational Query Logs
CREATE TABLE IF NOT EXISTS platform_operational_query_log (
    id                  BIGSERIAL PRIMARY KEY,
    query_text          TEXT NOT NULL,
    parsed_intent       VARCHAR(250) NOT NULL,
    execution_plan_json TEXT NOT NULL,
    queried_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
