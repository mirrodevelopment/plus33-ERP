-- V260: Explainable Decision Lineage
CREATE TABLE IF NOT EXISTS platform_xai_lineage (
    id                  BIGSERIAL PRIMARY KEY,
    version             INT NOT NULL DEFAULT 0,
    decision_key        VARCHAR(150) NOT NULL UNIQUE,
    contributing_factors TEXT NOT NULL,
    model_version       VARCHAR(50) NOT NULL,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
