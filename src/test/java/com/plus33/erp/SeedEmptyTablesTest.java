package com.plus33.erp;

import org.junit.jupiter.api.Test;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Seeds 5 sample rows into every remaining empty public table.
 * - FK columns resolved via live DB queries
 * - varchar length respected via character_maximum_length
 * - numeric precision/scale respected
 * - time columns use java.sql.Time
 * - File update uses safe fmtVal that never overflows
 */
public class SeedEmptyTablesTest {

    private static final String DB_URL  = "jdbc:postgresql://localhost:5432/plus33_erp";
    private static final String DB_USER = "postgres";
    private static final String DB_PASS = "crazy@8";
    private static final String TABLE_SAMPLES_PATH = "d:\\plus33\\plus33-erp\\table_samples.txt";

    private final Map<String, List<Long>> fkCache = new HashMap<>();

    record ColumnMeta(
        String name, String dataType, String udt, String nullable, String defaultVal,
        Integer maxLen, Integer numPrecision, Integer numScale) {}

    record FkMeta(String colName, String refTable, String refColumn) {}

    // ── Entry point ────────────────────────────────────────────────────────
    @Test
    public void seedEmptyTables() throws Exception {
        Class.forName("org.postgresql.Driver");
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS)) {
            conn.setAutoCommit(false);

            List<String> emptyTables = getEmptyTables(conn);
            System.out.println("Empty tables to seed: " + emptyTables.size());

            Map<String, List<ColumnMeta>> colCache  = new LinkedHashMap<>();
            Map<String, List<Map<String, Object>>> results = new LinkedHashMap<>();
            int seeded = 0;

            for (String table : emptyTables) {
                try {
                    List<ColumnMeta> cols = getColumns(conn, table);
                    List<FkMeta>     fks  = getFKs(conn, table);
                    colCache.put(table, cols);

                    List<Map<String, Object>> rows = seedTable(conn, table, cols, fks, 5);
                    if (!rows.isEmpty()) {
                        results.put(table, rows);
                        seeded++;
                        System.out.printf("[%d] Seeded %-50s  %d rows%n", seeded, table, rows.size());
                    } else {
                        System.out.println("[SKIP] " + table);
                    }
                } catch (Exception e) {
                    try { conn.rollback(); } catch (Exception ignored) {}
                    System.out.println("[ERR] " + table + ": " + e.getMessage());
                }
            }

            System.out.println("\nTotal tables seeded: " + seeded);

            // The UpdateTableSamplesTest handles file patching more reliably.
            // Here we just log what was seeded.
            System.out.println("Done! Run UpdateTableSamplesTest to refresh table_samples.txt.");
        }
    }

    // ── DB helpers ─────────────────────────────────────────────────────────
    private List<String> getEmptyTables(Connection c) throws SQLException {
        List<String> list = new ArrayList<>();
        try (Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("""
                SELECT t.table_name
                FROM information_schema.tables t
                JOIN pg_stat_user_tables s ON s.relname = t.table_name
                WHERE t.table_schema='public' AND s.n_live_tup=0
                ORDER BY t.table_name""")) {
            while (rs.next()) list.add(rs.getString(1));
        }
        return list;
    }

    private List<ColumnMeta> getColumns(Connection c, String table) throws SQLException {
        List<ColumnMeta> list = new ArrayList<>();
        try (PreparedStatement ps = c.prepareStatement("""
                SELECT column_name, data_type, udt_name, is_nullable, column_default,
                       character_maximum_length, numeric_precision, numeric_scale
                FROM information_schema.columns
                WHERE table_schema='public' AND table_name=?
                ORDER BY ordinal_position""")) {
            ps.setString(1, table);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Integer maxLen = rs.getObject(6) != null ? rs.getInt(6) : null;
                    Integer prec   = rs.getObject(7) != null ? rs.getInt(7) : null;
                    Integer scale  = rs.getObject(8) != null ? rs.getInt(8) : null;
                    list.add(new ColumnMeta(rs.getString(1), rs.getString(2),
                             rs.getString(3), rs.getString(4), rs.getString(5),
                             maxLen, prec, scale));
                }
            }
        }
        return list;
    }

    private List<FkMeta> getFKs(Connection c, String table) throws SQLException {
        List<FkMeta> list = new ArrayList<>();
        try (PreparedStatement ps = c.prepareStatement("""
                SELECT kcu.column_name, ccu.table_name, ccu.column_name
                FROM information_schema.table_constraints tc
                JOIN information_schema.key_column_usage kcu
                  ON tc.constraint_name=kcu.constraint_name AND tc.table_schema=kcu.table_schema
                JOIN information_schema.constraint_column_usage ccu
                  ON ccu.constraint_name=tc.constraint_name AND ccu.table_schema=tc.table_schema
                WHERE tc.constraint_type='FOREIGN KEY'
                  AND tc.table_schema='public' AND tc.table_name=?""")) {
            ps.setString(1, table);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next())
                    list.add(new FkMeta(rs.getString(1), rs.getString(2), rs.getString(3)));
            }
        }
        return list;
    }

    private List<Long> resolveRefIds(Connection c, String refTable, String refCol) throws SQLException {
        String key = refTable + "." + refCol;
        if (fkCache.containsKey(key)) return fkCache.get(key);
        List<Long> ids = new ArrayList<>();
        try (Statement st = c.createStatement();
             ResultSet rs = st.executeQuery("SELECT \"" + refCol + "\" FROM \"" + refTable + "\" LIMIT 10")) {
            while (rs.next()) ids.add(rs.getLong(1));
        } catch (Exception ignored) {}
        fkCache.put(key, ids);
        return ids;
    }

    // ── Seed one table ─────────────────────────────────────────────────────
    private List<Map<String, Object>> seedTable(Connection conn, String table,
                                                 List<ColumnMeta> cols, List<FkMeta> fks, int n)
            throws SQLException {
        Map<String, FkMeta> fkMap = fks.stream()
                .collect(Collectors.toMap(FkMeta::colName, f -> f, (a, b) -> a));

        Map<String, List<Long>> refIds = new LinkedHashMap<>();
        for (FkMeta fk : fks)
            refIds.put(fk.colName(), resolveRefIds(conn, fk.refTable(), fk.refColumn()));

        List<ColumnMeta> insertable = cols.stream()
                .filter(c -> c.defaultVal() == null || !c.defaultVal().contains("nextval"))
                .collect(Collectors.toList());
        if (insertable.isEmpty()) return Collections.emptyList();

        // Skip if any NOT NULL FK has no live data in referenced table
        for (ColumnMeta col : insertable) {
            if (fkMap.containsKey(col.name()) && "NO".equals(col.nullable())) {
                if (refIds.getOrDefault(col.name(), Collections.emptyList()).isEmpty()) {
                    System.out.println("  [SKIP-FK] " + table + "." + col.name()
                            + " -> " + fkMap.get(col.name()).refTable() + " (empty ref)");
                    return Collections.emptyList();
                }
            }
        }

        String colList = insertable.stream()
                .map(c -> "\"" + c.name() + "\"").collect(Collectors.joining(", "));
        String phList  = insertable.stream().map(c -> "?").collect(Collectors.joining(", "));
        String sql = "INSERT INTO \"" + table + "\" (" + colList + ") VALUES (" + phList + ") ON CONFLICT DO NOTHING";

        List<Map<String, Object>> inserted = new ArrayList<>();
        for (int i = 1; i <= n; i++) {
            Map<String, Object> rowMap = new LinkedHashMap<>();
            try (PreparedStatement ps = conn.prepareStatement(sql)) {
                for (int j = 0; j < insertable.size(); j++) {
                    ColumnMeta col = insertable.get(j);
                    Object val;
                    if (fkMap.containsKey(col.name())) {
                        List<Long> ids = refIds.getOrDefault(col.name(), Collections.emptyList());
                        val = ids.isEmpty() ? null : ids.get((i - 1) % ids.size());
                    } else {
                        val = generateValue(col, i);
                    }
                    rowMap.put(col.name(), val);
                    setParam(ps, j + 1, val, col.dataType(), col.udt());
                }
                ps.executeUpdate();
                conn.commit();
                inserted.add(rowMap);
            } catch (Exception e) {
                try { conn.rollback(); } catch (Exception ignored) {}
                System.out.println("  [ROW-ERR] " + table + " row " + i + ": " + e.getMessage());
            }
        }
        return inserted;
    }

    // ── Value generator ─────────────────────────────────────────────────────
    private Object generateValue(ColumnMeta col, int i) {
        String c  = col.name().toLowerCase();
        String dt = col.dataType().toLowerCase();
        boolean nullable = "YES".equals(col.nullable());

        if (dt.contains("bool")) return (i % 2 == 0);

        if (dt.contains("timestamp")) {
            LocalDateTime base = LocalDateTime.of(2026, 1, 1, 8, 0).plusDays(i * 15L);
            if (c.contains("updated") || c.contains("modified")) return base.plusDays(5);
            if (c.contains("expire"))                             return base.plusDays(365);
            if (c.contains("end") || c.contains("closed"))       return base.plusDays(30);
            return base;
        }

        if (dt.equals("date")) {
            LocalDate base = LocalDate.of(2026, 1, 1).plusDays(i * 10L);
            if (c.contains("expire")) return base.plusDays(365);
            if (c.contains("end") || c.contains("to"))   return base.plusDays(60);
            if (c.contains("start") || c.contains("from")) return base;
            return base;
        }

        // TIME: return proper java.sql.Time object
        if (dt.startsWith("time") && !dt.contains("stamp"))
            return LocalTime.of(8 + (i % 14), (i * 15) % 60, 0);

        if (dt.contains("numeric") || dt.contains("decimal")) {
            // Respect numeric precision/scale to avoid overflow
            int prec  = col.numPrecision() != null ? col.numPrecision() : 18;
            int scale = col.numScale()     != null ? col.numScale()     : 2;
            double maxVal = Math.pow(10, prec - scale) - Math.pow(10, -scale);

            if (anyOf(c,"rate","percent","margin","ratio","score","efficiency","factor")) {
                double v = Math.min(10.5 + i * 4.5, maxVal);
                return round(v, scale);
            }
            if (anyOf(c,"qty","quantity","units","pieces")) return round(Math.min(i * 25.0, maxVal), scale);
            if (anyOf(c,"weight","volume","capacity"))      return round(Math.min(i * 50.5, maxVal), scale);
            if (anyOf(c,"lat","latitude"))  return round(Math.min(48.85 + i * 0.01, maxVal), scale);
            if (anyOf(c,"lon","longitude")) return round(Math.min(2.35  + i * 0.01, maxVal), scale);
            return round(Math.min(1000.0 + i * 750.50, maxVal), scale);
        }

        if (anyOf(dt,"bigint","integer","smallint","int","serial")) {
            if (anyOf(c,"minutes","hours","days","seconds","duration","ms")) return (long)(i * 60);
            if (anyOf(c,"count","qty","quantity","units","line_number","sequence","rank","sort")) return (long)i;
            if (anyOf(c,"year"))  return 2026L;
            if (anyOf(c,"month")) return (long)(i % 12 + 1);
            if (anyOf(c,"day"))   return (long)(i % 28 + 1);
            if (anyOf(c,"port"))  return (long)(8080 + i);
            if (c.endsWith("_id") && nullable) return null;
            return (long)(i * 100);
        }

        if (anyOf(dt,"character varying","varchar","text","char","character","name","citext")
            || col.udt().contains("varchar") || col.udt().contains("text")) {
            String val = varcharFor(c, i);
            // Respect character_maximum_length
            if (col.maxLen() != null && val.length() > col.maxLen())
                val = val.substring(0, col.maxLen());
            return val;
        }

        if (dt.contains("uuid"))     return UUID.randomUUID().toString();
        if (dt.contains("json"))     return "{\"source\":\"seed\",\"row\":" + i + "}";
        if (dt.equals("ARRAY") || col.udt().endsWith("[]")) return "{}";
        if (anyOf(dt,"inet","cidr")) return "192.168." + (i + 10) + ".1";
        if (dt.contains("interval")) return i + " days";

        return nullable ? null : "val_" + i;
    }

    private String varcharFor(String c, int i) {
        // Use very short values for columns that likely have tight varchar limits
        if (anyOf(c,"currency_code","currency")) return pick(i,"EUR","USD","GBP","INR","AED");
        if (anyOf(c,"email"))        return "ops" + i + "@plus33.com";
        if (anyOf(c,"phone","mobile","tel")) return "+336000000" + i;
        if (anyOf(c,"iban"))         return "FR76" + String.format("%016d", (long)i * 123456L);
        if (anyOf(c,"swift","bic"))  return pick(i,"BNPAFR","SOGEFR","CRLYFR","CEPAFR","BARCGB");
        if (anyOf(c,"country"))      return pick(i,"FR","DE","GB","IN","AE");
        if (anyOf(c,"status"))       return pick(i,"ACTIVE","PENDING","DONE","APPROVED","CLOSED");
        if (anyOf(c,"type","entry_type","entity_type")) return pick(i,"DEBIT","CREDIT","TRANSFER","ADJUSTMENT","REVERSAL");
        if (anyOf(c,"priority"))     return pick(i,"LOW","MED","HIGH","CRIT","NORM");
        if (anyOf(c,"category","class","grade")) return pick(i,"A","B","C","D","E");
        if (anyOf(c,"action","operation")) return pick(i,"CREATE","UPDATE","APPROVE","REJECT","DONE");
        if (anyOf(c,"method","mode","source","origin")) return pick(i,"MANUAL","AUTO","API","IMPORT","SYS");
        if (anyOf(c,"frequency"))    return pick(i,"DAILY","WEEKLY","MONTHLY","QRTLY","ANNUAL");
        if (anyOf(c,"format"))       return pick(i,"PDF","XLS","CSV","JSON","XML");
        if (anyOf(c,"version"))      return "v" + i + ".0";
        if (anyOf(c,"language","locale")) return pick(i,"en","fr","de","ar","hi");
        if (anyOf(c,"timezone"))     return pick(i,"UTC","CET","IST","GST","EST");
        if (anyOf(c,"unit"))         return pick(i,"KG","LTR","PCS","BOX","BAG");
        if (anyOf(c,"level"))        return pick(i,"L1","L2","L3","SR","JR");
        if (anyOf(c,"scope"))        return pick(i,"GLOBAL","LOCAL","NATION","REGION","GLOBAL");
        if (anyOf(c,"direction"))    return pick(i,"IN","OUT","XFER","ADJ","RET");
        if (anyOf(c,"channel"))      return pick(i,"POS","WEB","B2B","FRAN","MOB");
        if (anyOf(c,"cron","schedule")) return pick(i,"0 9 * * *","0 0 * * 1","0 6 1 * *","*/30 * * *","0 18 * * 5");
        if (anyOf(c,"dimension"))    return pick(i,"nation","region","store","product","employee");
        if (anyOf(c,"operator"))     return pick(i,"=","!=","IN","LIKE",">=");
        if (anyOf(c,"number","reference","ref","asn_number","tracking_number","po_reference",
                    "po_number","order_number","invoice_number","receipt_number",
                    "transfer_number","adjustment_number","write_off_number",
                    "statement_number","batch_number","serial_number","lot_number",
                    "file_number","plan_number"))
            return "R-" + String.format("%05d", i * 1000 + i);
        if (anyOf(c,"code"))         return String.format("C%04d", i * 100);
        if (anyOf(c,"name","full_name","display_name","title","subject"))
            return pick(i,"Arabica","Robusta","Espresso","HouseBlend","Origin");
        if (anyOf(c,"description","notes","content","details","summary","body","message","remarks","finding"))
            return "Seed record #" + i + " — Plus33 ERP.";
        if (anyOf(c,"reason","comment","justification")) return "Routine entry #" + i;
        if (anyOf(c,"address"))      return i + " Rue Cafe, Paris";
        if (anyOf(c,"ip"))           return "192.168.1." + i;
        if (anyOf(c,"hash","checksum","token","key","secret"))
            return UUID.randomUUID().toString().replace("-","").substring(0,16);
        if (anyOf(c,"period"))       return "2026-" + String.format("%02d", (i % 12) + 1);
        if (anyOf(c,"department","dept")) return pick(i,"Finance","Ops","HR","IT","SCM");
        if (anyOf(c,"role"))         return pick(i,"MGR","BARISTA","SUP","ACCT","AUDIT");
        if (anyOf(c,"url","path","uri")) return "/doc/" + i + ".pdf";
        if (anyOf(c,"tag","label","keyword")) return pick(i,"coffee","premium","bulk","seasonal","organic");
        if (anyOf(c,"zone","area"))  return pick(i,"Z-A","Z-B","Z-C","North","South");
        if (anyOf(c,"branch"))       return "BR" + String.format("%02d", i);
        if (anyOf(c,"product"))      return pick(i,"Arabica","Espresso","Robusta","Blend","Dark");
        if (anyOf(c,"model"))        return pick(i,"XGBoost","ARIMA","Prophet","LinReg","RandFrst");
        if (anyOf(c,"publisher","author","owner","manager"))
            return pick(i,"Rajesh","Marie","Ahmed","Priya","Jean");
        if (anyOf(c,"filter_values","banking_products","tags","keywords","values_json"))
            return "[\"item_" + i + "\"]";
        if (anyOf(c,"sla_details","contract_details","requirements"))
            return "SLA-" + i + ": standard terms";
        if (anyOf(c,"condition"))    return pick(i,"NEW","GOOD","FAIR","POOR","DEGD");
        if (anyOf(c,"severity","risk_level")) return pick(i,"LOW","MED","HIGH","CRIT","EXT");
        if (anyOf(c,"signing","authority")) return pick(i,"SOLE","JOINT","DUAL","TRIPLE","ADV");
        if (anyOf(c,"charge_type","fee_type")) return pick(i,"MAINT","TRANS","SVC","PENL","INT");
        if (anyOf(c,"depreciation_method")) return pick(i,"SL","DB","SYD","UOP","NONE");
        if (anyOf(c,"change_type")) return pick(i,"INS","UPD","DEL","MRG","EXP");
        if (anyOf(c,"job_type"))    return pick(i,"ETL","RPT","REFRESH","SYNC","EXPORT");
        if (anyOf(c,"target_ref"))  return "ds_" + i;
        if (anyOf(c,"reaction","event_type")) return pick(i,"LIKE","VIEW","APRV","EXPRT","SYNC");
        if (anyOf(c,"relationship")) return pick(i,"PRIM","SEC","BKUP","PART","AFF");
        if (anyOf(c,"account_type")) return pick(i,"CURR","SAV","INV","ESC","PAY");
        // generic fallback
        return "Val_" + c.substring(0, Math.min(c.length(), 6)) + "_" + i;
    }

    private String pick(int i, String... opts) { return opts[(i - 1) % opts.length]; }
    private boolean anyOf(String t, String... p) { for (String s : p) if (t.contains(s)) return true; return false; }
    private double round(double v, int dp) { double f = Math.pow(10, dp); return Math.round(v * f) / f; }

    // ── JDBC param setter ──────────────────────────────────────────────────
    private void setParam(PreparedStatement ps, int idx, Object val, String dt, String udt) throws SQLException {
        if (val == null)                    { ps.setNull(idx, Types.NULL); return; }
        if (val instanceof Boolean b)       { ps.setBoolean(idx, b); return; }
        if (val instanceof LocalDateTime l) { ps.setTimestamp(idx, Timestamp.valueOf(l)); return; }
        if (val instanceof LocalDate l)     { ps.setDate(idx, java.sql.Date.valueOf(l)); return; }
        if (val instanceof LocalTime l)     { ps.setTime(idx, Time.valueOf(l)); return; }
        if (val instanceof Long l)          { ps.setLong(idx, l); return; }
        if (val instanceof Double d) {
            ps.setBigDecimal(idx, new java.math.BigDecimal(d)
                    .setScale(4, java.math.RoundingMode.HALF_UP));
            return;
        }
        if (val instanceof String s) {
            if (dt.contains("json"))                           { ps.setObject(idx, s, Types.OTHER); return; }
            if (dt.equals("ARRAY") || udt.endsWith("[]"))     { ps.setArray(idx, ps.getConnection().createArrayOf("text", new Object[]{})); return; }
            if (dt.contains("uuid"))                           { try { ps.setObject(idx, UUID.fromString(s)); } catch (Exception e) { ps.setString(idx, s); } return; }
            if (anyOf(dt,"inet","cidr"))                       { ps.setObject(idx, s, Types.OTHER); return; }
            if (dt.contains("interval"))                       { ps.setObject(idx, s, Types.OTHER); return; }
            ps.setString(idx, s); return;
        }
        ps.setObject(idx, val);
    }
}
