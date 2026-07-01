package com.plus33.erp.bi.etl;

import com.plus33.erp.bi.entity.BiCdcWatermark;
import com.plus33.erp.bi.entity.BiEtlJobRun;
import com.plus33.erp.bi.repository.BiCdcWatermarkRepository;
import com.plus33.erp.bi.repository.BiEtlJobRunRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

/**
 * ExtractionEngine: Reads from OLTP event stores using CDC watermarks.
 * Advances the watermark atomically on successful extraction.
 */
@Service
public class ExtractionEngine {

    private static final Logger log = LoggerFactory.getLogger(ExtractionEngine.class);
    private final BiCdcWatermarkRepository watermarkRepo;
    private final BiEtlJobRunRepository jobRunRepo;

    public ExtractionEngine(BiCdcWatermarkRepository watermarkRepo, BiEtlJobRunRepository jobRunRepo) {
        this.watermarkRepo = watermarkRepo;
        this.jobRunRepo = jobRunRepo;
    }

    @Transactional
    public ExtractionResult extract(Long jobId, String sourceModule, String sourceTable) {
        log.info("[ETL:EXTRACT] Starting extraction: module={} table={}", sourceModule, sourceTable);
        String batchId = UUID.randomUUID().toString();
        Optional<BiCdcWatermark> wm = watermarkRepo.findBySourceModuleAndSourceTable(sourceModule, sourceTable);
        Long fromEventId = wm.map(BiCdcWatermark::getLastEventId).orElse(0L);

        // Update job run status to EXTRACTING
        jobRunRepo.findTopByJobIdAndStatusOrderByCreatedAtDesc(jobId, "QUEUED").ifPresent(run -> {
            run.setStatus("EXTRACTING");
            run.setStartedAt(LocalDateTime.now());
            run.setBatchId(batchId);
            run.setWatermarkFrom(fromEventId);
            jobRunRepo.save(run);
        });

        log.info("[ETL:EXTRACT] Batch={} watermarkFrom={}", batchId, fromEventId);
        return new ExtractionResult(batchId, sourceModule, sourceTable, fromEventId);
    }

    @Transactional
    public void advanceWatermark(String sourceModule, String sourceTable, Long toEventId, int extractedCount) {
        BiCdcWatermark wm = watermarkRepo.findBySourceModuleAndSourceTable(sourceModule, sourceTable)
                .orElseGet(() -> { BiCdcWatermark n = new BiCdcWatermark(); n.setSourceModule(sourceModule); n.setSourceTable(sourceTable); return n; });
        wm.setLastEventId(toEventId);
        wm.setLastRun(LocalDateTime.now());
        watermarkRepo.save(wm);
        log.info("[ETL:WATERMARK] Advanced watermark: {}.{} -> eventId={} records={}", sourceModule, sourceTable, toEventId, extractedCount);
    }

    public static class ExtractionResult {
        private final String batchId;
        private final String sourceModule;
        private final String sourceTable;
        private final Long watermarkFrom;
        public ExtractionResult(String batchId, String module, String table, Long wmFrom) {
            this.batchId = batchId; this.sourceModule = module; this.sourceTable = table; this.watermarkFrom = wmFrom;
        }
        public String getBatchId() { return batchId; }
        public String getSourceModule() { return sourceModule; }
        public String getSourceTable() { return sourceTable; }
        public Long getWatermarkFrom() { return watermarkFrom; }
    }
}
