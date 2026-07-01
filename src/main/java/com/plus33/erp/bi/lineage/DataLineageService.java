package com.plus33.erp.bi.lineage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DataLineageService: Records end-to-end data provenance.
 * Writes lineage records and individual steps to bi_data_lineage and bi_lineage_step.
 */
@Service
public class DataLineageService {

    private static final Logger log = LoggerFactory.getLogger(DataLineageService.class);
    private final JdbcTemplate jdbc;

    public DataLineageService(JdbcTemplate jdbc) { this.jdbc = jdbc; }

    @Transactional
    public Long recordLineage(String sourceModule, String sourceTable, String targetTable,
                               String transformationType, Long jobRunId, String batchId, int records) {
        String lineageKey = UUID.randomUUID().toString();
        Long id = jdbc.queryForObject("""
            INSERT INTO bi_data_lineage(lineage_key, source_module, source_table, target_table,
                transformation_type, job_run_id, batch_id, records_processed, created_at)
            VALUES (?,?,?,?,?,?,?,?,CURRENT_TIMESTAMP)
            RETURNING id
            """, Long.class, lineageKey, sourceModule, sourceTable, targetTable, transformationType, jobRunId, batchId, records);
        log.info("[LINEAGE] Recorded: {} -> {} ({} records) id={}", sourceTable, targetTable, records, id);
        return id;
    }

    @Transactional
    public void addLineageStep(Long lineageId, int order, String stepName, String stepType,
                                String inputTable, String outputTable, int recordsIn, int recordsOut) {
        jdbc.update("""
            INSERT INTO bi_lineage_step(lineage_id, step_order, step_name, step_type, input_table, output_table,
                records_in, records_out, executed_at)
            VALUES (?,?,?,?,?,?,?,?,CURRENT_TIMESTAMP)
            """, lineageId, order, stepName, stepType, inputTable, outputTable, recordsIn, recordsOut);
    }
}
