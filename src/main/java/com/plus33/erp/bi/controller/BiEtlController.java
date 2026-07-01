package com.plus33.erp.bi.controller;

import com.plus33.erp.bi.entity.BiEtlJob;
import com.plus33.erp.bi.entity.BiEtlJobRun;
import com.plus33.erp.bi.repository.BiEtlJobRepository;
import com.plus33.erp.bi.repository.BiEtlJobRunRepository;
import com.plus33.erp.bi.scheduler.BiSchedulerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/bi/etl")
public class BiEtlController {

    private final BiEtlJobRepository jobRepo;
    private final BiEtlJobRunRepository jobRunRepo;
    private final BiSchedulerService schedulerService;

    public BiEtlController(BiEtlJobRepository jobRepo, BiEtlJobRunRepository jobRunRepo, BiSchedulerService schedulerService) {
        this.jobRepo = jobRepo;
        this.jobRunRepo = jobRunRepo;
        this.schedulerService = schedulerService;
    }

    @GetMapping("/jobs")
    public ResponseEntity<List<BiEtlJob>> getAllJobs() {
        return ResponseEntity.ok(jobRepo.findAll());
    }

    @GetMapping("/jobs/{jobId}/runs")
    public ResponseEntity<List<BiEtlJobRun>> getJobRuns(@PathVariable Long jobId) {
        return ResponseEntity.ok(jobRunRepo.findByJobIdOrderByCreatedAtDesc(jobId));
    }

    @PostMapping("/jobs/{jobId}/trigger")
    public ResponseEntity<BiEtlJobRun> triggerJob(@PathVariable Long jobId, @RequestParam(defaultValue = "manual") String triggeredBy) {
        BiEtlJobRun run = schedulerService.createAndQueueRun(jobId, triggeredBy);
        return ResponseEntity.ok(run);
    }
}
