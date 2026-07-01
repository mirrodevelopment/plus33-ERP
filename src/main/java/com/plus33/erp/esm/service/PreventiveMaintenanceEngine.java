package com.plus33.erp.esm.service;

import com.plus33.erp.esm.entity.EsmWorkOrder;
import com.plus33.erp.esm.entity.PreventiveMaintenancePlan;
import com.plus33.erp.esm.repository.EsmWorkOrderRepository;
import com.plus33.erp.esm.repository.PreventiveMaintenancePlanRepository;
import com.plus33.erp.esm.event.EsmEventBus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
public class PreventiveMaintenanceEngine {

    private final PreventiveMaintenancePlanRepository pmPlanRepository;
    private final EsmWorkOrderRepository workOrderRepository;
    private final EsmEventBus eventBus;

    public PreventiveMaintenanceEngine(PreventiveMaintenancePlanRepository pmPlanRepository,
                                       EsmWorkOrderRepository workOrderRepository,
                                       EsmEventBus eventBus) {
        this.pmPlanRepository = pmPlanRepository;
        this.workOrderRepository = workOrderRepository;
        this.eventBus = eventBus;
    }

    @Transactional
    public void evaluatePmPlans() {
        List<PreventiveMaintenancePlan> activePlans = pmPlanRepository.findByActive(true);
        for (PreventiveMaintenancePlan plan : activePlans) {
            if (LocalDate.now().isAfter(plan.getNextServiceDate()) || LocalDate.now().equals(plan.getNextServiceDate())) {
                // Generate automated work order
                EsmWorkOrder wo = new EsmWorkOrder();
                wo.setCompanyId(plan.getCompanyId());
                wo.setCustomerId(1L); // Default customer reference
                wo.setInstalledAssetId(plan.getInstalledAssetId());
                wo.setWorkOrderNumber("WO-PM-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
                wo.setStatus("DRAFT");
                wo.setPriority("MEDIUM");
                wo.setScheduledAt(LocalDateTime.now().plusDays(1));
                workOrderRepository.save(wo);

                plan.setNextServiceDate(LocalDate.now().plusDays(plan.getIntervalDays()));
                pmPlanRepository.save(plan);

                eventBus.publish("WorkOrderCreated", plan.getCompanyId(), wo.getId(), "Auto-generated PM Work Order: " + wo.getWorkOrderNumber());
            }
        }
    }
}
