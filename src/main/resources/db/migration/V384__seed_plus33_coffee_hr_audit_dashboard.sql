-- ============================================================
-- V384__seed_plus33_coffee_hr_audit_dashboard.sql
-- PLUS33 ERP — Payroll, Attendance, Leaves, Docs, Audit, BI Snaps
-- ============================================================

DO $$
DECLARE
    v_company_id BIGINT;
    v_now TIMESTAMP := NOW();
    v_admin_id BIGINT;
    
    -- Payroll Periods
    v_p1_id BIGINT;
    v_p2_id BIGINT;
    v_p3_id BIGINT;

    -- Leave Types
    v_lt_annual BIGINT;
    v_lt_sick BIGINT;

    -- Iteration variables
    rec RECORD;
    v_date DATE;
    v_checkin TIMESTAMP;
    v_checkout TIMESTAMP;
    v_status VARCHAR(20);
    v_work_min INT;
    v_ot_min INT;
    
    v_counter INT := 0;
BEGIN
    SELECT id INTO v_company_id FROM companies WHERE code = 'PLUS33_COFFEE';
    
    SELECT id INTO v_admin_id FROM users WHERE email = 'jean-pierre.moreau@plus33coffee.fr';
    IF v_admin_id IS NULL THEN
        SELECT id INTO v_admin_id FROM users LIMIT 1;
    END IF;

    SELECT id INTO v_lt_annual FROM leave_types WHERE code = 'LT_ANNUAL';
    SELECT id INTO v_lt_sick FROM leave_types WHERE code = 'LT_SICK';

    -- ── 1. Payroll Periods (April, May, June 2026) ───────────
    INSERT INTO payroll_periods (company_id, period_name, start_date, end_date, status, processed_at, created_at, updated_at)
    VALUES (v_company_id, 'April 2026', '2026-04-01', '2026-04-30', 'PROCESSED', v_now - INTERVAL '60 days', v_now - INTERVAL '90 days', v_now)
    RETURNING id INTO v_p1_id;

    INSERT INTO payroll_periods (company_id, period_name, start_date, end_date, status, processed_at, created_at, updated_at)
    VALUES (v_company_id, 'May 2026', '2026-05-01', '2026-05-31', 'PROCESSED', v_now - INTERVAL '30 days', v_now - INTERVAL '60 days', v_now)
    RETURNING id INTO v_p2_id;

    INSERT INTO payroll_periods (company_id, period_name, start_date, end_date, status, processed_at, created_at, updated_at)
    VALUES (v_company_id, 'June 2026', '2026-06-01', '2026-06-30', 'OPEN', NULL, v_now - INTERVAL '30 days', v_now)
    RETURNING id INTO v_p3_id;

    -- ── 2. Attendance (~18,000 records) & Leaves (~200) & Payroll (~600) ──
    -- Loop over all employees of the company
    FOR rec IN SELECT e.id, e.employee_code, e.hire_date, es.shift_id, s.start_time, s.end_time 
               FROM employees e
               LEFT JOIN employee_shifts es ON e.id = es.employee_id
               LEFT JOIN shifts s ON es.shift_id = s.id
               WHERE e.company_id = v_company_id LOOP
        
        -- Seed 90 days of attendance (from April 1 to June 29, 2026)
        FOR d IN 0..89 LOOP
            v_date := DATE '2026-04-01' + d * INTERVAL '1 day';
            
            -- Skip weekends for office staff, but keep for store staff (employee code sequence tells us)
            -- Let's check sequence: if employee_code has seq > 96, they are store staff
            IF (EXTRACT(DOW FROM v_date) IN (0, 6) AND rec.employee_code < 'EMP-0097') THEN
                CONTINUE; -- Weekend off for corporate staff
            END IF;

            -- 5% chance of being absent or on leave
            IF (rec.id + d) % 20 = 0 THEN
                v_status := 'ABSENT';
                v_checkin := NULL;
                v_checkout := NULL;
                v_work_min := 0;
                v_ot_min := 0;
            ELSE
                v_status := 'PRESENT';
                -- Set check-in/out times based on shift start/end times
                v_checkin := (v_date + COALESCE(rec.start_time, '08:00:00'::TIME))::TIMESTAMP + ((rec.id % 15) - 7) * INTERVAL '1 minute';
                v_checkout := v_checkin + INTERVAL '8 hours' + ((rec.id % 20) - 10) * INTERVAL '1 minute';
                v_work_min := 480 + ((rec.id % 20) - 10);
                v_ot_min := CASE WHEN v_work_min > 480 THEN v_work_min - 480 ELSE 0 END;
            END IF;

            INSERT INTO attendance (
                employee_id, shift_id, attendance_date, check_in_time, check_out_time, status, work_minutes, overtime_minutes, notes, created_at, updated_at
            ) VALUES (
                rec.id,
                rec.shift_id,
                v_date,
                v_checkin,
                v_checkout,
                v_status,
                v_work_min,
                v_ot_min,
                CASE WHEN v_status = 'PRESENT' THEN 'Regular shift attendance' ELSE 'Absent without leave' END,
                v_now - INTERVAL '30 days',
                v_now
            );
        END LOOP;

        -- Seed 1 Leave request per employee
        INSERT INTO employee_leaves (
            employee_id, leave_type_id, start_date, end_date, total_days, reason, status, approved_by, approved_at, rejection_reason, created_at, updated_at
        ) VALUES (
            rec.id,
            CASE WHEN rec.id % 2 = 0 THEN v_lt_annual ELSE v_lt_sick END,
            DATE '2026-07-10' + (rec.id % 10) * INTERVAL '1 day',
            DATE '2026-07-10' + (rec.id % 10 + 2) * INTERVAL '1 day',
            3.00,
            'Planned vacation / recovery',
            'APPROVED',
            v_admin_id,
            v_now - INTERVAL '5 days',
            NULL,
            v_now - INTERVAL '7 days',
            v_now
        );

        -- Seed 3 Payroll lines per employee (one per period)
        INSERT INTO employee_payrolls (
            payroll_period_id, employee_id, base_salary, overtime_amount, allowances, deductions, gross_salary, net_salary, payment_status, generated_at
        ) VALUES (
            v_p1_id,
            rec.id,
            3000.00 + (rec.id % 20) * 100.00,
            100.00,
            200.00,
            50.00,
            3300.00 + (rec.id % 20) * 100.00,
            3250.00 + (rec.id % 20) * 100.00,
            'PAID',
            v_now - INTERVAL '60 days'
        );

        INSERT INTO employee_payrolls (
            payroll_period_id, employee_id, base_salary, overtime_amount, allowances, deductions, gross_salary, net_salary, payment_status, generated_at
        ) VALUES (
            v_p2_id,
            rec.id,
            3000.00 + (rec.id % 20) * 100.00,
            120.00,
            200.00,
            50.00,
            3320.00 + (rec.id % 20) * 100.00,
            3270.00 + (rec.id % 20) * 100.00,
            'PAID',
            v_now - INTERVAL '30 days'
        );

        INSERT INTO employee_payrolls (
            payroll_period_id, employee_id, base_salary, overtime_amount, allowances, deductions, gross_salary, net_salary, payment_status, generated_at
        ) VALUES (
            v_p3_id,
            rec.id,
            3000.00 + (rec.id % 20) * 100.00,
            0.00,
            200.00,
            50.00,
            3200.00 + (rec.id % 20) * 100.00,
            3150.00 + (rec.id % 20) * 100.00,
            'DRAFT',
            v_now
        );

        -- ── 3. Employee Documents (~500 documents) ───────────────
        -- Contract, Passport, ID Card
        INSERT INTO hcm_employee_documents (employee_id, document_type, document_number, expiry_date, notified)
        VALUES (rec.id, 'PASSPORT', 'PASS-FR-' || LPAD(rec.id::TEXT, 6, '0'), DATE '2030-01-01' + (rec.id % 100) * INTERVAL '1 day', FALSE);

        INSERT INTO hcm_employee_documents (employee_id, document_type, document_number, expiry_date, notified)
        VALUES (rec.id, 'NATIONAL_ID', 'ID-FR-' || LPAD(rec.id::TEXT, 6, '0'), DATE '2031-05-15' + (rec.id % 100) * INTERVAL '1 day', FALSE);

        INSERT INTO hcm_employee_documents (employee_id, document_type, document_number, expiry_date, notified)
        VALUES (rec.id, 'WORK_CONTRACT', 'CONT-FR-' || LPAD(rec.id::TEXT, 6, '0'), DATE '2032-12-31', FALSE);

    END LOOP;

    -- ── 4. Platform Audit Logs (3000 entries) ─────────────────
    FOR i IN 1..3000 LOOP
        INSERT INTO platform_audit_log (action_name, user_identity, trace_context, payload_diff, created_at)
        VALUES (
            CASE (i % 6)
                WHEN 0 THEN 'USER_LOGIN'
                WHEN 1 THEN 'SALES_ORDER_CREATE'
                WHEN 2 THEN 'INVENTORY_STOCK_UPDATE'
                WHEN 3 THEN 'PURCHASE_ORDER_APPROVE'
                WHEN 4 THEN 'PAYROLL_RUN_PROCESS'
                ELSE 'CRM_LEAD_CONVERT'
            END,
            'user.' || (1 + (i % 200)) || '@plus33coffee.fr',
            'TRACE-CTX-' || LPAD(i::TEXT, 8, '0'),
            '{"action_sequence": ' || i || ', "client_ip": "192.168.1.' || (1 + (i % 254)) || '"}',
            v_now - (i * INTERVAL '40 minutes')
        );
    END LOOP;

    -- ── 5. BI Analytics Snapshot (~100 snapshots) ────────────
    FOR i IN 1..100 LOOP
        INSERT INTO bi_analytics_snapshot (
            company_id, snapshot_date, snapshot_period, kpi_code, kpi_value, kpi_unit, dimension_filters, source_job_run_id, created_at
        ) VALUES (
            v_company_id,
            (v_now - (i * INTERVAL '1 day'))::DATE,
            'DAILY',
            CASE (i % 6)
                WHEN 0 THEN 'TOTAL_SALES_REV'
                WHEN 1 THEN 'OP_EXPENSE'
                WHEN 2 THEN 'ACTIVE_CUSTOMER_CNT'
                WHEN 3 THEN 'EMP_ATTENDANCE_RATE'
                WHEN 4 THEN 'INVENTORY_TURN_RATE'
                ELSE 'CARBON_FOOTPRINT_CO2E'
            END,
            CASE (i % 6)
                WHEN 0 THEN 25000.00 + (i * 120.00)
                WHEN 1 THEN 12000.00 + (i * 50.00)
                WHEN 2 THEN 1200.00 + (i * 2.00)
                WHEN 3 THEN 96.50 + (i % 4) * 0.50
                WHEN 4 THEN 4.20 + (i % 10) * 0.15
                ELSE 180.00 - (i * 0.80) -- Carbon emissions reducing over time due to optimization
            END,
            CASE (i % 6)
                WHEN 0 THEN 'EUR'
                WHEN 1 THEN 'EUR'
                WHEN 2 THEN 'COUNT'
                WHEN 3 THEN 'PERCENTAGE'
                WHEN 4 THEN 'RATIO'
                ELSE 'KG'
            END,
            '{"region": "all", "segment": "all"}',
            NULL,
            v_now - (i * INTERVAL '1 day')
        );
    END LOOP;

END $$;
