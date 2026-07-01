-- V265: Route Modification Audits
CREATE TABLE IF NOT EXISTS platform_route_audit_log (
    id                  BIGSERIAL PRIMARY KEY,
    transit_route_id    BIGINT NOT NULL,
    old_route_json      TEXT NOT NULL,
    new_route_json      TEXT NOT NULL,
    reason              TEXT NOT NULL,
    trigger_type        VARCHAR(100) NOT NULL, -- AUTONOMOUS, MANUAL
    operator            VARCHAR(100) NOT NULL,
    changed_at          TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
