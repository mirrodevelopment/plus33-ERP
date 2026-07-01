-- V185: AI Generative Insights and Performance Commentary DDL
CREATE TABLE IF NOT EXISTS bi_ai_insight_generation (
    id                  BIGSERIAL PRIMARY KEY,
    insight_code        VARCHAR(100) NOT NULL UNIQUE,
    company_id          BIGINT NOT NULL,
    kpi_code            VARCHAR(100) NOT NULL,
    source_snapshot_id  BIGINT,
    generated_model     VARCHAR(100) NOT NULL,
    confidence_score    NUMERIC(5,2),
    narrative_text      TEXT NOT NULL,
    generated_at        TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    accepted_by_user    BOOLEAN DEFAULT FALSE,
    feedback_rating     INTEGER CHECK (feedback_rating >= 1 AND feedback_rating <= 5)
);