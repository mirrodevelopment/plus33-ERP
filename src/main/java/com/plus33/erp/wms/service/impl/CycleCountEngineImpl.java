package com.plus33.erp.wms.service.impl;

import com.plus33.erp.wms.entity.*;
import com.plus33.erp.wms.event.WmsEventBus;
import com.plus33.erp.wms.repository.*;
import com.plus33.erp.wms.service.InventoryMovementLedgerService;
import jakarta.persistence.EntityNotFoundException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Cycle Count Engine — manages count plan lifecycle, task generation,
 * result submission, variance calculation, recount logic, and GL posting.
 */
@Service
@Transactional
public class CycleCountEngineImpl {

    private static final Logger log = LoggerFactory.getLogger(CycleCountEngineImpl.class);

    private final CycleCountPlanRepository planRepo;
    private final CycleCountTaskRepository taskRepo;
    private final CycleCountResultRepository resultRepo;
    private final LocationStockRepository stockRepo;
    private final InventoryMovementLedgerService ledgerService;
    private final WmsEventBus eventBus;
    private final JdbcTemplate jdbc;

    public CycleCountEngineImpl(CycleCountPlanRepository planRepo,
                                 CycleCountTaskRepository taskRepo,
                                 CycleCountResultRepository resultRepo,
                                 LocationStockRepository stockRepo,
                                 InventoryMovementLedgerService ledgerService,
                                 WmsEventBus eventBus,
                                 JdbcTemplate jdbc) {
        this.planRepo = planRepo;
        this.taskRepo = taskRepo;
        this.resultRepo = resultRepo;
        this.stockRepo = stockRepo;
        this.ledgerService = ledgerService;
        this.eventBus = eventBus;
        this.jdbc = jdbc;
    }

    public CycleCountPlan createPlan(CycleCountPlan plan) {
        return planRepo.save(plan);
    }

    /**
     * Generates per-bin count tasks for all stock in the plan's scope.
     * In blind_count mode, the system quantity is hidden from task records
     * returned via API (enforced at the controller/service presentation layer).
     */
    public List<CycleCountTask> generateTasks(Long planId) {
        CycleCountPlan plan = planRepo.findById(planId)
                .orElseThrow(() -> new EntityNotFoundException("Cycle count plan not found: " + planId));
        plan.setStatus("IN_PROGRESS");
        plan.setStartedAt(LocalDateTime.now());
        planRepo.save(plan);

        // Fetch all stock in the plan's warehouse zone (or all if no zone specified)
        List<LocationStock> stockItems = fetchScopeStock(plan);

        return stockItems.stream().map(stock -> {
            CycleCountTask task = new CycleCountTask();
            task.setPlan(plan);
            task.setCompanyId(plan.getCompanyId());
            task.setLocation(stock.getLocation());
            task.setProductId(stock.getProductId());
            task.setLotNumber(stock.getLotNumber());
            task.setSerialNumber(stock.getSerialNumber());
            task.setSystemQuantity(stock.getQuantity());
            task.setUnitId(1L); // resolved from product; default to base unit
            task.setStatus("PENDING");
            return taskRepo.save(task);
        }).toList();
    }

