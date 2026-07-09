/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Esm Module
 * Package           : com.plus33.erp.esm.service
 * File              : PreventiveMaintenanceEngine.java
 * Purpose           : Business logic service layer for Esm Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PreventiveMaintenanceEngineController
 * Related Service   : PreventiveMaintenanceEngine
 * Related Repository: PreventiveMaintenancePlanRepository, EsmWorkOrderRepository
 * Related Entity    : PreventiveMaintenanceEngine
 * Related DTO       : N/A
 * Related Mapper    : PreventiveMaintenanceEngineMapper
 * Related DB Table  : preventive_maintenance_engines
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PreventiveMaintenanceEngineController, PreventiveMaintenanceEngineImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Esm Module. Implements PreventiveMaintenanceEngineService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
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

/**
 * <b>PLUS33 Coffee ERP -- Esm Module</b>
 *
 * <p><b>Class  :</b> {@code PreventiveMaintenanceEngine}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.esm.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Esm Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * PreventiveMaintenanceEngineController
 *   --> PreventiveMaintenanceEngine (this)
 *   --> Validate business rules
 *   --> PreventiveMaintenanceEngineRepository (read/write 'preventive_maintenance_engines')
 *   --> PreventiveMaintenanceEngineMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code preventive_maintenance_engines}</p>
 * <p><b>Module Deps      :</b> Esm</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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

    /**
     * Performs the evaluatePmPlans operation in this module.
     *
     */
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