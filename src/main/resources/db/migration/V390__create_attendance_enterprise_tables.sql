-- 1. Create country_work_policies table
CREATE TABLE IF NOT EXISTS country_work_policies (
    id BIGSERIAL PRIMARY KEY,
    country_code VARCHAR(10) NOT NULL UNIQUE,
    weekly_required_hours NUMERIC(5,2) NOT NULL DEFAULT 35.00,
    overtime_threshold_hours NUMERIC(5,2) NOT NULL DEFAULT 35.00,
    overtime_rate NUMERIC(3,2) NOT NULL DEFAULT 1.25,
    night_start TIME NOT NULL DEFAULT '21:00:00',
    night_end TIME NOT NULL DEFAULT '06:00:00',
    night_overtime_rate NUMERIC(3,2) NOT NULL DEFAULT 1.50,
    holiday_overtime_rate NUMERIC(3,2) NOT NULL DEFAULT 2.00,
    grace_period_minutes INTEGER NOT NULL DEFAULT 15,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- 2. Alter attendance table to add extra columns
ALTER TABLE attendance ADD COLUMN IF NOT EXISTS late_minutes INTEGER DEFAULT 0;
ALTER TABLE attendance ADD COLUMN IF NOT EXISTS early_out_minutes INTEGER DEFAULT 0;
ALTER TABLE attendance ADD COLUMN IF NOT EXISTS gps_coordinates VARCHAR(100);
ALTER TABLE attendance ADD COLUMN IF NOT EXISTS device_info VARCHAR(255);

-- 3. Create attendance_breaks table
CREATE TABLE IF NOT EXISTS attendance_breaks (
    id BIGSERIAL PRIMARY KEY,
    attendance_id BIGINT NOT NULL,
    break_start TIMESTAMP NOT NULL,
    break_end TIMESTAMP,
    duration_minutes INTEGER DEFAULT 0,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_breaks_attendance FOREIGN KEY (attendance_id) REFERENCES attendance(id) ON DELETE CASCADE
);

-- 4. Create attendance_corrections table
CREATE TABLE IF NOT EXISTS attendance_corrections (
    id BIGSERIAL PRIMARY KEY,
    attendance_id BIGINT NOT NULL,
    employee_id BIGINT NOT NULL,
    request_date DATE NOT NULL,
    requested_check_in TIMESTAMP,
    requested_check_out TIMESTAMP,
    reason TEXT NOT NULL,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING',
    rejection_reason TEXT,
    approved_by BIGINT,
    approved_at TIMESTAMP,
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_corrections_attendance FOREIGN KEY (attendance_id) REFERENCES attendance(id) ON DELETE CASCADE,
    CONSTRAINT fk_corrections_employee FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE CASCADE,
    CONSTRAINT fk_corrections_approver FOREIGN KEY (approved_by) REFERENCES users(id)
);

-- 5. Create attendance_audit_trail table
CREATE TABLE IF NOT EXISTS attendance_audit_trail (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    attendance_id BIGINT,
    action_type VARCHAR(50) NOT NULL,
    timestamp TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    ip_address VARCHAR(50),
    device VARCHAR(255),
    previous_value TEXT,
    new_value TEXT,
    reason TEXT,
    CONSTRAINT fk_audit_user FOREIGN KEY (user_id) REFERENCES users(id),
    CONSTRAINT fk_audit_attendance FOREIGN KEY (attendance_id) REFERENCES attendance(id) ON DELETE SET NULL
);

-- Seed country policy
INSERT INTO country_work_policies (country_code, weekly_required_hours, overtime_threshold_hours, overtime_rate, night_start, night_end, night_overtime_rate, holiday_overtime_rate, grace_period_minutes)
VALUES ('FR', 35.00, 35.00, 1.25, '21:00:00'::TIME, '06:00:00'::TIME, 1.50, 2.00, 15)
ON CONFLICT (country_code) DO NOTHING;

-- Seed admin employee mapping
INSERT INTO employees (employee_code, user_id, company_id, first_name, last_name, email, phone, designation, department, employment_type, hire_date, status, active)
SELECT 'EMP-0000', id, 1, 'System', 'Administrator', 'admin@plus33.com', '+33-0-00000000', 'Administrator', 'IT', 'PERMANENT', '2024-01-15'::DATE, 'ACTIVE', TRUE
FROM users
WHERE email = 'admin@plus33.com'
  AND NOT EXISTS (SELECT 1 FROM employees WHERE employee_code = 'EMP-0000');

