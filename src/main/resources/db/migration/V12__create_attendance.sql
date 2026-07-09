-- ============================================================================
-- Project           : PLUS33 Coffee ERP
-- Developed By      : Haulo
-- Developed For     : PLUS33 Coffee
-- Developer         : Sivasurya
--
-- Migration Version : 12
-- File              : V12__create_attendance.sql
-- Operation Type    : Schema Creation
-- Purpose           : create attendance
--
-- Tables Created    : attendance
-- Tables Altered    : N/A
-- Seed Data For     : N/A
-- Indexes           : idx_attendance_employee, idx_attendance_date, idx_attendance_status
--
-- Notes
-- ----------------------------------------------------------------------------
-- Flyway migration applied automatically on application startup.
-- Do NOT modify after applying to any environment.
-- ============================================================================
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
