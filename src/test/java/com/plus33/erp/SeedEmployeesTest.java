package com.plus33.erp;

import org.junit.jupiter.api.Test;
import java.sql.*;
import java.time.LocalDate;

public class SeedEmployeesTest {
    private static final String DB_URL = "jdbc:postgresql://localhost:5432/plus33_erp";
    private static final String DB_USER = "postgres";
    private static final String DB_PASS = "crazy@8";

    @Test
    public void testSeedEmployees() throws Exception {
        Class.forName("org.postgresql.Driver");
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            conn.setAutoCommit(false);
            try {
                // Let's print existing companies
                System.out.println("=== COMPANIES ===");
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery("SELECT id, name, code FROM companies")) {
                    while (rs.next()) {
                        System.out.println("Company ID: " + rs.getLong("id") + ", Code: " + rs.getString("code") + ", Name: " + rs.getString("name"));
                    }
                }

                // Query all users that are store admins, shift supervisors, or senior employees in user_stores
                // but do not have an employee record yet.
                String query = 
                    "SELECT u.id as user_id, u.email, u.first_name, u.last_name, us.store_id, s.region_id, r.company_id, s.code as store_code, ro.code as role_code " +
                    "FROM users u " +
                    "JOIN user_stores us ON u.id = us.user_id " +
                    "JOIN stores s ON us.store_id = s.id " +
                    "JOIN regions r ON s.region_id = r.id " +
                    "JOIN user_roles ur ON u.id = ur.user_id " +
                    "JOIN roles ro ON ur.role_id = ro.id " +
                    "LEFT JOIN employees e ON u.id = e.user_id " +
                    "WHERE e.id IS NULL AND (u.email LIKE 'admin_st_%' OR u.email LIKE 'sup%_st_%' OR u.email LIKE 'emp%_st_%')";

                System.out.println("Fetching users to seed as employees...");
                int insertCount = 0;
                try (Statement stmt = conn.createStatement();
                     ResultSet rs = stmt.executeQuery(query)) {
                    
                    String insertSql = 
                        "INSERT INTO employees (employee_code, user_id, company_id, first_name, last_name, email, phone, designation, department, employment_type, hire_date, status, active, created_at, updated_at) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, 'ACTIVE', TRUE, NOW(), NOW())";
                    
                    try (PreparedStatement ps = conn.prepareStatement(insertSql)) {
                        while (rs.next()) {
                            long userId = rs.getLong("user_id");
                            String email = rs.getString("email");
                            String firstName = rs.getString("first_name");
                            String lastName = rs.getString("last_name");
                            long companyId = rs.getLong("company_id");
                            String storeCode = rs.getString("store_code");
                            String roleCode = rs.getString("role_code");
                            
                            // Generate unique employee code
                            String empCode = "EMP-" + storeCode + "-" + userId;
                            
                            // Determine designation
                            String designation;
                            if ("STORE_ADMIN".equals(roleCode)) {
                                designation = "Store Manager";
                            } else if ("SHIFT_SUPERVISOR".equals(roleCode)) {
                                designation = "Shift Supervisor";
                            } else {
                                designation = "Senior Barista";
                            }
                            
                            ps.setString(1, empCode);
                            ps.setLong(2, userId);
                            ps.setLong(3, companyId);
                            ps.setString(4, firstName);
                            ps.setString(5, lastName);
                            ps.setString(6, email);
                            ps.setString(7, "+1234567890");
                            ps.setString(8, designation);
                            ps.setString(9, "Operations");
                            ps.setString(10, "FULL_TIME");
                            ps.setDate(11, Date.valueOf(LocalDate.of(2026, 1, 1)));
                            
                            ps.addBatch();
                            insertCount++;
                        }
                        
                        if (insertCount > 0) {
                            ps.executeBatch();
                        }
                    }
                }
                
                conn.commit();
                System.out.println("Successfully seeded " + insertCount + " employee records in the employees table!");
                
            } catch (Exception e) {
                conn.rollback();
                System.err.println("Error seeding employees, rolled back: " + e.getMessage());
                throw e;
            }
        }
    }
}
