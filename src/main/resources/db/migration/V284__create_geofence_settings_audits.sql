-- V284: Geofence Settings Audits
CREATE TABLE IF NOT EXISTS platform_geofence_audit_log (
    id                  BIGSERIAL PRIMARY KEY,
    geofence_id         BIGINT NOT NULL,
    operator            VARCHAR(100) NOT NULL,
    action_type         VARCHAR(100) NOT NULL, -- CREATE, UPDATE, DELETE
    previous_geometry_wkt TEXT,
    new_geometry_wkt     TEXT,
    approval_id         VARCHAR(100),
    trace_id            VARCHAR(100),
    audited_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
