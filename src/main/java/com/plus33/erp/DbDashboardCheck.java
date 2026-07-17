package com.plus33.erp;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbDashboardCheck {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/plus33_erp";
        String user = "postgres";
        String pass = "crazy@8";
        
        System.out.println("==================================================================");
        System.out.println("VERIFYING WMS INVENTORY DASHBOARD DATABASE METRICS");
        System.out.println("==================================================================");

        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            // 1. Total Inventory Value
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT COALESCE(SUM(quantity * COALESCE(unit_cost, 1.50)), 0) FROM location_stock")) {
                if (rs.next()) {
                    double val = rs.getDouble(1);
                    System.out.printf("Total Inventory Value: ₹ %.2f (%.2f Million)\n", val * 83, (val * 83) / 1000000.0);
                }
            }

            // 2. Low Stock Items
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM location_stock WHERE quantity <= 100 AND quantity > 0")) {
                if (rs.next()) {
                    System.out.println("Low Stock Items Count: " + rs.getLong(1));
                }
            }

            // 3. Purchase Orders
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM purchase_orders")) {
                if (rs.next()) {
                    System.out.println("Total Purchase Orders count: " + rs.getLong(1));
                }
            }

            // 4. Products, Categories, Warehouses, Stores, Suppliers, Employees
            long products = 0, categories = 0, warehouses = 0, stores = 0, suppliers = 0, employees = 0;
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM products WHERE active = true")) {
                if (rs.next()) products = rs.getLong(1);
            }
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM product_categories WHERE active = true")) {
                if (rs.next()) categories = rs.getLong(1);
            }
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM warehouses WHERE active = true")) {
                if (rs.next()) warehouses = rs.getLong(1);
            }
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM stores WHERE active = true")) {
                if (rs.next()) stores = rs.getLong(1);
            }
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM suppliers WHERE active = true")) {
                if (rs.next()) suppliers = rs.getLong(1);
            }
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM employees WHERE active = true")) {
                if (rs.next()) employees = rs.getLong(1);
            }
            System.out.println("--- Footer Metrics ---");
            System.out.printf("Products: %d | Categories: %d | Warehouses: %d | Stores: %d | Suppliers: %d | Employees: %d\n",
                    products, categories, warehouses, stores, suppliers, employees);

            // 5. Category Distribution
            System.out.println("--- Category Distribution ---");
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(
                         "SELECT pc.name AS category, COALESCE(SUM(ls.quantity), 0) AS total_qty " +
                         "FROM location_stock ls " +
                         "JOIN products p ON p.id = ls.product_id " +
                         "JOIN product_categories pc ON pc.id = p.category_id " +
                         "GROUP BY pc.name " +
                         "ORDER BY total_qty DESC")) {
                while (rs.next()) {
                    System.out.printf(" - %s: %.0f units\n", rs.getString("category"), rs.getDouble("total_qty"));
                }
            }

            // 6. Seeded Regional & National Warehouses
            System.out.println("--- Seeded National & Regional Warehouses (FR, AE, IN) ---");
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(
                          "SELECT w.code, w.name, r.name AS region_name FROM warehouses w " +
                          "JOIN regions r ON r.id = w.region_id " +
                          "WHERE w.code LIKE 'WH_%' ORDER BY w.code ASC")) {
                while (rs.next()) {
                    System.out.printf(" - %s: %s (%s)\n", rs.getString("code"), rs.getString("name"), rs.getString("region_name"));
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("==================================================================");
    }
}
