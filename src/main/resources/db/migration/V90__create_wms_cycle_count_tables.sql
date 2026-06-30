-- ============================================================
-- V90__create_wms_cycle_count_tables.sql
-- PLUS33 ERP — Cycle Count Engine (ABC, Blind Count, Variance)
-- ============================================================

-- ============================================================
-- SECTION 1: CYCLE COUNT PLANS
-- ============================================================

CREATE TABLE cycle_count_plans (
    id              BIGSERIAL PRIMARY KEY,
    company_id      BIGINT          NOT NULL REFERENCES companies(id),
    warehouse_id    BIGINT          NOT NULL REFERENCES warehouses(id),
    plan_number     VARCHAR(50)     NOT NULL,
    plan_type       VARCHAR(30)     NOT NULL DEFAULT 'ABC',
        -- ABC, HIGH_VALUE, RANDOM, FULL_COUNT, BLIND, ZONE_BASED
    zone_id         BIGINT          REFERENCES warehouse_zones(id),
    abc_class       VARCHAR(5),      -- A, B, C — for ABC type
    status          VARCHAR(30)     NOT NULL DEFAULT 'DRAFT',
        -- DRAFT, SCHEDULED, IN_PROGRESS, COMPLETED, CANCELLED
    scheduled_date  DATE,
    started_at      TIMESTAMP,
    completed_at    TIMESTAMP,
    blind_count     BOOLEAN         NOT NULL DEFAULT FALSE,
    auto_approve_below_variance DECIMAL(5,2) NOT NULL DEFAULT 0.00,  -- % variance threshold for auto-approval
    notes           TEXT,
    created_by      BIGINT          REFERENCES users(id),
    approved_by     BIGINT          REFERENCES users(id),
    approved_at     TIMESTAMP,
    created_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT uk_cycle_count_plan UNIQUE (company_id, plan_number)
);

-- ============================================================
-- SECTION 2: CYCLE COUNT TASKS (per-bin)
-- ============================================================

CREATE TABLE cycle_count_tasks (
    id                  BIGSERIAL PRIMARY KEY,
    plan_id             BIGINT          NOT NULL REFERENCES cycle_count_plans(id),
    company_id          BIGINT          NOT NULL REFERENCES companies(id),
    location_id         BIGINT          NOT NULL REFERENCES warehouse_locations(id),
    product_id          BIGINT          NOT NULL REFERENCES products(id),
    lot_number          VARCHAR(50),
    serial_number       VARCHAR(100),
    -- System quantity at time task was created (hidden in blind count mode)
    system_quantity     DECIMAL(18,6)   NOT NULL,
    unit_id             BIGINT          NOT NULL REFERENCES units_of_measure(id),
    status              VARCHAR(30)     NOT NULL DEFAULT 'PENDING',
        -- PENDING, ASSIGNED, IN_PROGRESS, COUNTED, RECOUNT_REQUIRED, APPROVED, REJECTED
    assigned_to         BIGINT          REFERENCES users(id),
    assigned_at         TIMESTAMP,
    started_at          TIMESTAMP,
    completed_at        TIMESTAMP,
    recount_reason      TEXT,
    recount_count       INT             NOT NULL DEFAULT 0,
    created_at          TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_cycle_count_tasks_plan ON cycle_count_tasks(plan_id, status);
CREATE INDEX idx_cycle_count_tasks_location ON cycle_count_tasks(location_id, status);
CREATE INDEX idx_cycle_count_tasks_assignee ON cycle_count_tasks(assigned_to, status);

-- ============================================================
-- SECTION 3: CYCLE COUNT RESULTS
-- ============================================================

CREATE TABLE cycle_count_results (
    id                  BIGSERIAL PRIMARY KEY,
    task_id             BIGINT          NOT NULL REFERENCES cycle_count_tasks(id),
    plan_id             BIGINT          NOT NULL REFERENCES cycle_count_plans(id),
    company_id          BIGINT          NOT NULL REFERENCES companies(id),
    location_id         BIGINT          NOT NULL REFERENCES warehouse_locations(id),
    product_id          BIGINT          NOT NULL REFERENCES products(id),
    lot_number          VARCHAR(50),
    serial_number       VARCHAR(100),
    system_quantity     DECIMAL(18,6)   NOT NULL,
    counted_quantity    DECIMAL(18,6)   NOT NULL,
    variance_quantity   DECIMAL(18,6)   GENERATED ALWAYS AS (counted_quantity - system_quantity) STORED,
    variance_pct        DECIMAL(10,4),   -- computed and stored by service
    unit_id             BIGINT          NOT NULL REFERENCES units_of_measure(id),
    unit_cost           DECIMAL(18,6),
    variance_value      DECIMAL(18,2),   -- variance_quantity * unit_cost
    count_number        INT             NOT NULL DEFAULT 1,  -- supports recounts
    status              VARCHAR(30)     NOT NULL DEFAULT 'PENDING_APPROVAL',
        -- PENDING_APPROVAL, AUTO_APPROVED, APPROVED, REJECTED, RECOUNT_REQUESTED
    approval_notes      TEXT,
    approved_by         BIGINT          REFERENCES users(id),
    approved_at         TIMESTAMP,
    -- GL journal entry posted after approval
    gl_journal_id       BIGINT,
    counted_by          BIGINT          REFERENCES users(id),
    counted_at          TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    created_at          TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at          TIMESTAMP       NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_cycle_count_results_plan ON cycle_count_results(plan_id, status);
CREATE INDEX idx_cycle_count_results_product ON cycle_count_results(product_id);
CREATE INDEX idx_cycle_count_results_variance ON cycle_count_results(variance_quantity) WHERE variance_quantity != 0;
