-- ============================================================
-- PLUS33 Coffee ERP Migration: V440__create_store_tasks_and_reports.sql
-- Module  : Store & Employee Operations — Task Management
-- Purpose : Database table creation for store operational tasks,
--           subtask hierarchies, status tracking, deadline extension requests,
--           and task execution reports.
-- ============================================================

CREATE TABLE IF NOT EXISTS store_tasks (
    id BIGSERIAL PRIMARY KEY,
    parent_task_id BIGINT REFERENCES store_tasks(id) ON DELETE SET NULL,
    title VARCHAR(255) NOT NULL,
    description TEXT,
    category VARCHAR(64) NOT NULL DEFAULT 'GENERAL',
    priority VARCHAR(32) NOT NULL DEFAULT 'COMMON',
    status VARCHAR(32) NOT NULL DEFAULT 'PENDING',
    delegation_mode VARCHAR(32) NOT NULL DEFAULT 'DIRECT_EMPLOYEE',
    is_preemptive_immediate BOOLEAN NOT NULL DEFAULT FALSE,
    paused_task_id BIGINT REFERENCES store_tasks(id) ON DELETE SET NULL,
    due_date TIMESTAMP WITH TIME ZONE,
    extension_status VARCHAR(32) NOT NULL DEFAULT 'NONE',
    requested_due_date TIMESTAMP WITH TIME ZONE,
    extension_reason TEXT,
    extension_review_notes TEXT,
    assigned_employee_id BIGINT,
    assigned_employee_name VARCHAR(128),
    assigned_employee_email VARCHAR(128),
    created_by_user_id BIGINT,
    created_by_name VARCHAR(128),
    creator_role VARCHAR(64),
    store_id BIGINT,
    shift_id VARCHAR(64),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS store_task_reports (
    id BIGSERIAL PRIMARY KEY,
    task_id BIGINT NOT NULL REFERENCES store_tasks(id) ON DELETE CASCADE,
    reporter_user_id BIGINT,
    reporter_name VARCHAR(128),
    status VARCHAR(32) NOT NULL,
    completion_percentage INT DEFAULT 0,
    progress_notes TEXT,
    attachment_url TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Performance Indexes
CREATE INDEX IF NOT EXISTS idx_store_tasks_assigned_email ON store_tasks(assigned_employee_email);
CREATE INDEX IF NOT EXISTS idx_store_tasks_store_id ON store_tasks(store_id);
CREATE INDEX IF NOT EXISTS idx_store_tasks_status ON store_tasks(status);
CREATE INDEX IF NOT EXISTS idx_store_tasks_due_date ON store_tasks(due_date);
CREATE INDEX IF NOT EXISTS idx_store_tasks_parent_id ON store_tasks(parent_task_id);
CREATE INDEX IF NOT EXISTS idx_store_task_reports_task_id ON store_task_reports(task_id);
