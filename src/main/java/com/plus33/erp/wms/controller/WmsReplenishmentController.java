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

@RestController("wmsReplenishmentController")
@RequestMapping("/api/v1/wms/replenishment")
@Tag(name = "Pick-Face Replenishment", description = "APIs for min/max replenishment rules and task execution")
public class WmsReplenishmentController {

    private final ReplenishmentEngineImpl replenishmentEngine;

    public WmsReplenishmentController(ReplenishmentEngineImpl replenishmentEngine) {
        this.replenishmentEngine = replenishmentEngine;
    }

    @PostMapping("/plans")
    @PreAuthorize("hasAuthority('WMS_MANAGE')")
    @Operation(summary = "Create replenishment plan", description = "Defines min/max thresholds for pick-face bin replenishment")
    public ResponseEntity<ApiResponse<ReplenishmentPlan>> createPlan(@RequestBody ReplenishmentPlan plan) {
        ReplenishmentPlan saved = replenishmentEngine.createPlan(plan);
        return new ResponseEntity<>(ApiResponse.success("Replenishment plan created", saved), HttpStatus.CREATED);
    }

    @PostMapping("/evaluate")
    @PreAuthorize("hasAuthority('WMS_OPERATIONS')")
    @Operation(summary = "Evaluate min/max rules", description = "Triggers automated replenishment sweep for warehouse")
    public ResponseEntity<ApiResponse<List<ReplenishmentTask>>> evaluateRules(@RequestParam Long companyId,
                                                                               @RequestParam Long warehouseId) {
        List<ReplenishmentTask> triggered = replenishmentEngine.evaluateAndTrigger(companyId, warehouseId);
        return ResponseEntity.ok(ApiResponse.success("Replenishment evaluation complete", triggered));
    }

    @PostMapping("/tasks/{taskId}/complete")
    @PreAuthorize("hasAuthority('WMS_OPERATIONS')")
    @Operation(summary = "Complete replenishment task", description = "Confirms bin movement and updates replenishment task status")
    public ResponseEntity<ApiResponse<ReplenishmentTask>> completeTask(@PathVariable Long taskId,
                                                                         @RequestParam BigDecimal movedQty,
                                                                         @RequestParam Long operatorId) {
        ReplenishmentTask completed = replenishmentEngine.completeTask(taskId, movedQty, operatorId);
        return ResponseEntity.ok(ApiResponse.success("Replenishment task completed", completed));
    }

    @GetMapping("/tasks/open")
    @PreAuthorize("hasAuthority('WMS_VIEW')")
    @Operation(summary = "Get open replenishment tasks", description = "Retrieves pending replenishment tasks")
    public ResponseEntity<ApiResponse<List<ReplenishmentTask>>> getOpenTasks(@RequestParam Long companyId,
                                                                              @RequestParam Long warehouseId) {
        List<ReplenishmentTask> openTasks = replenishmentEngine.getOpenTasks(companyId, warehouseId);
        return ResponseEntity.ok(ApiResponse.success("Open tasks retrieved", openTasks));
    }
}
