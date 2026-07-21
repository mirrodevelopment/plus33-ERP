-- ============================================================
-- V424: Store GPS Coordinates + Away Permission Requests
-- PLUS33 Coffee ERP
-- ============================================================

-- GPS coordinates on the stores table (set once by store admin; persists until next explicit update)
ALTER TABLE stores ADD COLUMN IF NOT EXISTS latitude DECIMAL(10,8);
ALTER TABLE stores ADD COLUMN IF NOT EXISTS longitude DECIMAL(11,8);
ALTER TABLE stores ADD COLUMN IF NOT EXISTS geofence_radius_meters INT DEFAULT 200;

-- ============================================================
-- Away Permission Requests
-- Employees request to leave the 200m geofence during a shift.
-- Supervisor sets custom duration when approving.
-- A 10-minute grace buffer is applied on expiry before auto clock-out.
-- After grace expires, employee can request an extension.
-- ============================================================
CREATE TABLE IF NOT EXISTS away_permission_requests (
    id                      BIGSERIAL PRIMARY KEY,
    employee_id             BIGINT NOT NULL REFERENCES employees(id) ON DELETE CASCADE,
    store_id                BIGINT NOT NULL REFERENCES stores(id) ON DELETE CASCADE,
    attendance_id           BIGINT REFERENCES attendance(id) ON DELETE SET NULL,
    requested_at            TIMESTAMP NOT NULL DEFAULT NOW(),
    resolved_at             TIMESTAMP,
    resolved_by             BIGINT REFERENCES users(id) ON DELETE SET NULL,
    -- PENDING | APPROVED | DENIED | EXPIRED | EXTENSION_REQUESTED
    status                  VARCHAR(30) NOT NULL DEFAULT 'PENDING',
    reason                  TEXT,
    -- Set by supervisor when approving (in minutes)
    approved_duration_mins  INT,
    -- Computed: requested_at + approved_duration_mins
    approved_until          TIMESTAMP,
    -- Grace buffer: 10 minutes after approved_until before auto clock-out fires
    grace_buffer_mins       INT NOT NULL DEFAULT 10,
    -- When employee requests an extension after grace period starts
    extension_requested_at  TIMESTAMP,
    extension_reason        TEXT,
    -- Tracks if this is an extension of a previous request
    parent_request_id       BIGINT REFERENCES away_permission_requests(id) ON DELETE SET NULL,
    created_at              TIMESTAMP NOT NULL DEFAULT NOW(),
    updated_at              TIMESTAMP NOT NULL DEFAULT NOW()
);

CREATE INDEX IF NOT EXISTS idx_away_permission_employee ON away_permission_requests(employee_id);
CREATE INDEX IF NOT EXISTS idx_away_permission_store ON away_permission_requests(store_id);
CREATE INDEX IF NOT EXISTS idx_away_permission_attendance ON away_permission_requests(attendance_id);
CREATE INDEX IF NOT EXISTS idx_away_permission_status ON away_permission_requests(status);
