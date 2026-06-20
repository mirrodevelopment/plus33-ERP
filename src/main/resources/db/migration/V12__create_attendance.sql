-- ============================================================
-- V12__create_attendance.sql
-- PLUS33 ERP — Attendance Master Table
-- ============================================================

CREATE TABLE attendance (
    id BIGSERIAL PRIMARY KEY,

    employee_id BIGINT NOT NULL,
    shift_id BIGINT NOT NULL,

    attendance_date DATE NOT NULL,

    check_in_time TIMESTAMP,
    check_out_time TIMESTAMP,

    status VARCHAR(30) NOT NULL,

    work_minutes INTEGER DEFAULT 0,
    overtime_minutes INTEGER DEFAULT 0,

    notes TEXT,

    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_attendance_employee
        FOREIGN KEY (employee_id)
        REFERENCES employees(id),

    CONSTRAINT fk_attendance_shift
        FOREIGN KEY (shift_id)
        REFERENCES shifts(id),

    CONSTRAINT uk_attendance_employee_date
        UNIQUE (employee_id, attendance_date)
);

CREATE INDEX idx_attendance_employee ON attendance(employee_id);
CREATE INDEX idx_attendance_date ON attendance(attendance_date);
CREATE INDEX idx_attendance_status ON attendance(status);
