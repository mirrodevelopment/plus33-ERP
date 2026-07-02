-- V342: Dispatch Assignments
CREATE TABLE IF NOT EXISTS platform_dispatch_assignment (
    id                          BIGSERIAL PRIMARY KEY,
    dispatch_code               VARCHAR(100) NOT NULL UNIQUE,
    vehicle_id                  BIGINT NOT NULL,
    driver_id                   BIGINT NOT NULL,
    route_id                    BIGINT NOT NULL,
    shipment_id                 BIGINT NOT NULL,
    assignment_status           VARCHAR(50) NOT NULL, -- CREATED, ASSIGNED, ACCEPTED, EN_ROUTE, DELIVERED, FAILED
    assigned_time               TIMESTAMP NOT NULL,
    accepted_time               TIMESTAMP,
    completed_time              TIMESTAMP,
    estimated_eta               TIMESTAMP NOT NULL,
    actual_eta                  TIMESTAMP
);
