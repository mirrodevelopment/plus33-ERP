/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.controller
 * File              : WmsReplenishmentController.java
 * Purpose           : REST Controller exposing HTTP endpoints for Wms Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: WmsReplenishmentController
 * Related Service   : WmsReplenishmentControllerService, WmsReplenishmentControllerServiceImpl
 * Related Repository: WmsReplenishmentControllerRepository
 * Related Entity    : WmsReplenishmentController
 * Related DTO       : ApiResponse
 * Related Mapper    : WmsReplenishmentControllerMapper
 * Related DB Table  : wms_replenishment_controllers
 * Related REST APIs : POST /api/v1/wms/replenishment/plans, POST /api/v1/wms/replenishment/evaluate, POST /api/v1/wms/replenishment/tasks/{taskId}/complete, GET /api/v1/wms/replenishment/tasks/open
 * Depends On        : Common Module
 * Used By           : Wms Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * REST Controller for Wms Module. Exposes HTTP endpoints secured by @PreAuthorize. Delegates to service layer. Returns ApiResponse<T>. APIs: POST /api/v1/wms/replenishment/plans, POST /api/v1/wms/replenishment/evaluate, POST /api/v1/wms/replenishment/tasks/{taskId}/complete, GET /api/v1/wms/replenishment/tasks/open
 ******************************************************************************/
package com.plus33.erp.wms.controller;

import com.plus33.erp.common.dto.ApiResponse;
import com.plus33.erp.wms.entity.ReplenishmentPlan;
import com.plus33.erp.wms.entity.ReplenishmentTask;
import com.plus33.erp.wms.service.impl.ReplenishmentEngineImpl;
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
 * <p><b>Class  :</b> {@code WmsReplenishmentController}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.controller}</p>
 * <p><b>Layer  :</b> REST Controller: HTTP endpoints layer. Secured by JWT + @PreAuthorize. Delegates to WmsReplenishmentService.</p>
 *
 * <p><b>Request Flow:</b></p>
 * <pre>
 * HTTP Request
 *   --> JWT Auth Filter (validate Bearer token)
 *   --> @PreAuthorize (permission check)
 *   --> WmsReplenishmentController.endpoint()
 *   --> WmsReplenishmentService.method()
 *   --> WmsReplenishmentRepository (PostgreSQL)
 *   --> ApiResponse wrapped in ResponseEntity
 *   --> JSON response to Frontend
 * </pre>
 *
 * <p><b>REST Endpoints    :</b> POST /api/v1/wms/replenishment/plans, POST /api/v1/wms/replenishment/evaluate, POST /api/v1/wms/replenishment/tasks/{taskId}/complete, GET /api/v1/wms/replenishment/tasks/open</p>
 * <p><b>Module Deps      :</b> Common, Wms</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@RestController("wmsReplenishmentController")
@RequestMapping("/api/v1/wms/replenishment")
@Tag(name = "Pick-Face Replenishment", description = "APIs for min/max replenishment rules and task execution")
public class WmsReplenishmentController {

    private final ReplenishmentEngineImpl replenishmentEngine;

    public WmsReplenishmentController(ReplenishmentEngineImpl replenishmentEngine) {
        this.replenishmentEngine = replenishmentEngine;
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
    @Operation(summary = "Create replenishment plan", description = "Defines min/max thresholds for pick-face bin replenishment")
    public ResponseEntity<ApiResponse<ReplenishmentPlan>> createPlan(@RequestBody ReplenishmentPlan plan) {
        ReplenishmentPlan saved = replenishmentEngine.createPlan(plan);
        return new ResponseEntity<>(ApiResponse.success("Replenishment plan created", saved), HttpStatus.CREATED);
    }

    /**
     * Performs the evaluateRules operation in this module.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data List of matching records
     */
    @PostMapping("/evaluate")
    @PreAuthorize("hasAuthority('WMS_OPERATIONS')")
    @Operation(summary = "Evaluate min/max rules", description = "Triggers automated replenishment sweep for warehouse")
    public ResponseEntity<ApiResponse<List<ReplenishmentTask>>> evaluateRules(@RequestParam Long companyId,
                                                                               @RequestParam Long warehouseId) {
        List<ReplenishmentTask> triggered = replenishmentEngine.evaluateAndTrigger(companyId, warehouseId);
        return ResponseEntity.ok(ApiResponse.success("Replenishment evaluation complete", triggered));
    }

    /**
     * Completes the task workflow and finalizes the record status.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data
     */
    @PostMapping("/tasks/{taskId}/complete")
    @PreAuthorize("hasAuthority('WMS_OPERATIONS')")
    @Operation(summary = "Complete replenishment task", description = "Confirms bin movement and updates replenishment task status")
    public ResponseEntity<ApiResponse<ReplenishmentTask>> completeTask(@PathVariable Long taskId,
                                                                         @RequestParam BigDecimal movedQty,
                                                                         @RequestParam Long operatorId) {
        ReplenishmentTask completed = replenishmentEngine.completeTask(taskId, movedQty, operatorId);
        return ResponseEntity.ok(ApiResponse.success("Replenishment task completed", completed));
    }

    /**
     * Retrieves open tasks data from the database.
     *
     * <p><em>Requires JWT authentication. Permission enforced via @PreAuthorize annotation.</em></p>
     *
     * @return HTTP ResponseEntity wrapping ApiResponse with status code and data List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @GetMapping("/tasks/open")
    @PreAuthorize("hasAuthority('WMS_VIEW')")
    @Operation(summary = "Get open replenishment tasks", description = "Retrieves pending replenishment tasks")
    public ResponseEntity<ApiResponse<List<ReplenishmentTask>>> getOpenTasks(@RequestParam Long companyId,
                                                                              @RequestParam Long warehouseId) {
        List<ReplenishmentTask> openTasks = replenishmentEngine.getOpenTasks(companyId, warehouseId);
        return ResponseEntity.ok(ApiResponse.success("Open tasks retrieved", openTasks));
    }
}