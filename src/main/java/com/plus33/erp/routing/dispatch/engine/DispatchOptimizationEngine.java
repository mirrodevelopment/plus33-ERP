package com.plus33.erp.routing.dispatch.engine;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class DispatchOptimizationEngine {
    @Autowired PlatformDispatchPolicyRepository policyRepo;
    @Autowired PlatformDispatchAssignmentRepository assignmentRepo;
    @Autowired PlatformDispatchAuditLogRepository auditRepo;

    @Transactional
    public PlatformDispatchPolicy createPolicy(String code, String strategy) {
        PlatformDispatchPolicy p = new PlatformDispatchPolicy();
        p.setPolicyCode(code);
        p.setDispatchStrategy(strategy);
        p.setPriorityWeight(BigDecimal.valueOf(0.45));
        p.setVehicleSelectionStrategy("HIGHEST_UTILIZATION_FIRST");
        p.setDriverSelectionStrategy("BALANCED_SHIFT_HOURS");
        p.setOptimizationGoal("MinimizeTotalFleetCost");
        p.setPlanningHorizonMins(720);
        p.setAllowPartialLoad(true);
        p.setAllowSplitDelivery(false);
        p.setAllowDynamicReroute(true);
        p.setEnabled(true);
        return policyRepo.save(p);
    }

    @Transactional
    public PlatformDispatchAssignment dispatchJob(String dispatchCode, Long vehicleId, Long driverId, Long routeId, Long shipmentId) {
        PlatformDispatchAssignment assignment = new PlatformDispatchAssignment();
        assignment.setDispatchCode(dispatchCode);
        assignment.setVehicleId(vehicleId);
        assignment.setDriverId(driverId);
        assignment.setRouteId(routeId);
        assignment.setShipmentId(shipmentId);
        assignment.setAssignmentStatus("ASSIGNED");
        assignment.setAssignedTime(LocalDateTime.now());
        assignment.setEstimatedEta(LocalDateTime.now().plusHours(4));
        assignment = assignmentRepo.save(assignment);

        PlatformDispatchAuditLog audit = new PlatformDispatchAuditLog();
        audit.setOptimizerVersion("v57.0-AI-ENGINE");
        audit.setPlanningTimeMs(485L);
        audit.setDecisionTrace("MATCHING(vehicle=" + vehicleId + ", driver=" + driverId + ")");
        audit.setExecutionId("EXEC-ID-AI-DISPATCH");
        auditRepo.save(audit);

        return assignment;
    }
}