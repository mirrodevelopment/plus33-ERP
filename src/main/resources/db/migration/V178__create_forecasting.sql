-- V178: Forecasting Framework
-- Purpose: Forecast model registry, execution runs, predictions storage,
--          and accuracy/backtesting tracking.

-- -----------------------------------------------------------------------------
-- FORECAST MODEL REGISTRY: registered forecasting strategies
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS bi_forecast_model_registry (
    id                  BIGSERIAL PRIMARY KEY,
    model_code          VARCHAR(100) NOT NULL UNIQUE,
    model_name          VARCHAR(200) NOT NULL,
    model_type          VARCHAR(50) NOT NULL DEFAULT 'LINEAR',
    forecast_domain     VARCHAR(50) NOT NULL,
    is_active           BOOLEAN NOT NULL DEFAULT TRUE,
    is_default          BOOLEAN NOT NULL DEFAULT FALSE,
    accuracy_score      NUMERIC(5,2),
    description         TEXT,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- -----------------------------------------------------------------------------
-- FORECAST RUN: individual forecasting execution instance
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS bi_forecast_run (
    id                  BIGSERIAL PRIMARY KEY,
    company_id          BIGINT NOT NULL,
    model_id            BIGINT NOT NULL REFERENCES bi_forecast_model_registry(id),
    forecast_domain     VARCHAR(50) NOT NULL,
    run_reference       VARCHAR(100) NOT NULL UNIQUE,
    horizon_months      INTEGER NOT NULL DEFAULT 12,
    training_from       DATE,
    training_to         DATE,
    forecast_from       DATE NOT NULL,
    forecast_to         DATE NOT NULL,
    status              VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    mae                 NUMERIC(19,4),
    rmse                NUMERIC(19,4),
    accuracy_pct        NUMERIC(5,2),
    started_at          TIMESTAMP,
    completed_at        TIMESTAMP,
    error_message       TEXT,
    triggered_by        VARCHAR(100),
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- -----------------------------------------------------------------------------
-- FORECAST PREDICTION: individual period predictions
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS bi_forecast_prediction (
    id                  BIGSERIAL PRIMARY KEY,
    run_id              BIGINT NOT NULL REFERENCES bi_forecast_run(id),
    company_id          BIGINT NOT NULL,
    forecast_date       DATE NOT NULL,
    predicted_value     NUMERIC(19,4) NOT NULL,
    lower_bound         NUMERIC(19,4),
    upper_bound         NUMERIC(19,4),
    confidence_level    NUMERIC(5,2) DEFAULT 95,
    actual_value        NUMERIC(19,4),
    variance_pct        NUMERIC(10,4),
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- -----------------------------------------------------------------------------
-- FORECAST ACCURACY HISTORY: per-model accuracy tracking over time
-- -----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS bi_forecast_accuracy_history (
    id                  BIGSERIAL PRIMARY KEY,
    model_id            BIGINT NOT NULL REFERENCES bi_forecast_model_registry(id),
    company_id          BIGINT NOT NULL,
    evaluated_at        DATE NOT NULL,
    mae                 NUMERIC(19,4),
    rmse                NUMERIC(19,4),
    accuracy_pct        NUMERIC(5,2),
    sample_size         INTEGER,
    notes               TEXT,
    created_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Seed default forecast model registry
INSERT INTO bi_forecast_model_registry (model_code, model_name, model_type, forecast_domain, is_active, is_default, description) VALUES
('REVENUE_LINEAR',      'Revenue Linear Trend',     'LINEAR',       'REVENUE',      TRUE, TRUE,  'Simple linear regression on monthly revenue'),
('DEMAND_MOVING_AVG',   'Demand Moving Average',    'MOVING_AVG',   'DEMAND',       TRUE, TRUE,  '3-month moving average for product demand'),
('CASH_EXPONENTIAL',    'Cash Exponential Smoothing','EXPONENTIAL',  'CASH',         TRUE, TRUE,  'Exponential smoothing for cash flow forecasting'),
('ATTRITION_LOGISTIC',  'Attrition Risk Model',     'LOGISTIC',     'ATTRITION',    TRUE, TRUE,  'Logistic model for employee attrition probability')
ON CONFLICT (model_code) DO NOTHING;
