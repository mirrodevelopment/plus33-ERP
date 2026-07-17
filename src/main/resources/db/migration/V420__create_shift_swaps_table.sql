-- Create shift swaps table for employee shift trade and escalation flow
CREATE TABLE IF NOT EXISTS employee_shift_swaps (
    id BIGSERIAL PRIMARY KEY,
    employee_id BIGINT NOT NULL REFERENCES employees(id),
    shift_date DATE NOT NULL,
    current_shift_id BIGINT NOT NULL REFERENCES shifts(id),
    preferred_shift_id BIGINT NOT NULL REFERENCES shifts(id),
    status VARCHAR(50) NOT NULL DEFAULT 'PENDING',
    rejection_reason VARCHAR(255),
    admin_rejection_reason VARCHAR(255),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);
