package com.plus33.erp.logistics.routing;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class AutonomousReroutingCoordinator {
    @Autowired PlatformAutonomousReroutingRepository reroutingRepo;
    @Autowired PlatformReroutingExecutionRepository executionRepo;
    @Autowired PlatformRouteAuditLogRepository auditRepo;

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