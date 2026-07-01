-- V126: Supplier Performance Scorecards
CREATE TABLE IF NOT EXISTS procurement_supplier_scorecards (
    id BIGSERIAL PRIMARY KEY,
    supplier_id BIGINT NOT NULL UNIQUE,
    on_time_delivery_rate NUMERIC(5, 2) NOT NULL DEFAULT 100.00,
    quality_defect_rate NUMERIC(5, 2) NOT NULL DEFAULT 0.00,
    invoice_accuracy_rate NUMERIC(5, 2) NOT NULL DEFAULT 100.00,
    overall_rating NUMERIC(5, 2) NOT NULL DEFAULT 100.00,
    recalculated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
