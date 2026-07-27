-- ============================================================================
-- PLUS33 Coffee ERP — Database Migration V437
-- Description: Create support_tickets, anonymous_complaints, and support_knowledge_base
-- NOTE: Uses PostgreSQL syntax (BIGSERIAL, separate CREATE INDEX, no ON UPDATE)
-- ============================================================================

-- 1. Standard Support Tickets Table (Tech Support, HR & Payroll, General Feedback)
CREATE TABLE IF NOT EXISTS support_tickets (
    id BIGSERIAL PRIMARY KEY,
    ticket_code VARCHAR(30) NOT NULL UNIQUE,
    category VARCHAR(50) NOT NULL,
    subcategory VARCHAR(100),
    priority VARCHAR(20) NOT NULL DEFAULT 'MEDIUM',
    status VARCHAR(30) NOT NULL DEFAULT 'OPEN',
    subject VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    reporter_id BIGINT,
    reporter_name VARCHAR(100),
    reporter_role VARCHAR(50),
    store_id BIGINT,
    region_id BIGINT,
    assigned_to BIGINT,
    admin_response TEXT,
    resolved_at TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_st_ticket_code ON support_tickets (ticket_code);
CREATE INDEX IF NOT EXISTS idx_st_reporter_id ON support_tickets (reporter_id);
CREATE INDEX IF NOT EXISTS idx_st_store_id ON support_tickets (store_id);
CREATE INDEX IF NOT EXISTS idx_st_status ON support_tickets (status);
CREATE INDEX IF NOT EXISTS idx_st_category ON support_tickets (category);

-- 2. Separate Anonymous Complaints Table (Workplace Grievances, Harassment, Safety)
-- NOTE: Absolutely NO reporter_id, reporter_name, or user identification columns exist.
CREATE TABLE IF NOT EXISTS anonymous_complaints (
    id BIGSERIAL PRIMARY KEY,
    tracking_key VARCHAR(50) NOT NULL UNIQUE,
    category VARCHAR(50) NOT NULL DEFAULT 'WORKPLACE_COMPLAINT',
    subcategory VARCHAR(100),
    severity VARCHAR(20) NOT NULL DEFAULT 'HIGH',
    status VARCHAR(30) NOT NULL DEFAULT 'OPEN',
    subject VARCHAR(255) NOT NULL,
    description TEXT NOT NULL,
    store_id BIGINT,
    region_id BIGINT,
    compliance_response TEXT,
    response_published_at TIMESTAMP NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IF NOT EXISTS idx_ac_tracking_key ON anonymous_complaints (tracking_key);
CREATE INDEX IF NOT EXISTS idx_ac_status ON anonymous_complaints (status);
CREATE INDEX IF NOT EXISTS idx_ac_category ON anonymous_complaints (category);
CREATE INDEX IF NOT EXISTS idx_ac_store_id ON anonymous_complaints (store_id);

-- 3. Support Knowledge Base / FAQ Table
CREATE TABLE IF NOT EXISTS support_knowledge_base (
    id BIGSERIAL PRIMARY KEY,
    category VARCHAR(50) NOT NULL,
    question VARCHAR(255) NOT NULL,
    answer TEXT NOT NULL,
    target_role VARCHAR(50) DEFAULT 'ALL',
    sort_order INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Seed initial Knowledge Base FAQs
INSERT INTO support_knowledge_base (category, question, answer, target_role, sort_order) VALUES
('ATTENDANCE', 'How do I clock in using GPS geofencing?', 'Navigate to Workplace -> Attendance or Dashboard. Grant browser location permissions when prompted. Click "Clock In". Your distance from the store geofence will be verified automatically.', 'ALL', 1),
('LEAVE', 'What is the approval workflow for casual leaves?', 'Casual leave requests submitted by store employees are automatically routed to your Shift Supervisor or Store Manager for review. Once approved, your leave balance updates instantly.', 'storeEmployee', 2),
('POS', 'What should I do if the POS machine loses internet connection?', 'The POS operates in Offline Mesh Mode for up to 4 hours. Offline sales will buffer locally and sync automatically to the cloud once network connectivity is restored.', 'ALL', 3),
('SAFETY', 'How does the Anonymous Workplace Complaint system protect me?', 'When you submit an anonymous complaint, no personal info or user ID is saved. A 12-character secret Tracking Key is generated. Use this key to track investigation status privately.', 'ALL', 4),
('PAYROLL', 'When are monthly payslips generated?', 'Payslips are generated on the 1st of every month after monthly attendance reconciliation by HR.', 'ALL', 5);

-- Seed initial sample support tickets
INSERT INTO support_tickets (ticket_code, category, subcategory, priority, status, subject, description, reporter_name, reporter_role, store_id) VALUES
('TK-2026-0001', 'TECH_SUPPORT', 'POS Glitch', 'MEDIUM', 'OPEN', 'Barista station 2 scanner delay', 'Barcode scanner on station 2 takes 3-4 seconds to respond during peak morning rush.', 'Barista Alex', 'storeEmployee', 1),
('TK-2026-0002', 'HR_PAYROLL', 'Payslip Inquiry', 'LOW', 'RESOLVED', 'Overtime calculation clarification for weekend shift', 'Requested breakdown of 1.5x overtime multiplier applied on Sunday July 12th.', 'Barista Sam', 'storeEmployee', 1);

-- Seed initial sample anonymous complaints (strictly decoupled from any reporter)
INSERT INTO anonymous_complaints (tracking_key, category, severity, status, subject, description, store_id, compliance_response, response_published_at) VALUES
('TK-ANO-89F2A1', 'HARASSMENT', 'CRITICAL', 'UNDER_INVESTIGATION', 'Unfair shift distribution and verbal misconduct', 'Repeated pressure during evening cleanup shifts with aggressive conduct.', 1, 'Senior Compliance Officer assigned. Initial workplace review commenced.', CURRENT_TIMESTAMP),
('TK-ANO-3B4E91', 'SAFETY_HAZARD', 'HIGH', 'ACTION_TAKEN', 'Exposed electrical wiring near espresso machine 2', 'Water leakage risk near power outlet behind station 2 bar.', 1, 'Store maintenance dispatched. Waterproof junction box installed on July 25.', CURRENT_TIMESTAMP);
