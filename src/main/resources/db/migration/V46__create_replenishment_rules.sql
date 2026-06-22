-- ============================================================
-- V46__create_replenishment_rules.sql
-- PLUS33 ERP — Inventory Replenishment Rules & Suggestions
-- ============================================================

-- 1. replenishment_rules table
CREATE TABLE replenishment_rules (
    id                  BIGSERIAL PRIMARY KEY,
    company_id          BIGINT NOT NULL,
    product_id          BIGINT NOT NULL,
    warehouse_id        BIGINT,
    store_id            BIGINT,
    min_quantity        NUMERIC(12,2) NOT NULL DEFAULT 0,
    max_quantity        NUMERIC(12,2) NOT NULL,
    reorder_point       NUMERIC(12,2) NOT NULL,
    reorder_quantity    NUMERIC(12,2) NOT NULL,
    lead_time_days      INTEGER NOT NULL DEFAULT 0,
    active              BOOLEAN NOT NULL DEFAULT TRUE,
    client_reference_id UUID NOT NULL UNIQUE,
    version             BIGINT NOT NULL DEFAULT 0,
    created_at          TIMESTAMP NOT NULL DEFAULT now(),
    updated_at          TIMESTAMP NOT NULL DEFAULT now(),

    CONSTRAINT fk_repl_rules_company  FOREIGN KEY (company_id)  REFERENCES companies(id),
    CONSTRAINT fk_repl_rules_product  FOREIGN KEY (product_id)  REFERENCES products(id),
    CONSTRAINT fk_repl_rules_wh       FOREIGN KEY (warehouse_id) REFERENCES warehouses(id),
    CONSTRAINT fk_repl_rules_store    FOREIGN KEY (store_id)     REFERENCES stores(id),

    -- Enhancement #7: include reorder_quantity <= max_quantity
    CONSTRAINT chk_rule_quantities CHECK (
        min_quantity        >= 0
        AND reorder_point   >= min_quantity
        AND max_quantity    >  reorder_point
        AND reorder_quantity > 0
        AND reorder_quantity <= max_quantity
    ),
    CONSTRAINT chk_rule_location CHECK (
        (warehouse_id IS NOT NULL AND store_id IS NULL)
        OR
        (store_id IS NOT NULL AND warehouse_id IS NULL)
    ),
    -- Prevent duplicate rules for the same product + location
    CONSTRAINT uq_replenishment_rule
        UNIQUE (product_id, warehouse_id, store_id)
);

-- 2. replenishment_suggestions table
CREATE TABLE replenishment_suggestions (
    id                  BIGSERIAL PRIMARY KEY,
    rule_id             BIGINT NOT NULL,
    company_id          BIGINT NOT NULL,
    product_id          BIGINT NOT NULL,
    warehouse_id        BIGINT,
    store_id            BIGINT,

    -- Enhancement #4: full evaluation-context snapshot
    current_quantity    NUMERIC(12,2) NOT NULL,
    reserved_quantity   NUMERIC(12,2) NOT NULL DEFAULT 0,
    available_quantity  NUMERIC(12,2) NOT NULL,
    suggested_quantity  NUMERIC(12,2) NOT NULL,

    status              VARCHAR(30) NOT NULL DEFAULT 'OPEN',

    -- Enhancement #3: evaluation source tracking
    evaluation_source   VARCHAR(20) NOT NULL DEFAULT 'MANUAL',

    purchase_request_id BIGINT,
    transfer_id         BIGINT,
    client_reference_id UUID NOT NULL UNIQUE,
    version             BIGINT NOT NULL DEFAULT 0,
    evaluated_at        TIMESTAMP NOT NULL DEFAULT now(),
    acknowledged_at     TIMESTAMP,
    ordered_at          TIMESTAMP,
    fulfilled_at        TIMESTAMP,
    cancelled_at        TIMESTAMP,
    notes               TEXT,
    created_at          TIMESTAMP NOT NULL DEFAULT now(),
    updated_at          TIMESTAMP NOT NULL DEFAULT now(),

    CONSTRAINT fk_repl_sugg_rule    FOREIGN KEY (rule_id)             REFERENCES replenishment_rules(id),
    CONSTRAINT fk_repl_sugg_company FOREIGN KEY (company_id)          REFERENCES companies(id),
    CONSTRAINT fk_repl_sugg_product FOREIGN KEY (product_id)          REFERENCES products(id),
    CONSTRAINT fk_repl_sugg_wh      FOREIGN KEY (warehouse_id)        REFERENCES warehouses(id),
    CONSTRAINT fk_repl_sugg_store   FOREIGN KEY (store_id)            REFERENCES stores(id),
    CONSTRAINT fk_repl_sugg_pr      FOREIGN KEY (purchase_request_id) REFERENCES purchase_requests(id),
    CONSTRAINT fk_repl_sugg_xfer    FOREIGN KEY (transfer_id)         REFERENCES inventory_transfers(id),

    CONSTRAINT chk_sugg_status CHECK (
        status IN ('OPEN', 'ACKNOWLEDGED', 'ORDERED', 'FULFILLED', 'CANCELLED')
    ),
    CONSTRAINT chk_sugg_eval_source CHECK (
        evaluation_source IN ('MANUAL', 'SCHEDULED', 'EVENT_DRIVEN')
    )
);

