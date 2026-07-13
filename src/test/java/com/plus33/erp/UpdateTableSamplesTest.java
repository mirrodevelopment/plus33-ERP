package com.plus33.erp;

import org.junit.jupiter.api.Test;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

/**
 * Regenerates table_samples.txt from scratch using the live database,
 * and provides a utility to empty seeded mock data.
 */
public class UpdateTableSamplesTest {

    private static final String DB_URL  = "jdbc:postgresql://localhost:5432/plus33_erp";
    private static final String DB_USER = "postgres";
    private static final String DB_PASS = "crazy@8";
    private static final String TABLE_SAMPLES_PATH = "d:\\plus33\\plus33-erp\\table_samples.txt";

    @Test
    public void emptyMockData() throws Exception {
        Class.forName("org.postgresql.Driver");
        java.nio.file.Path countsPath = java.nio.file.Path.of("d:\\plus33\\plus33-erp\\database_counts.txt");
        if (!java.nio.file.Files.exists(countsPath)) {
            System.out.println("Error: database_counts.txt not found!");
            return;
        }
        
        List<String> lines = java.nio.file.Files.readAllLines(countsPath);
        List<String> tablesToEmpty = new ArrayList<>();
        for (String line : lines) {
            line = line.trim();
            if (line.startsWith("Table: ")) {
                int pipeIdx = line.indexOf('|');
                if (pipeIdx > 0) {
                    String tablePart = line.substring(0, pipeIdx).trim();
                    String countPart = line.substring(pipeIdx + 1).trim();
                    String tableName = tablePart.substring(7).trim();
                    if (countPart.startsWith("Count: ")) {
                        int cnt = Integer.parseInt(countPart.substring(7).trim());
                        if (cnt == 0) {
                            tablesToEmpty.add(tableName);
                        }
                    }
                }
            }
        }
        
        System.out.println("Found " + tablesToEmpty.size() + " tables to empty.");
        
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            try (Statement st = conn.createStatement()) {
                st.execute("SET session_replication_role = replica;");
            }
            
            int truncated = 0;
            for (String table : tablesToEmpty) {
                try (Statement st = conn.createStatement()) {
                    st.execute("DELETE FROM \"" + table + "\";");
                    truncated++;
                } catch (Exception e) {
                    System.out.println("Error deleting from " + table + ": " + e.getMessage());
                }
            }
            
            try (Statement st = conn.createStatement()) {
                st.execute("SET session_replication_role = DEFAULT;");
            }
            System.out.println("Successfully emptied " + truncated + " tables.");
        }
        
