package com.plus33.erp.bi.scd;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * ScdType2Engine: Manages Slowly Changing Dimension Type 2 operations.
 * Expires the current row and inserts a new version for changed attributes.
 * All dimension tables follow the pattern: effective_from, effective_to, is_current.
 */
@Service
public class ScdType2Engine {

    private static final Logger log = LoggerFactory.getLogger(ScdType2Engine.class);
    private final JdbcTemplate jdbc;

    public ScdType2Engine(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    /**
     * Expires the current active record for a given source entity ID in a dimension table.
     * Marks is_current=false and sets effective_to=today-1.
     */
    @Transactional
    public int expireCurrentRecord(String dimensionTable, String sourceIdColumn, Long sourceId) {
        String sql = "UPDATE " + dimensionTable +
                " SET is_current = false, effective_to = ? WHERE " + sourceIdColumn + " = ? AND is_current = true";
        int updated = jdbc.update(sql, LocalDate.now().minusDays(1), sourceId);
        if (updated > 0) {
            log.info("[SCD2] Expired {} record(s) in table={} for sourceId={}", updated, dimensionTable, sourceId);
        }
        return updated;
    }

    /**
     * Returns the list of dimension tables that support SCD Type 2.
     */
    public List<String> getScdDimensions() {
        return List.of(
            "dim_customer", "dim_product", "dim_supplier", "dim_employee",
            "dim_organization", "dim_account", "dim_location", "dim_project", "dim_company"
        );
    }

    /**
     * Validates that no active record violates SCD constraints (no two is_current=true rows for same source).
     */
    public List<ScdViolation> detectViolations(String dimensionTable, String sourceIdColumn) {
        String sql = "SELECT " + sourceIdColumn + ", COUNT(*) AS cnt FROM " + dimensionTable +
                " WHERE is_current = true GROUP BY " + sourceIdColumn + " HAVING COUNT(*) > 1";
        return jdbc.query(sql, (rs, i) -> new ScdViolation(dimensionTable, rs.getLong(1), rs.getInt(2)));
    }

    public static class ScdViolation {
        private final String dimensionTable;
        private final Long sourceId;
        private final int duplicateCount;
        public ScdViolation(String table, Long sourceId, int count) {
            this.dimensionTable = table; this.sourceId = sourceId; this.duplicateCount = count;
        }
        public String getDimensionTable() { return dimensionTable; }
        public Long getSourceId() { return sourceId; }
        public int getDuplicateCount() { return duplicateCount; }
    }
}
