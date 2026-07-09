/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.controller
 * File              : BiEtlController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Bi Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BiEtlController
 * Related Service   : BiEtlControllerService, BiEtlControllerServiceImpl
 * Related Repository: BiEtlJobRepository, BiEtlJobRunRepository
 * Related Entity    : BiEtlController
 * Related DTO       : N/A
 * Related Mapper    : BiEtlControllerMapper
 * Related DB Table  : bi_etl_controllers
 * Related REST APIs : GET /api/bi/etl/jobs, GET /api/bi/etl/jobs/{jobId}/runs, POST /api/bi/etl/jobs/{jobId}/trigger
 * Depends On        : None
 * Used By           : Bi Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Bi Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: GET /api/bi/etl/jobs, GET /api/bi/etl/jobs/{jobId}/runs, POST /api/bi/etl/jobs/{jobId}/trigger
 ******************************************************************************/
package com.plus33.erp.bi.controller;

import com.plus33.erp.bi.entity.BiEtlJob;
import com.plus33.erp.bi.entity.BiEtlJobRun;
import com.plus33.erp.bi.repository.BiEtlJobRepository;
import com.plus33.erp.bi.repository.BiEtlJobRunRepository;
import com.plus33.erp.bi.scheduler.BiSchedulerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Bi Module</b>
 *
 * <p><b>Class  :</b> {@code BiEtlController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.bi.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to BiEtlService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> BiEtlController.endpoint()
 *   --> BiEtlService.method()
 *   --> BiEtlRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> GET /api/bi/etl/jobs, GET /api/bi/etl/jobs/{jobId}/runs, POST /api/bi/etl/jobs/{jobId}/trigger</p>
 * <p><b>Module Deps      :</b> Bi</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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

    /**
     * Retrieves a paginated list of all jobs records.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/jobs")
    public ResponseEntity<List<BiEtlJob>> getAllJobs() {
        return ResponseEntity.ok(jobRepo.findAll());
    }

    /**
     * Retrieves job runs data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param jobId the jobId input value
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/jobs/{jobId}/runs")
    public ResponseEntity<List<BiEtlJobRun>> getJobRuns(@PathVariable Long jobId) {
        return ResponseEntity.ok(jobRunRepo.findByJobIdOrderByCreatedAtDesc(jobId));
    }

    /**
     * Performs the triggerJob operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param jobId the jobId input value
     * @param triggeredBy the triggeredBy input value
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/jobs/{jobId}/trigger")
    public ResponseEntity<BiEtlJobRun> triggerJob(@PathVariable Long jobId, @RequestParam(defaultValue = "manual") String triggeredBy) {
        BiEtlJobRun run = schedulerService.createAndQueueRun(jobId, triggeredBy);
        return ResponseEntity.ok(run);
    }
}