/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.controller
 * File              : CycleCountController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Wms Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CycleCountController
 * Related Service   : CycleCountControllerService, CycleCountControllerServiceImpl
 * Related Repository: CycleCountControllerRepository
 * Related Entity    : CycleCountController
 * Related DTO       : ApiResponse
 * Related Mapper    : CycleCountControllerMapper
 * Related DB Table  : cycle_count_controllers
 * Related REST APIs : POST /api/v1/wms/cycle-counts/plans, POST /api/v1/wms/cycle-counts/plans/{planId}/generate-tasks, POST /api/v1/wms/cycle-counts/tasks/{taskId}/submit, POST /api/v1/wms/cycle-counts/results/{resultId}/approve
 * Depends On        : Common Module
 * Used By           : Wms Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Wms Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: POST /api/v1/wms/cycle-counts/plans, POST /api/v1/wms/cycle-counts/plans/{planId}/generate-tasks, POST /api/v1/wms/cycle-counts/tasks/{taskId}/submit, POST /api/v1/wms/cycle-counts/results/{resultId}/approve
 ******************************************************************************/
package com.plus33.erp.wms.controller;

import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.wms.entity.CycleCountPlan;
import com.plus33.erp.wms.entity.CycleCountResult;
import com.plus33.erp.wms.entity.CycleCountTask;
import com.plus33.erp.wms.service.impl.CycleCountEngineImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code CycleCountController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to CycleCountService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> CycleCountController.endpoint()
 *   --> CycleCountService.method()
 *   --> CycleCountRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> POST /api/v1/wms/cycle-counts/plans, POST /api/v1/wms/cycle-counts/plans/{planId}/generate-tasks, POST /api/v1/wms/cycle-counts/tasks/{taskId}/submit, POST /api/v1/wms/cycle-counts/results/{resultId}/approve, POST /api/v1/wms/cycle-counts/plans/{planId}/complete</p>
 * <p><b>Module Deps      :</b> Common, Wms</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController
@RequestMapping("/api/v1/wms/cycle-counts")
@Tag(name = "Cycle Counting & Audit", description = "APIs for count plan scheduling, task generation, submission, and GL approval")
public class CycleCountController {

    private final CycleCountEngineImpl cycleCountEngine;

    public CycleCountController(CycleCountEngineImpl cycleCountEngine) {
        this.cycleCountEngine = cycleCountEngine;
    }

    /**
     * Creates a new plan and persists it to the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param plan the plan input value
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/plans")
    @PreAuthorize("hasAuthority('WMS_MANAGE')")
    @Operation(summary = "Create count plan", description = "Creates a new cycle count plan")
    public ResponseEntity<ApiResponse<CycleCountPlan>> createPlan(@RequestBody CycleCountPlan plan) {
        CycleCountPlan saved = cycleCountEngine.createPlan(plan);
        return new ResponseEntity<>(ApiResponse.success("Plan created", saved), HttpStatus.CREATED);
    }

    /**
     * Generates the tasks based on input parameters and business rules.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param planId the planId input value
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data List of matching records
     */
    @PostMapping("/plans/{planId}/generate-tasks")
    @PreAuthorize("hasAuthority('WMS_MANAGE')")
    @Operation(summary = "Generate count tasks", description = "Generates bin-level count tasks for plan scope")
    public ResponseEntity<ApiResponse<List<CycleCountTask>>> generateTasks(@PathVariable Long planId) {
        List<CycleCountTask> tasks = cycleCountEngine.generateTasks(planId);
        return ResponseEntity.ok(ApiResponse.success("Tasks generated", tasks));
    }

    /**
     * Submits the count for approval. Transitions DRAFT to SUBMITTED status.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/tasks/{taskId}/submit")
    @PreAuthorize("hasAuthority('WMS_OPERATIONS')")
    @Operation(summary = "Submit counted quantity", description = "Submits physically counted quantity for task")
    public ResponseEntity<ApiResponse<CycleCountResult>> submitCount(@PathVariable Long taskId,
                                                                      @RequestParam BigDecimal countedQty,
                                                                      @RequestParam Long countedByUserId) {
        CycleCountResult result = cycleCountEngine.submitCount(taskId, countedQty, countedByUserId);
        return ResponseEntity.ok(ApiResponse.success("Count submitted", result));
    }

    /**
     * Approves the result, transitions to APPROVED status, and posts GL journal entries.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     * @throws BusinessException if a business rule is violated
     */
    @PostMapping("/results/{resultId}/approve")
    @PreAuthorize("hasAuthority('WMS_MANAGE')")
    @Operation(summary = "Approve count result", description = "Supervisor approves variance and posts GL adjustment")
    public ResponseEntity<ApiResponse<CycleCountResult>> approveResult(@PathVariable Long resultId,
                                                                         @RequestParam Long approvedByUserId,
                                                                         @RequestParam(required = false) String notes) {
        CycleCountResult approved = cycleCountEngine.approveResult(resultId, approvedByUserId, notes);
        return ResponseEntity.ok(ApiResponse.success("Result approved and GL posted", approved));
    }

    /**
     * Completes the plan workflow and finalizes the record status.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param planId the planId input value
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/plans/{planId}/complete")
    @PreAuthorize("hasAuthority('WMS_MANAGE')")
    @Operation(summary = "Complete plan", description = "Marks cycle count plan complete")
    public ResponseEntity<ApiResponse<Void>> completePlan(@PathVariable Long planId) {
        cycleCountEngine.completePlan(planId);
        return ResponseEntity.ok(ApiResponse.success("Plan completed", null));
    }

    /**
     * Retrieves tasks data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param planId the planId input value
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/plans/{planId}/tasks")
    @PreAuthorize("hasAuthority('WMS_VIEW')")
    @Operation(summary = "Get tasks by plan", description = "Retrieves tasks generated for plan")
    public ResponseEntity<ApiResponse<List<CycleCountTask>>> getTasks(@PathVariable Long planId) {
        return ResponseEntity.ok(ApiResponse.success("Tasks retrieved", cycleCountEngine.getTasksByPlan(planId)));
    }

    /**
     * Retrieves results data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @param planId the planId input value
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/plans/{planId}/results")
    @PreAuthorize("hasAuthority('WMS_VIEW')")
    @Operation(summary = "Get results by plan", description = "Retrieves count results and variances for plan")
    public ResponseEntity<ApiResponse<List<CycleCountResult>>> getResults(@PathVariable Long planId) {
        return ResponseEntity.ok(ApiResponse.success("Results retrieved", cycleCountEngine.getResultsByPlan(planId)));
    }
}