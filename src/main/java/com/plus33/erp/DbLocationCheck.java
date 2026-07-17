package com.plus33.erp;

import java.sql.*;

public class DbLocationCheck {
    public static void main(String[] args) {
        String url = "jdbc:postgresql://localhost:5432/plus33_erp";
        String user = "postgres";
        String pass = "crazy@8";
        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            System.out.println("--- WAREHOUSE LOCATIONS ---");
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery("SELECT id, warehouse_id, code, name, location_type FROM warehouse_locations")) {
                while (rs.next()) {
                    System.out.println(rs.getLong("id") + "\t" + rs.getLong("warehouse_id") + "\t" + rs.getString("code") + "\t" + rs.getString("name") + "\t" + rs.getString("location_type"));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
