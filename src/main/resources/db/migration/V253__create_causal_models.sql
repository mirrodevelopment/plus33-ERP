-- V253: Causal Models
CREATE TABLE IF NOT EXISTS platform_causal_model (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    model_code          VARCHAR(100) NOT NULL UNIQUE,
    model_name          VARCHAR(150) NOT NULL,
    structure_json      TEXT NOT NULL,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
