-- ============================================================
-- V425: Overtime Shift Requests Table
-- PLUS33 Coffee ERP
-- ============================================================

CREATE TABLE IF NOT EXISTS overtime_requests (
    id              BIGSERIAL PRIMARY KEY,
    employee_id     BIGINT NOT NULL REFERENCES employees(id) ON DELETE CASCADE,
    store_id        BIGINT NOT NULL REFERENCES stores(id) ON DELETE CASCADE,
    requested_date  DATE NOT NULL,
    shift_id        BIGINT NOT NULL REFERENCES shifts(id) ON DELETE CASCADE,
    reason          TEXT,
    -- PENDING | APPROVED | DENIED
    status          VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    resolved_at     TIMESTAMP,
    resolved_by     BIGINT REFERENCES users(id) ON DELETE SET NULL,
    created_at      TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at      TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_overtime_req_employee ON overtime_requests(employee_id);
CREATE INDEX IF NOT EXISTS idx_overtime_req_store ON overtime_requests(store_id);
CREATE INDEX IF NOT EXISTS idx_overtime_req_status ON overtime_requests(status);
CREATE INDEX IF NOT EXISTS idx_overtime_req_date ON overtime_requests(requested_date);
