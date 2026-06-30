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

@RestController
@RequestMapping("/api/v1/wms/cycle-counts")
@Tag(name = "Cycle Counting & Audit", description = "APIs for count plan scheduling, task generation, submission, and GL approval")
public class CycleCountController {

    private final CycleCountEngineImpl cycleCountEngine;

    public CycleCountController(CycleCountEngineImpl cycleCountEngine) {
        this.cycleCountEngine = cycleCountEngine;
    }

    @PostMapping("/plans")
    @PreAuthorize("hasAuthority('WMS_MANAGE')")
    @Operation(summary = "Create count plan", description = "Creates a new cycle count plan")
    public ResponseEntity<ApiResponse<CycleCountPlan>> createPlan(@RequestBody CycleCountPlan plan) {
        CycleCountPlan saved = cycleCountEngine.createPlan(plan);
        return new ResponseEntity<>(ApiResponse.success("Plan created", saved), HttpStatus.CREATED);
    }

    @PostMapping("/plans/{planId}/generate-tasks")
    @PreAuthorize("hasAuthority('WMS_MANAGE')")
    @Operation(summary = "Generate count tasks", description = "Generates bin-level count tasks for plan scope")
    public ResponseEntity<ApiResponse<List<CycleCountTask>>> generateTasks(@PathVariable Long planId) {
        List<CycleCountTask> tasks = cycleCountEngine.generateTasks(planId);
        return ResponseEntity.ok(ApiResponse.success("Tasks generated", tasks));
    }

    @PostMapping("/tasks/{taskId}/submit")
    @PreAuthorize("hasAuthority('WMS_OPERATIONS')")
    @Operation(summary = "Submit counted quantity", description = "Submits physically counted quantity for task")
    public ResponseEntity<ApiResponse<CycleCountResult>> submitCount(@PathVariable Long taskId,
                                                                      @RequestParam BigDecimal countedQty,
                                                                      @RequestParam Long countedByUserId) {
        CycleCountResult result = cycleCountEngine.submitCount(taskId, countedQty, countedByUserId);
        return ResponseEntity.ok(ApiResponse.success("Count submitted", result));
    }

    @PostMapping("/results/{resultId}/approve")
    @PreAuthorize("hasAuthority('WMS_MANAGE')")
    @Operation(summary = "Approve count result", description = "Supervisor approves variance and posts GL adjustment")
    public ResponseEntity<ApiResponse<CycleCountResult>> approveResult(@PathVariable Long resultId,
                                                                         @RequestParam Long approvedByUserId,
                                                                         @RequestParam(required = false) String notes) {
        CycleCountResult approved = cycleCountEngine.approveResult(resultId, approvedByUserId, notes);
        return ResponseEntity.ok(ApiResponse.success("Result approved and GL posted", approved));
    }

    @PostMapping("/plans/{planId}/complete")
    @PreAuthorize("hasAuthority('WMS_MANAGE')")
    @Operation(summary = "Complete plan", description = "Marks cycle count plan complete")
    public ResponseEntity<ApiResponse<Void>> completePlan(@PathVariable Long planId) {
        cycleCountEngine.completePlan(planId);
        return ResponseEntity.ok(ApiResponse.success("Plan completed", null));
    }

    @GetMapping("/plans/{planId}/tasks")
    @PreAuthorize("hasAuthority('WMS_VIEW')")
    @Operation(summary = "Get tasks by plan", description = "Retrieves tasks generated for plan")
    public ResponseEntity<ApiResponse<List<CycleCountTask>>> getTasks(@PathVariable Long planId) {
        return ResponseEntity.ok(ApiResponse.success("Tasks retrieved", cycleCountEngine.getTasksByPlan(planId)));
    }

    @GetMapping("/plans/{planId}/results")
    @PreAuthorize("hasAuthority('WMS_VIEW')")
    @Operation(summary = "Get results by plan", description = "Retrieves count results and variances for plan")
    public ResponseEntity<ApiResponse<List<CycleCountResult>>> getResults(@PathVariable Long planId) {
        return ResponseEntity.ok(ApiResponse.success("Results retrieved", cycleCountEngine.getResultsByPlan(planId)));
    }
}