-- 3. Company-location validation trigger on replenishment_rules
CREATE OR REPLACE FUNCTION validate_replenishment_rule_company()
RETURNS TRIGGER AS $$
DECLARE
    loc_company_id BIGINT;
BEGIN
    IF NEW.warehouse_id IS NOT NULL THEN
        SELECT r.company_id INTO loc_company_id
        FROM warehouses w
        JOIN regions r ON w.region_id = r.id
        WHERE w.id = NEW.warehouse_id;
    ELSE
        SELECT r.company_id INTO loc_company_id
        FROM stores s
        JOIN regions r ON s.region_id = r.id
        WHERE s.id = NEW.store_id;
    END IF;

    IF loc_company_id IS NULL THEN
        RAISE EXCEPTION 'Invalid location reference on replenishment rule';
    END IF;

    IF loc_company_id <> NEW.company_id THEN
        RAISE EXCEPTION 'Replenishment rule location must belong to company %', NEW.company_id;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_validate_replenishment_rule
    BEFORE INSERT OR UPDATE ON replenishment_rules
    FOR EACH ROW EXECUTE FUNCTION validate_replenishment_rule_company();

-- 4. Company-consistency validation trigger on replenishment_suggestions
CREATE OR REPLACE FUNCTION validate_replenishment_suggestion_company()
RETURNS TRIGGER AS $$
DECLARE
    rule_company_id BIGINT;
BEGIN
    SELECT company_id INTO rule_company_id
    FROM replenishment_rules
    WHERE id = NEW.rule_id;

    IF rule_company_id IS NULL THEN
        RAISE EXCEPTION 'Invalid rule reference on replenishment suggestion';
    END IF;

    IF rule_company_id <> NEW.company_id THEN
        RAISE EXCEPTION 'Suggestion company must match parent rule company %', rule_company_id;
    END IF;

    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_validate_replenishment_suggestion
    BEFORE INSERT OR UPDATE ON replenishment_suggestions
    FOR EACH ROW EXECUTE FUNCTION validate_replenishment_suggestion_company();

-- 5. Updated_at triggers
CREATE OR REPLACE FUNCTION update_replenishment_updated_at()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = now();
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER trg_replenishment_rules_updated_at
    BEFORE UPDATE ON replenishment_rules
    FOR EACH ROW EXECUTE FUNCTION update_replenishment_updated_at();

CREATE TRIGGER trg_replenishment_suggestions_updated_at
    BEFORE UPDATE ON replenishment_suggestions
    FOR EACH ROW EXECUTE FUNCTION update_replenishment_updated_at();

-- 6. Indexes for replenishment_rules
CREATE INDEX idx_repl_rules_company   ON replenishment_rules(company_id);
CREATE INDEX idx_repl_rules_product   ON replenishment_rules(product_id);
CREATE INDEX idx_repl_rules_wh        ON replenishment_rules(warehouse_id)  WHERE warehouse_id IS NOT NULL;
CREATE INDEX idx_repl_rules_store     ON replenishment_rules(store_id)      WHERE store_id IS NOT NULL;
CREATE INDEX idx_repl_rules_active    ON replenishment_rules(active)        WHERE active = TRUE;
CREATE INDEX idx_repl_rules_client_ref ON replenishment_rules(client_reference_id);

-- 7. Indexes for replenishment_suggestions
CREATE INDEX idx_repl_sugg_rule       ON replenishment_suggestions(rule_id);
CREATE INDEX idx_repl_sugg_status     ON replenishment_suggestions(status);
CREATE INDEX idx_repl_sugg_product    ON replenishment_suggestions(product_id);
CREATE INDEX idx_repl_sugg_client_ref ON replenishment_suggestions(client_reference_id);
CREATE INDEX idx_repl_sugg_company    ON replenishment_suggestions(company_id);

-- Enhancement #2: partial unique index — only one OPEN or ACKNOWLEDGED suggestion per rule at a time
CREATE UNIQUE INDEX uq_replenishment_active_suggestion
    ON replenishment_suggestions(rule_id)
    WHERE status IN ('OPEN', 'ACKNOWLEDGED');
