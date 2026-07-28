-- ============================================================
-- PLUS33 Coffee ERP Migration: V441__seed_store_tasks_data.sql
-- Module  : Store Operations — Real Initial Task Seed Data
-- Purpose : Seed initial real store tasks into store_tasks table
-- ============================================================

INSERT INTO store_tasks (
    title, description, category, priority, status, delegation_mode, is_preemptive_immediate,
    due_date, extension_status, assigned_employee_id, assigned_employee_name, assigned_employee_email,
    created_by_user_id, created_by_name, creator_role, store_id, shift_id
) VALUES
(
    'Morning Espresso Grinder Calibration & Dial-In',
    'Calibrate La Marzocco grinder dose weight (18g in, 36g out in 27s) before morning rush.',
    'OPENING_CHECKLIST', 'IMMEDIATE', 'STARTED', 'DIRECT_EMPLOYEE', TRUE,
    CURRENT_TIMESTAMP + INTERVAL '2 hours', 'NONE',
    5, 'Neha Sharma', 'neha.sharma@plus33.com',
    4, 'Alex Rivers (Shift Supervisor)', 'shiftSupervisor', 1, 'MORNING_SHIFT'
),
(
    'Sanitize Milk Steaming Wands & Refrigerator Temp Check',
    'Ensure steamer wands purged and sub-4°C temp logged for dairy fridge.',
    'HYGIENE_CLEANING', 'IMPORTANT', 'PENDING', 'DIRECT_EMPLOYEE', FALSE,
    CURRENT_TIMESTAMP + INTERVAL '4 hours', 'NONE',
    5, 'Neha Sharma', 'neha.sharma@plus33.com',
    4, 'Alex Rivers (Shift Supervisor)', 'shiftSupervisor', 1, 'MORNING_SHIFT'
),
(
    'Oat & Almond Milk Stock Replenishment',
    'Restock front-bar undercounter fridge from backroom dry storage.',
    'STOCK_REPLENISHMENT', 'COMMON', 'COMPLETED', 'DIRECT_EMPLOYEE', FALSE,
    CURRENT_TIMESTAMP - INTERVAL '1 hour', 'NONE',
    5, 'Neha Sharma', 'neha.sharma@plus33.com',
    3, 'Store Manager', 'storeAdmin', 1, 'MORNING_SHIFT'
),
(
    'Mid-Day Espresso Grinder & Water Filtration Pressure Audit',
    'Inspect bar water pressure gauges and calibrate grinder dose consistency.',
    'EQUIPMENT_MAINTENANCE', 'IMPORTANT', 'ASSIGNED', 'SUPERVISOR_DELEGATION', FALSE,
    CURRENT_TIMESTAMP + INTERVAL '6 hours', 'NONE',
    4, 'Alex Rivers (Shift Supervisor)', 'alex.rivers@plus33.com',
    3, 'Store Manager', 'storeAdmin', 1, 'AFTERNOON_SHIFT'
),
(
    'Weekly Store Hygiene & Health Safety Audit Directive',
    'Execute comprehensive store hygiene audit across counter, seating area, and sanitization stations.',
    'SAFETY_AUDIT', 'IMPORTANT', 'ASSIGNED', 'SUPERVISOR_DELEGATION', FALSE,
    CURRENT_TIMESTAMP + INTERVAL '2 days', 'NONE',
    4, 'Alex Rivers (Shift Supervisor)', 'alex.rivers@plus33.com',
    3, 'Store Manager', 'storeAdmin', 1, 'ALL_SHIFTS'
);
