package com.plus33.erp.bi.scheduler;

import com.plus33.erp.bi.entity.BiEtlJob;
import com.plus33.erp.bi.entity.BiEtlJobRun;
import com.plus33.erp.bi.repository.BiEtlJobRepository;
import com.plus33.erp.bi.repository.BiEtlJobRunRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * BiSchedulerService: Manages the ETL job lifecycle state machine.
 * Transitions jobs through CREATED -> SCHEDULED -> QUEUED -> EXTRACTING
 * -> VALIDATING -> TRANSFORMING -> LOADING -> REFRESHING_CUBES -> COMPLETED.
 * Exception paths: FAILED | CANCELLED | PAUSED | RETRYING.
 */
@Service
public class BiSchedulerService {

    private static final Logger log = LoggerFactory.getLogger(BiSchedulerService.class);
    private final BiEtlJobRepository jobRepo;
    private final BiEtlJobRunRepository jobRunRepo;
    private final JdbcTemplate jdbc;

    public BiSchedulerService(BiEtlJobRepository jobRepo, BiEtlJobRunRepository jobRunRepo, JdbcTemplate jdbc) {
        this.jobRepo = jobRepo;
        this.jobRunRepo = jobRunRepo;
        this.jdbc = jdbc;
    }

    @Transactional
    public BiEtlJobRun createAndQueueRun(Long jobId, String triggeredBy) {
        BiEtlJob job = jobRepo.findById(jobId).orElseThrow(() -> new IllegalArgumentException("ETL Job not found: " + jobId));
        BiEtlJobRun run = new BiEtlJobRun();
        run.setJobId(jobId);
        run.setStatus("QUEUED");
        run.setBatchId(UUID.randomUUID().toString());
        run.setTriggeredBy(triggeredBy);
        run.setRunNumber(jobRunRepo.findByJobIdOrderByCreatedAtDesc(jobId).size() + 1);
        run = jobRunRepo.save(run);
        job.setStatus("QUEUED");
        job.setUpdatedAt(LocalDateTime.now());
        jobRepo.save(job);
        log.info("[SCHEDULER] Queued run #{} for job={} triggeredBy={}", run.getRunNumber(), job.getJobName(), triggeredBy);
        return run;
    }

    @Transactional
    public void transitionRun(Long runId, String newStatus) {
        BiEtlJobRun run = jobRunRepo.findById(runId).orElseThrow();
        log.info("[SCHEDULER] Transitioning runId={} {} -> {}", runId, run.getStatus(), newStatus);
        run.setStatus(newStatus);
        if ("COMPLETED".equals(newStatus) || "FAILED".equals(newStatus) || "CANCELLED".equals(newStatus)) {
            run.setCompletedAt(LocalDateTime.now());
            if (run.getStartedAt() != null) {
                run.setDurationMs(java.time.Duration.between(run.getStartedAt(), run.getCompletedAt()).toMillis());
            }
        }
        jobRunRepo.save(run);
        jobRepo.findById(run.getJobId()).ifPresent(job -> {
            job.setStatus(newStatus);
            job.setUpdatedAt(LocalDateTime.now());
            jobRepo.save(job);
        });
    }

    @Transactional
    public void failRun(Long runId, String errorMessage) {
        BiEtlJobRun run = jobRunRepo.findById(runId).orElseThrow();
        run.setStatus("FAILED");
        run.setErrorMessage(errorMessage);
        run.setCompletedAt(LocalDateTime.now());
        if (run.getStartedAt() != null) {
            run.setDurationMs(java.time.Duration.between(run.getStartedAt(), run.getCompletedAt()).toMillis());
        }
        jobRunRepo.save(run);
        log.error("[SCHEDULER] RunId={} FAILED: {}", runId, errorMessage);
    }

    public List<BiEtlJob> getEnabledJobs() {
        return jobRepo.findByStatusAndEnabledTrue("CREATED");
    }

    public int countActiveRuns() {
        return jdbc.queryForObject(
            "SELECT COUNT(*) FROM bi_etl_job_run WHERE status IN ('QUEUED','EXTRACTING','VALIDATING','TRANSFORMING','LOADING','REFRESHING_CUBES')",
            Integer.class);
    }
}
