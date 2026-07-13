package com.plus33.erp;

import org.junit.jupiter.api.Test;
import java.sql.*;
import java.time.LocalDate;

public class SeedGrcTest {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/plus33_erp";
    private static final String DB_USER = "postgres";
    private static final String DB_PASS = "crazy@8";

    @Test
    public void testSeedGrc() throws Exception {
        Class.forName("org.postgresql.Driver");
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            conn.setAutoCommit(false);
            try {
                System.out.println("Seeding GRC database tables...");

                // 1. Get PLUS33_COFFEE company ID
                long companyId = 0;
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT id FROM companies WHERE code = 'PLUS33_COFFEE'")) {
                    if (rs.next()) {
                        companyId = rs.getLong("id");
                    }
                }
                if (companyId == 0) {
                    try (Statement stmt = conn.createStatement();
                         ResultSet rs = stmt.executeQuery("SELECT id FROM companies LIMIT 1")) {
                        if (rs.next()) {
                            companyId = rs.getLong("id");
                        }
                    }
                }
                System.out.println("Resolved Company ID: " + companyId);

                // 2. Get Jean-Pierre Moreau user ID or any user ID as owner/tester
                long userId = 0;
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT id FROM users WHERE email = 'jean-pierre.moreau@plus33coffee.fr'")) {
                    if (rs.next()) {
                        userId = rs.getLong("id");
                    }
                }
                if (userId == 0) {
                    try (Statement stmt = conn.createStatement();
                         ResultSet rs = stmt.executeQuery("SELECT id FROM users LIMIT 1")) {
                        if (rs.next()) {
                            userId = rs.getLong("id");
                        }
                    }
                }
                System.out.println("Resolved User/Owner ID: " + userId);