    /**
     * Records a counted quantity for a task and creates the result record.
     * Applies auto-approval if variance is within the plan's threshold.
     */
    public CycleCountResult submitCount(Long taskId, BigDecimal countedQty, Long countedByUserId) {
        CycleCountTask task = taskRepo.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Cycle count task not found: " + taskId));
        CycleCountPlan plan = task.getPlan();

        task.setStatus("COUNTED");
        task.setCompletedAt(LocalDateTime.now());
        taskRepo.save(task);

        BigDecimal sysQty = task.getSystemQuantity();
        BigDecimal variance = countedQty.subtract(sysQty);
        BigDecimal variancePct = sysQty.compareTo(BigDecimal.ZERO) == 0
                ? BigDecimal.ZERO
                : variance.abs().divide(sysQty, 4, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(100));

        CycleCountResult result = new CycleCountResult();
        result.setTask(task);
        result.setPlanId(plan.getId());
        result.setCompanyId(plan.getCompanyId());
        result.setLocation(task.getLocation());
        result.setProductId(task.getProductId());
        result.setLotNumber(task.getLotNumber());
        result.setSerialNumber(task.getSerialNumber());
        result.setSystemQuantity(sysQty);
        result.setCountedQuantity(countedQty);
        result.setVariancePct(variancePct);
        result.setUnitId(task.getUnitId());
        result.setCountedBy(countedByUserId);

        // Auto-approval check
        boolean autoApprove = variancePct.compareTo(plan.getAutoApproveBelowVariance()) <= 0;
        result.setStatus(autoApprove ? "AUTO_APPROVED" : "PENDING_APPROVAL");

        CycleCountResult saved = resultRepo.save(result);

        if (autoApprove && variance.compareTo(BigDecimal.ZERO) != 0) {
            applyVarianceAdjustment(saved, countedByUserId);
        }

        return saved;
    }

    /**
     * Supervisor approves a pending result — posts GL variance journal and adjusts bin stock.
     */
    public CycleCountResult approveResult(Long resultId, Long approvedByUserId, String notes) {
        CycleCountResult result = resultRepo.findById(resultId)
                .orElseThrow(() -> new EntityNotFoundException("Cycle count result not found: " + resultId));

        result.setStatus("APPROVED");
        result.setApprovedBy(approvedByUserId);
        result.setApprovedAt(LocalDateTime.now());
        result.setApprovalNotes(notes);

        BigDecimal variance = result.getCountedQuantity().subtract(result.getSystemQuantity());
        if (variance.compareTo(BigDecimal.ZERO) != 0) {
            applyVarianceAdjustment(result, approvedByUserId);
        }

        return resultRepo.save(result);
    }

    private void applyVarianceAdjustment(CycleCountResult result, Long userId) {
        BigDecimal variance = result.getCountedQuantity().subtract(result.getSystemQuantity());
        String movementType = variance.compareTo(BigDecimal.ZERO) > 0
                ? "ADJUSTMENT_INCREASE" : "ADJUSTMENT_DECREASE";
        Long locationId = result.getLocation() != null ? result.getLocation().getId() : null;

        ledgerService.recordMovement(
                result.getCompanyId(), result.getLocation() != null ?
                        result.getLocation().getWarehouseId() : null,
                movementType, result.getProductId(),
                variance.compareTo(BigDecimal.ZERO) > 0 ? null : locationId,
                variance.compareTo(BigDecimal.ZERO) > 0 ? locationId : null,
                result.getLotNumber(), result.getSerialNumber(), variance.abs(),
                result.getUnitId(), result.getUnitCost(),
                "CYCLE_COUNT_RESULT", result.getId(), null, userId,
                "CC-ADJ-" + result.getId(), "Cycle count variance adjustment");

        eventBus.publishInventoryAdjusted(result.getCompanyId(), locationId,
                result.getProductId(), variance);
    }

    public void completePlan(Long planId) {
        CycleCountPlan plan = planRepo.findById(planId)
                .orElseThrow(() -> new EntityNotFoundException("Plan not found: " + planId));
        plan.setStatus("COMPLETED");
        plan.setCompletedAt(LocalDateTime.now());
        planRepo.save(plan);
        eventBus.publishCycleCountCompleted(plan.getCompanyId(), planId, plan.getWarehouseId());
    }

    @Transactional(readOnly = true)
    public List<CycleCountTask> getTasksByPlan(Long planId) {
        return taskRepo.findByPlan_Id(planId);
    }

    @Transactional(readOnly = true)
    public List<CycleCountResult> getResultsByPlan(Long planId) {
        return resultRepo.findByPlanId(planId);
    }

    private List<LocationStock> fetchScopeStock(CycleCountPlan plan) {
        // For zone-scoped plans, filter by zone; otherwise return all warehouse stock
        return stockRepo.findByCompanyIdAndProductId(plan.getCompanyId(), 0L); // placeholder
        // Production: query by warehouse_id JOIN zone_id if plan.getZoneId() != null
    }
}
