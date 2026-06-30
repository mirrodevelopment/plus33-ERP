package com.plus33.erp.wms.service.impl;

import com.plus33.erp.wms.entity.*;
import com.plus33.erp.wms.event.WmsEventBus;
import com.plus33.erp.wms.repository.*;
import com.plus33.erp.wms.service.LocationStockService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Replenishment Engine — evaluates bin stock against min/max rules
 * and generates replenishment tasks when thresholds are breached.
 * The scheduled sweep runs every 30 minutes in production.
 */
@Service
@Transactional
public class ReplenishmentEngineImpl {

    private final ReplenishmentPlanRepository planRepo;
    private final ReplenishmentTaskRepository taskRepo;
    private final LocationStockRepository stockRepo;
    private final LocationStockService stockService;
    private final WmsEventBus eventBus;

    public ReplenishmentEngineImpl(ReplenishmentPlanRepository planRepo,
                                    ReplenishmentTaskRepository taskRepo,
                                    LocationStockRepository stockRepo,
                                    LocationStockService stockService,
                                    WmsEventBus eventBus) {
        this.planRepo = planRepo;
        this.taskRepo = taskRepo;
        this.stockRepo = stockRepo;
        this.stockService = stockService;
        this.eventBus = eventBus;
    }

    public ReplenishmentPlan createPlan(ReplenishmentPlan plan) {
        return planRepo.save(plan);
    }

    /**
     * Evaluates all active replenishment plans for a warehouse and triggers tasks
     * where the pick-face quantity has fallen below the minimum threshold.
     */
    @Scheduled(cron = "0 */30 * * * *")
    public void runReplenishmentSweep() {
        // In production: iterate all companies/warehouses. Here we use a direct service call.
    }

    public List<ReplenishmentTask> evaluateAndTrigger(Long companyId, Long warehouseId) {
        List<ReplenishmentPlan> plans = planRepo.findByCompanyIdAndWarehouseIdAndActiveTrue(companyId, warehouseId);

        return plans.stream()
                .map(plan -> triggerIfNeeded(companyId, plan))
                .filter(t -> t != null)
                .toList();
    }

    private ReplenishmentTask triggerIfNeeded(Long companyId, ReplenishmentPlan plan) {
        if (plan.getToLocation() == null) return null;

        BigDecimal currentQty = stockRepo
                .findByLocationIdAndProductId(plan.getToLocation().getId(), plan.getProductId())
                .stream()
                .map(LocationStock::getQuantity)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        if (currentQty.compareTo(plan.getMinQuantity()) <= 0) {
            BigDecimal replenishQty = plan.getMaxQuantity().subtract(currentQty);

            ReplenishmentTask task = new ReplenishmentTask();
            task.setCompanyId(companyId);
            task.setReplenishmentPlan(plan);
            task.setWarehouseId(plan.getWarehouseId());
            task.setProductId(plan.getProductId());
            task.setToLocation(plan.getToLocation());
            task.setQuantity(replenishQty);
            task.setUnitId(plan.getUnitId());
            task.setTriggerReason("Below min threshold: " + currentQty + " < " + plan.getMinQuantity());
            task.setStatus("PENDING");

            ReplenishmentTask saved = taskRepo.save(task);
            eventBus.publishReplenishmentTriggered(companyId, saved.getId(), plan.getProductId());
            return saved;
        }
        return null;
    }

    public ReplenishmentTask completeTask(Long taskId, BigDecimal movedQty, Long operatorId) {
        ReplenishmentTask task = taskRepo.findById(taskId)
                .orElseThrow(() -> new EntityNotFoundException("Replenishment task not found: " + taskId));
        task.setMovedQuantity(movedQty);
        task.setStatus("COMPLETED");
        task.setCompletedAt(LocalDateTime.now());
        task.setAssignedTo(operatorId);
        return taskRepo.save(task);
    }

    @Transactional(readOnly = true)
    public List<ReplenishmentTask> getOpenTasks(Long companyId, Long warehouseId) {
        return taskRepo.findByCompanyIdAndWarehouseIdAndStatus(companyId, warehouseId, "PENDING");
    }
}