                // 3. Clear existing GRC tables to make seed clean and idempotent
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute("DELETE FROM grc_cap_tasks");
                    stmt.execute("DELETE FROM grc_corrective_action_plans");
                    stmt.execute("DELETE FROM grc_enterprise_issues");
                    stmt.execute("DELETE FROM grc_audit_finding_responses");
                    stmt.execute("DELETE FROM grc_audit_findings");
                    stmt.execute("DELETE FROM grc_audit_engagements");
                    stmt.execute("DELETE FROM grc_control_alerts");
                    stmt.execute("DELETE FROM grc_continuous_control_monitors");
                    stmt.execute("DELETE FROM grc_control_test_results");
                    stmt.execute("DELETE FROM grc_control_test_plans");
                    stmt.execute("DELETE FROM grc_control_mappings");
                    stmt.execute("DELETE FROM grc_control_library");
                }

                // 4. Seed grc_control_library
                String insertLibSql = "INSERT INTO grc_control_library (company_id, control_code, name, description, status, owner_id) VALUES (?, ?, ?, ?, 'ACTIVE', ?)";
                long libId1, libId2, libId3;
                try (PreparedStatement ps = conn.prepareStatement(insertLibSql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setLong(1, companyId);
                    ps.setString(2, "CTRL-SEC-01");
                    ps.setString(3, "Data Access Control");
                    ps.setString(4, "Restricts unauthorized access to sensitive financial databases.");
                    ps.setLong(5, userId);
                    ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) { rs.next(); libId1 = rs.getLong(1); }

                    ps.setString(2, "CTRL-FIN-02");
                    ps.setString(3, "Dual Authorization");
                    ps.setString(4, "Enforces purchase orders above 10,000 EUR to require manager approval.");
                    ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) { rs.next(); libId2 = rs.getLong(1); }

                    ps.setString(2, "CTRL-OPS-03");
                    ps.setString(3, "Food Safety Controls");
                    ps.setString(4, "Ensures daily temperature logs for milk and coffee bean storage are maintained.");
                    ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) { rs.next(); libId3 = rs.getLong(1); }
                }

                // 5. Seed grc_control_test_plans
                String insertPlanSql = "INSERT INTO grc_control_test_plans (control_library_id, test_name, frequency, next_test_date) VALUES (?, ?, 'MONTHLY', ?)";
                long planId1, planId2, planId3;
                try (PreparedStatement ps = conn.prepareStatement(insertPlanSql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setLong(1, libId1);
                    ps.setString(2, "Monthly database access log audit");
                    ps.setDate(3, Date.valueOf(LocalDate.now().plusMonths(1)));
                    ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) { rs.next(); planId1 = rs.getLong(1); }

                    ps.setLong(1, libId2);
                    ps.setString(2, "Dual auth audit sample testing");
                    ps.setDate(3, Date.valueOf(LocalDate.now().plusMonths(1)));
                    ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) { rs.next(); planId2 = rs.getLong(1); }

                    ps.setLong(1, libId3);
                    ps.setString(2, "Weekly store food safety records review");
                    ps.setDate(3, Date.valueOf(LocalDate.now().plusMonths(1)));
                    ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) { rs.next(); planId3 = rs.getLong(1); }
                }

                // 6. Seed grc_control_test_results (8 PASSED, 2 FAILED -> 80% compliance score)
                String insertResultSql = "INSERT INTO grc_control_test_results (test_plan_id, result, tested_by_id, tested_at, notes) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(insertResultSql)) {
                    // Passed results
                    for (int i = 0; i < 8; i++) {
                        ps.setLong(1, i % 2 == 0 ? planId1 : planId2);
                        ps.setString(2, "PASSED");
                        ps.setLong(3, userId);
                        ps.setTimestamp(4, Timestamp.valueOf(LocalDate.now().minusDays(i * 3 + 1).atStartOfDay()));
                        ps.setString(5, "Control check successfully verified and verified conformant.");
                        ps.addBatch();
                    }
                    // Failed results
                    for (int i = 0; i < 2; i++) {
                        ps.setLong(1, planId3);
                        ps.setString(2, "FAILED");
                        ps.setLong(3, userId);
                        ps.setTimestamp(4, Timestamp.valueOf(LocalDate.now().minusDays(i * 10 + 2).atStartOfDay()));
                        ps.setString(5, "Control check failed: some daily logs were missing or incomplete.");
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }

                // 7. Seed grc_audit_engagements (total audits completed count is fetched by: status IN ('COMPLETED', 'CLOSED'))
                // Table requires: program_id, audit_universe_id, engagement_number, title, status, lead_auditor_id, start_date, end_date
                // First insert a dummy program and audit universe if not exists
                long programId = 1;
                long universeId = 1;
                try (Statement stmt = conn.createStatement()) {
                    stmt.execute("INSERT INTO grc_audit_universe (company_id, entity_name, entity_type, risk_score) VALUES (" + companyId + ", 'Enterprise IT Operations', 'IT', 95.00) ON CONFLICT DO NOTHING");
                    stmt.execute("INSERT INTO grc_audit_programs (company_id, program_name, fiscal_year, status) VALUES (" + companyId + ", 'Annual Financial and Security Program', 2026, 'ACTIVE') ON CONFLICT DO NOTHING");
                    
                    try (ResultSet rs = stmt.executeQuery("SELECT id FROM grc_audit_universe LIMIT 1")) { if (rs.next()) universeId = rs.getLong(1); }
                    try (ResultSet rs = stmt.executeQuery("SELECT id FROM grc_audit_programs LIMIT 1")) { if (rs.next()) programId = rs.getLong(1); }
                }

                String insertAuditSql = "INSERT INTO grc_audit_engagements (program_id, audit_universe_id, engagement_number, title, status, lead_auditor_id, start_date, end_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(insertAuditSql)) {
                    // Completed audits
                    for (int i = 1; i <= 5; i++) {
                        ps.setLong(1, programId);
                        ps.setLong(2, universeId);
                        ps.setString(3, "AUD-2026-" + i);
                        ps.setString(4, "Q" + i + " Compliance Review " + i);
                        ps.setString(5, i <= 3 ? "COMPLETED" : "CLOSED");
                        ps.setLong(6, userId);
                        ps.setDate(7, Date.valueOf(LocalDate.now().minusMonths(6 - i)));
                        ps.setDate(8, Date.valueOf(LocalDate.now().minusMonths(6 - i).plusDays(10)));
                        ps.addBatch();
                    }
                    // Planned/Open audit
                    ps.setLong(1, programId);
                    ps.setLong(2, universeId);
                    ps.setString(3, "AUD-2026-6");
                    ps.setString(4, "Q4 Enterprise Security Audit");
                    ps.setString(5, "IN_PROGRESS");
                    ps.setLong(6, userId);
                    ps.setDate(7, Date.valueOf(LocalDate.now()));
                    ps.setDate(8, Date.valueOf(LocalDate.now().plusDays(15)));
                    ps.addBatch();

                    ps.executeBatch();
                }

                // 8. Seed grc_enterprise_issues and grc_corrective_action_plans
                // Corrective action plans requires: issue_id, description, owner_id, due_date, status ('OPEN', 'CLOSED')
                long issueId1, issueId2;
                String insertIssueSql = "INSERT INTO grc_enterprise_issues (company_id, issue_number, title, source, status, severity, owner_id, due_date) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(insertIssueSql, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setLong(1, companyId);
                    ps.setString(2, "ISSUE-01");
                    ps.setString(3, "Missing daily storage temperature logs");
                    ps.setString(4, "CONTROL_TEST");
                    ps.setString(5, "OPEN");
                    ps.setString(6, "MEDIUM");
                    ps.setLong(7, userId);
                    ps.setDate(8, Date.valueOf(LocalDate.now().minusDays(5))); // Overdue issue
                    ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) { rs.next(); issueId1 = rs.getLong(1); }

                    ps.setString(2, "ISSUE-02");
                    ps.setString(3, "Unauthorized access request detected");
                    ps.setString(4, "AUDIT");
                    ps.setString(5, "OPEN");
                    ps.setString(6, "HIGH");
                    ps.setLong(7, userId);
                    ps.setDate(8, Date.valueOf(LocalDate.now().plusDays(15))); // Future issue
                    ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) { rs.next(); issueId2 = rs.getLong(1); }
                }

                String insertCapSql = "INSERT INTO grc_corrective_action_plans (issue_id, description, owner_id, due_date, status) VALUES (?, ?, ?, ?, ?)";
                try (PreparedStatement ps = conn.prepareStatement(insertCapSql)) {
                    // Overdue CAP
                    ps.setLong(1, issueId1);
                    ps.setString(2, "Install automated temperature monitoring system and backfill records.");
                    ps.setLong(3, userId);
                    ps.setDate(4, Date.valueOf(LocalDate.now().minusDays(3))); // Overdue CAP
                    ps.setString(5, "OPEN");
                    ps.addBatch();

                    // Open CAP (due in future)
                    ps.setLong(1, issueId2);
                    ps.setString(2, "Implement multi-factor authentication for DB access.");
                    ps.setLong(3, userId);
                    ps.setDate(4, Date.valueOf(LocalDate.now().plusDays(20))); // Future CAP
                    ps.setString(5, "OPEN");
                    ps.addBatch();

                    // Closed CAP
                    ps.setLong(1, issueId1);
                    ps.setString(2, "Conduct staff training on GRC food logging rules.");
                    ps.setLong(3, userId);
                    ps.setDate(4, Date.valueOf(LocalDate.now().minusDays(10)));
                    ps.setString(5, "CLOSED");
                    ps.addBatch();

                    ps.executeBatch();
                }

                conn.commit();
                System.out.println("Successfully seeded GRC compliance data!");
            } catch (Exception e) {
                conn.rollback();
                System.err.println("Error seeding GRC database tables: " + e.getMessage());
                throw e;
            }
        }
    }

    @Test
    public void testListUsers() throws Exception {
        Class.forName("org.postgresql.Driver");
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            System.out.println("=== DB COMPANIES ===");
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT id, code, name FROM companies")) {
                while (rs.next()) {
                    System.out.println("COMPANY: " + rs.getLong("id") + " | " + rs.getString("code") + " | " + rs.getString("name"));
                }
            }
            System.out.println("=== DB SUPPLIERS ===");
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT id, name, company_id FROM suppliers")) {
                while (rs.next()) {
                    System.out.println("SUPPLIER: " + rs.getLong("id") + " | " + rs.getString("name") + " | Company: " + rs.getLong("company_id"));
                }
            }
            System.out.println("=== DB CUSTOMERS ===");
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT id, name, company_id FROM customers LIMIT 10")) {
                while (rs.next()) {
                    System.out.println("CUSTOMER: " + rs.getLong("id") + " | " + rs.getString("name") + " | Company: " + rs.getLong("company_id"));
                }
            }
        }
    }

    private String dbColumnExists(ResultSet rs, String columnName) {
        try {
            return String.valueOf(rs.getLong(columnName));
        } catch (Exception e) {
            return "N/A";
        }
    }

    @Test
    public void testSeed30DaysData() throws Exception {
        Class.forName("org.postgresql.Driver");
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            conn.setAutoCommit(false);
            try {
                System.out.println("Seeding 30 days of sales & financial overview data...");
                
                // 1. Resolve Company ID
                long companyId = 0;
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT id FROM companies WHERE code = 'PLUS33_COFFEE'")) {
                    if (rs.next()) companyId = rs.getLong("id");
                }
                if (companyId == 0) {
                    try (Statement stmt = conn.createStatement();
                         ResultSet rs = stmt.executeQuery("SELECT id FROM companies LIMIT 1")) {
                        if (rs.next()) companyId = rs.getLong("id");
                    }
                }
                
                // 2. Resolve Customer ID
                long customerId = 0;
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT id FROM customers WHERE company_id = " + companyId + " LIMIT 1")) {
                    if (rs.next()) customerId = rs.getLong("id");
                }
                if (customerId == 0) {
                    try (Statement stmt = conn.createStatement();
                         ResultSet rs = stmt.executeQuery("SELECT id FROM customers LIMIT 1")) {
                        if (rs.next()) customerId = rs.getLong("id");
                    }
                }
                
                // 3. Resolve Supplier ID
                long supplierId = 0;
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT id FROM suppliers WHERE company_id = " + companyId + " LIMIT 1")) {
                    if (rs.next()) supplierId = rs.getLong("id");
                }
                if (supplierId == 0) {
                    try (Statement stmt = conn.createStatement();
                         ResultSet rs = stmt.executeQuery("SELECT id FROM suppliers LIMIT 1")) {
                        if (rs.next()) supplierId = rs.getLong("id");
                    }
                }
                if (supplierId == 0) {
                    String insertSuppSql = "INSERT INTO suppliers (code, name, contact_person, email, phone, address, tax_number, active, company_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement ps = conn.prepareStatement(insertSuppSql, Statement.RETURN_GENERATED_KEYS)) {
                        ps.setString(1, "SUP-001");
                        ps.setString(2, "Default Supplier");
                        ps.setString(3, "John Doe");
                        ps.setString(4, "john@supplier.com");
                        ps.setString(5, "+1234567890");
                        ps.setString(6, "123 Supplier St");
                        ps.setString(7, "TAX-001");
                        ps.setBoolean(8, true);
                        ps.setLong(9, companyId);
                        ps.executeUpdate();
                        try (ResultSet rs = ps.getGeneratedKeys()) {
                            if (rs.next()) supplierId = rs.getLong(1);
                        }
                    }
                }
                
                // 4. Resolve User ID
                long userId = 0;
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT id FROM users LIMIT 1")) {
                    if (rs.next()) userId = rs.getLong("id");
                }
                
                // 5. Get all Store IDs
                java.util.List<Long> storeIds = new java.util.ArrayList<>();
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT id FROM stores")) {
                    while (rs.next()) {
                        storeIds.add(rs.getLong("id"));
                    }
                }
                if (storeIds.isEmpty()) {
                    storeIds.add(1L); // Fallback
                }
                
                System.out.println("Resolved Details - Company: " + companyId + ", Customer: " + customerId + ", Supplier: " + supplierId + ", User: " + userId + ", Stores count: " + storeIds.size());
                
                // Clear existing sales/invoice data for the last 40 days to make it clean
                try (PreparedStatement ps = conn.prepareStatement(
                        "DELETE FROM sales_orders WHERE order_date >= ?")) {
                    ps.setDate(1, java.sql.Date.valueOf(LocalDate.now().minusDays(40)));
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = conn.prepareStatement(
                        "DELETE FROM customer_invoices WHERE invoice_date >= ?")) {
                    ps.setDate(1, java.sql.Date.valueOf(LocalDate.now().minusDays(40)));
                    ps.executeUpdate();
                }
                try (PreparedStatement ps = conn.prepareStatement(
                        "DELETE FROM supplier_invoices WHERE invoice_date >= ?")) {
                    ps.setDate(1, java.sql.Date.valueOf(LocalDate.now().minusDays(40)));
                    ps.executeUpdate();
                }
                
                // Prepare statements
                String insertOrderSql = "INSERT INTO sales_orders (company_id, customer_id, order_number, client_reference_id, order_date, currency_code, billing_address, shipping_address, status, customer_name, customer_code, customer_type, pricing_tier, tax_profile, subtotal, total_amount, outstanding_amount, ordered_by, store_id) VALUES (?, ?, ?, ?, ?, 'EUR', '123 Coffee St', '123 Coffee St', 'FULFILLED', 'Test Customer', 'CUST001', 'RETAIL', 'STANDARD', 'VAT_STANDARD', ?, ?, ?, ?, ?)";
                String insertCustInvSql = "INSERT INTO customer_invoices (company_id, customer_id, invoice_number, client_reference_id, invoice_date, subtotal_amount, total_amount, status, currency_code, created_by) VALUES (?, ?, ?, ?, ?, ?, ?, 'PAID', 'EUR', ?)";
                String insertSuppInvSql = "INSERT INTO supplier_invoices (company_id, supplier_id, invoice_number, invoice_date, total_amount, status, currency_code) VALUES (?, ?, ?, ?, ?, 'PAID', 'EUR')";
                
                try (PreparedStatement psOrder = conn.prepareStatement(insertOrderSql);
                     PreparedStatement psCustInv = conn.prepareStatement(insertCustInvSql);
                     PreparedStatement psSuppInv = conn.prepareStatement(insertSuppInvSql)) {
                     
                    java.util.Random rand = new java.util.Random(42); // Seeded for reproducibility
                    LocalDate today = LocalDate.now();
                    
                    for (int i = 0; i < 30; i++) {
                        LocalDate date = today.minusDays(i);
                        java.sql.Date sqlDate = java.sql.Date.valueOf(date);
                        
                        // Seed multiple sales orders per day to different stores
                        for (int s = 0; s < storeIds.size(); s++) {
                            long storeId = storeIds.get(s);
                            double amount = 500 + rand.nextInt(2000);
                            java.math.BigDecimal amtVal = java.math.BigDecimal.valueOf(amount);
                            
                            psOrder.setLong(1, companyId);
                            psOrder.setLong(2, customerId);
                            psOrder.setString(3, "SO-" + date.toString().replace("-", "") + "-" + storeId);
                            psOrder.setObject(4, java.util.UUID.randomUUID());
                            psOrder.setDate(5, sqlDate);
                            psOrder.setBigDecimal(6, amtVal);
                            psOrder.setBigDecimal(7, amtVal);
                            psOrder.setBigDecimal(8, amtVal);
                            psOrder.setLong(9, userId);
                            psOrder.setLong(10, storeId);
                            psOrder.addBatch();
                        }
                        
                        // Daily customer invoice total
                        double custInvAmt = 1500 + rand.nextInt(5000);
                        java.math.BigDecimal custInvVal = java.math.BigDecimal.valueOf(custInvAmt);
                        psCustInv.setLong(1, companyId);
                        psCustInv.setLong(2, customerId);
                        psCustInv.setString(3, "INV-CUST-" + date.toString().replace("-", ""));
                        psCustInv.setObject(4, java.util.UUID.randomUUID());
                        psCustInv.setDate(5, sqlDate);
                        psCustInv.setBigDecimal(6, custInvVal);
                        psCustInv.setBigDecimal(7, custInvVal);
                        psCustInv.setLong(8, userId);
                        psCustInv.addBatch();
                        
                        // Daily supplier invoice total (expenses)
                        double suppInvAmt = 1000 + rand.nextInt(3000);
                        java.math.BigDecimal suppInvVal = java.math.BigDecimal.valueOf(suppInvAmt);
                        psSuppInv.setLong(1, companyId);
                        psSuppInv.setLong(2, supplierId);
                        psSuppInv.setString(3, "INV-SUPP-" + date.toString().replace("-", ""));
                        psSuppInv.setDate(4, sqlDate);
                        psSuppInv.setBigDecimal(5, suppInvVal);
                        psSuppInv.addBatch();
                    }
                    
                    psOrder.executeBatch();
                    psCustInv.executeBatch();
                    psSuppInv.executeBatch();
                }
                
                conn.commit();
                System.out.println("30 days of data seeded successfully!");
            } catch (Exception e) {
                conn.rollback();
                System.err.println("Transaction rolled back due to error: " + e.getMessage());
                throw e;
            }
        }
    }

    @Test
    public void testExportSeededData() throws Exception {
        Class.forName("org.postgresql.Driver");
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            System.out.println("Exporting seeded 30 days data to file...");
            java.io.File file = new java.io.File("d:/plus33/plus33-erp/seeded_30_days_data.txt");
            try (java.io.PrintWriter writer = new java.io.PrintWriter(new java.io.FileWriter(file))) {
                writer.println("==============================================================================");
                writer.println("PLUS33 COFFEE ERP -- SEEDED 30 DAYS DATA DUMP");
                writer.println("==============================================================================");
                writer.println();
                
                // 1. Sales Orders summary
                writer.println("------------------------------------------------------------------------------");
                writer.println("DAILY SALES ORDERS (LAST 30 DAYS)");
                writer.println("------------------------------------------------------------------------------");
                writer.printf("%-12s | %-6s | %-20s | %-12s\n", "Order Date", "Store", "Order Number", "Total Amount");
                writer.println("------------------------------------------------------------------------------");
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(
                         "SELECT order_date, store_id, order_number, total_amount " +
                         "FROM sales_orders " +
                         "WHERE order_date >= CURRENT_DATE - INTERVAL '30 days' " +
                         "ORDER BY order_date DESC, store_id ASC")) {
                    while (rs.next()) {
                        writer.printf("%-12s | %-6d | %-20s | %-12s\n",
                            rs.getDate("order_date"),
                            rs.getLong("store_id"),
                            rs.getString("order_number"),
                            rs.getBigDecimal("total_amount") + " EUR");
                    }
                }
                writer.println();
                
                // 2. Customer Invoices summary
                writer.println("------------------------------------------------------------------------------");
                writer.println("DAILY CUSTOMER INVOICES (LAST 30 DAYS)");
                writer.println("------------------------------------------------------------------------------");
                writer.printf("%-12s | %-25s | %-12s\n", "Invoice Date", "Invoice Number", "Total Amount");
                writer.println("------------------------------------------------------------------------------");
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(
                         "SELECT invoice_date, invoice_number, total_amount " +
                         "FROM customer_invoices " +
                         "WHERE invoice_date >= CURRENT_DATE - INTERVAL '30 days' " +
                         "ORDER BY invoice_date DESC")) {
                    while (rs.next()) {
                        writer.printf("%-12s | %-25s | %-12s\n",
                            rs.getDate("invoice_date"),
                            rs.getString("invoice_number"),
                            rs.getBigDecimal("total_amount") + " EUR");
                    }
                }
                writer.println();
                
                // 3. Supplier Invoices summary
                writer.println("------------------------------------------------------------------------------");
                writer.println("DAILY SUPPLIER INVOICES (LAST 30 DAYS)");
                writer.println("------------------------------------------------------------------------------");
                writer.printf("%-12s | %-25s | %-12s\n", "Invoice Date", "Invoice Number", "Total Amount");
                writer.println("------------------------------------------------------------------------------");
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(
                         "SELECT invoice_date, invoice_number, total_amount " +
                         "FROM supplier_invoices " +
                         "WHERE invoice_date >= CURRENT_DATE - INTERVAL '30 days' " +
                         "ORDER BY invoice_date DESC")) {
                    while (rs.next()) {
                        writer.printf("%-12s | %-25s | %-12s\n",
                            rs.getDate("invoice_date"),
                            rs.getString("invoice_number"),
                            rs.getBigDecimal("total_amount") + " EUR");
                    }
                }
                writer.println();
                writer.println("==============================================================================");
            }
            System.out.println("Data successfully written to seeded_30_days_data.txt!");
        }
    }
}