        // Regenerate table_samples.txt
        regenerateAll();
    }

    @Test
    public void regenerateAll() throws Exception {
        Class.forName("org.postgresql.Driver");
        java.nio.file.Path fp = java.nio.file.Path.of(TABLE_SAMPLES_PATH);
        String nl = "\n"; // use standard newline for output

        Map<String, String> purposes = parsePurposes();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            // Get all tables in public schema
            List<String> allTables = new ArrayList<>();
            try (Statement st = conn.createStatement();
                 ResultSet rs = st.executeQuery("""
                     SELECT table_name
                     FROM information_schema.tables
                     WHERE table_schema = 'public'
                     ORDER BY table_name
                     """)) {
                while (rs.next()) {
                    allTables.add(rs.getString(1));
                }
            }

            // Get row counts
            Map<String, Integer> counts = new HashMap<>();
            int totalLiveRecords = 0;
            for (String table : allTables) {
                try (Statement st = conn.createStatement();
                     ResultSet rs = st.executeQuery("SELECT count(*) FROM \"" + table + "\"")) {
                    if (rs.next()) {
                        int cnt = rs.getInt(1);
                        counts.put(table, cnt);
                        totalLiveRecords += cnt;
                    }
                } catch (Exception e) {
                    counts.put(table, 0);
                }
            }

            // Build the file header
            StringBuilder sb = new StringBuilder();
            sb.append("================================================================================").append(nl);
            sb.append("  PLUS33 Coffee ERP - Visual Table Schemas & Live Sample Datasets").append(nl);
            sb.append("================================================================================").append(nl);
            sb.append("Total Tables in Database: ").append(allTables.size()).append(nl);
            sb.append("Total Live Records: ").append(totalLiveRecords).append(nl);
            sb.append("================================================================================").append(nl).append(nl);

            // Summary Table
            sb.append("Database Table Summary, Counts, and Purposes:").append(nl);
            sb.append("+------------------------------------------+-------------+--------------------------------------------------------------------------------+").append(nl);
            sb.append("| Table Name                               | Row Count   | Table Purpose                                                                  |").append(nl);
            sb.append("+------------------------------------------+-------------+--------------------------------------------------------------------------------+").append(nl);

            for (String table : allTables) {
                int cnt = counts.get(table);
                String purpose = purposes.getOrDefault(table, "Holds standard module data records.");
                if (purpose.length() > 76) {
                    purpose = purpose.substring(0, 73) + "...";
                }
                sb.append(String.format("| %-40s | %-11d | %-78s |", table, cnt, purpose)).append(nl);
            }
            sb.append("+------------------------------------------+-------------+--------------------------------------------------------------------------------+").append(nl).append(nl);

            // Table blocks
            for (String table : allTables) {
                // Fetch columns
                List<String[]> colDefs = new ArrayList<>();
                try (PreparedStatement ps = conn.prepareStatement("""
                        SELECT column_name, udt_name
                        FROM information_schema.columns
                        WHERE table_schema='public' AND table_name=?
                        ORDER BY ordinal_position""")) {
                    ps.setString(1, table);
                    try (ResultSet rs = ps.executeQuery()) {
                        while (rs.next()) {
                            colDefs.add(new String[]{rs.getString(1), rs.getString(2)});
                        }
                    }
                }

                // Fetch up to 5 rows
                List<List<String>> rows = new ArrayList<>();
                try (Statement st = conn.createStatement();
                     ResultSet rs = st.executeQuery("SELECT * FROM \"" + table + "\" LIMIT 5")) {
                    ResultSetMetaData meta = rs.getMetaData();
                    int colCount = meta.getColumnCount();
                    while (rs.next()) {
                        List<String> row = new ArrayList<>();
                        for (int c = 1; c <= colCount; c++) {
                            Object v = rs.getObject(c);
                            row.add(fmtVal(v));
                        }
                        rows.add(row);
                    }
                }

                // Format ASCII Table
                sb.append(nl);
                if (rows.isEmpty()) {
                    sb.append(formatEmptyTable(table, colDefs, nl));
                } else {
                    sb.append(formatAsciiTable(table, colDefs, rows, nl));
                }
                sb.append(nl);
            }

            java.nio.file.Files.writeString(fp, sb.toString());
            System.out.println("Regeneration complete! File written. Total Tables: " + allTables.size() + ", Live records: " + totalLiveRecords);
        }
    }

    private String formatEmptyTable(String tableName, List<String[]> colDefs, String nl) {
        int colCount = colDefs.size();
        int[] widths = new int[colCount];
        
        String[] headers = new String[colCount];
        for (int i = 0; i < colCount; i++) {
            headers[i] = colDefs.get(i)[0] + " [" + colDefs.get(i)[1] + "]";
            widths[i] = headers[i].length() + 2;
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("Table: ").append(tableName).append(nl);
        
        StringBuilder sep = new StringBuilder("+");
        for (int w : widths) {
            for (int k = 0; k < w; k++) sep.append("-");
            sep.append("+");
        }
        sep.append(nl);
        
        sb.append(sep);
        
        sb.append("|");
        for (int i = 0; i < colCount; i++) {
            sb.append(String.format(" %-" + (widths[i] - 1) + "s|", headers[i]));
        }
        sb.append(nl);
        
        sb.append(sep);
        
        int totalWidth = sep.length() - 3;
        String msg = "No active records found.";
        if (msg.length() > totalWidth) {
            sb.append("| ").append(msg.substring(0, totalWidth)).append(" |").append(nl);
        } else {
            int spaces = totalWidth - msg.length();
            int left = spaces / 2;
            int right = spaces - left;
            sb.append("|").append(" ".repeat(left)).append(msg).append(" ".repeat(right)).append("|").append(nl);
        }
        
        sb.append(sep);
        return sb.toString();
    }

    private String formatAsciiTable(String tableName, List<String[]> colDefs, List<List<String>> rows, String nl) {
        int colCount = colDefs.size();
        int[] widths = new int[colCount];
        
        String[] headers = new String[colCount];
        for (int i = 0; i < colCount; i++) {
            headers[i] = colDefs.get(i)[0] + " [" + colDefs.get(i)[1] + "]";
            widths[i] = headers[i].length();
        }
        
        for (List<String> row : rows) {
            for (int i = 0; i < colCount; i++) {
                if (i < row.size()) {
                    widths[i] = Math.max(widths[i], row.get(i).length());
                }
            }
        }
        
        for (int i = 0; i < colCount; i++) {
            widths[i] += 2;
        }
        
        StringBuilder sb = new StringBuilder();
        sb.append("Table: ").append(tableName).append(nl);
        
        StringBuilder sep = new StringBuilder("+");
        for (int w : widths) {
            for (int k = 0; k < w; k++) sep.append("-");
            sep.append("+");
        }
        sep.append(nl);
        
        sb.append(sep);
        
        sb.append("|");
        for (int i = 0; i < colCount; i++) {
            sb.append(String.format(" %-" + (widths[i] - 1) + "s|", headers[i]));
        }
        sb.append(nl);
        
        sb.append(sep);
        
        for (List<String> row : rows) {
            sb.append("|");
            for (int i = 0; i < colCount; i++) {
                String val = (i < row.size()) ? row.get(i) : "NULL";
                sb.append(String.format(" %-" + (widths[i] - 1) + "s|", val));
            }
            sb.append(nl);
        }
        
        sb.append(sep);
        return sb.toString();
    }

    private Map<String, String> parsePurposes() throws Exception {
        Map<String, String> map = new HashMap<>();
        java.nio.file.Path path = java.nio.file.Path.of("d:\\plus33\\plus33-erp\\table purpose.txt");
        if (!java.nio.file.Files.exists(path)) {
            return map;
        }
        List<String> lines = java.nio.file.Files.readAllLines(path);
        String currentTable = null;
        StringBuilder purpose = new StringBuilder();
        for (String line : lines) {
            line = line.trim();
            if (line.startsWith("Table: ")) {
                if (currentTable != null && purpose.length() > 0) {
                    map.put(currentTable, purpose.toString().trim());
                }
                currentTable = line.substring(7).trim();
                purpose = new StringBuilder();
            } else if (currentTable != null && line.startsWith("* Purpose:")) {
                purpose.append(line.substring(10).trim());
            } else if (currentTable != null && line.startsWith("*") && purpose.length() > 0) {
                map.put(currentTable, purpose.toString().trim());
                currentTable = null;
            } else if (currentTable != null && purpose.length() > 0) {
                purpose.append(" ").append(line);
            }
        }
        if (currentTable != null && purpose.length() > 0) {
            map.put(currentTable, purpose.toString().trim());
        }
        return map;
    }

    private String fmtVal(Object v) {
        if (v == null) return "NULL";
        if (v instanceof Boolean) return v.toString();
        String s = v.toString();
        if (s.matches("\\d{4}-\\d{2}-\\d{2} \\d{2}.*") || s.matches("\\d{4}-\\d{2}-\\d{2}T\\d{2}.*")) {
            s = s.replace('T', ' ');
            s = s.length() > 19 ? s.substring(0, 19) : s;
        }
        return s.length() > 30 ? s.substring(0, 28) + ".." : s;
    }
}
