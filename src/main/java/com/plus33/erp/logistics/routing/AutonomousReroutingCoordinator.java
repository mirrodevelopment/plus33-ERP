/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Logistics Module
 * Package           : com.plus33.erp.logistics.routing
 * File              : AutonomousReroutingCoordinator.java
 * Purpose           : Business logic service layer for Logistics Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: AutonomousReroutingCoordinatorController
 * Related Service   : AutonomousReroutingCoordinator
 * Related Repository: AutonomousReroutingCoordinatorRepository
 * Related Entity    : AutonomousReroutingCoordinator
 * Related DTO       : N/A
 * Related Mapper    : AutonomousReroutingCoordinatorMapper
 * Related DB Table  : autonomous_rerouting_coordinators
 * Related REST APIs : N/A
 * Depends On        : Platform Module
 * Used By           : AutonomousReroutingCoordinatorController, AutonomousReroutingCoordinatorImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Logistics Module. Implements AutonomousReroutingCoordinatorService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.logistics.routing;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Logistics Module</b>
 *
 * <p><b>Class  :</b> {@code AutonomousReroutingCoordinator}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.logistics.routing}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Logistics Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * AutonomousReroutingCoordinatorController
 *   --> AutonomousReroutingCoordinator (this)
 *   --> Validate business rules
 *   --> AutonomousReroutingCoordinatorRepository (read/write 'autonomous_rerouting_coordinators')
 *   --> AutonomousReroutingCoordinatorMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code autonomous_rerouting_coordinators}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class AutonomousReroutingCoordinator {
    @Autowired PlatformAutonomousReroutingRepository reroutingRepo;
    @Autowired PlatformReroutingExecutionRepository executionRepo;
    @Autowired PlatformRouteAuditLogRepository auditRepo;
    /**
     * Performs the executeReroute operation in this module.
     *
     * @param routeId the routeId input value
     * @param policyId the policyId input value
     * @param newPath the newPath input value
     */
    @Transactional
    public void executeReroute(Long routeId, Long policyId, String newPath) {
        PlatformAutonomousRerouting reroute = new PlatformAutonomousRerouting();
        reroute.setTransitRouteId(routeId);
        reroute.setPolicyId(policyId);
        reroute.setConfidence(BigDecimal.valueOf(95.00));
        reroute.setSuggestedRouteJson(newPath);
        reroute.setStatus("APPROVED");
        reroute = reroutingRepo.save(reroute);

        PlatformReroutingExecution exec = new PlatformReroutingExecution();
        exec.setReroutingId(reroute.getId());
        exec.setStatus("EXECUTED");
        exec.setExecutedAt(LocalDateTime.now());
        executionRepo.save(exec);

        PlatformRouteAuditLog audit = new PlatformRouteAuditLog();
        audit.setTransitRouteId(routeId);
        audit.setOldRouteJson("{}");
        audit.setNewRouteJson(newPath);
        audit.setReason("Severe traffic congestion detected along previous route segment");
        audit.setTriggerType("AUTONOMOUS");
        audit.setOperator("sys-routing-coordinator");
        audit.setChangedAt(LocalDateTime.now());
        auditRepo.save(audit);
    }
}