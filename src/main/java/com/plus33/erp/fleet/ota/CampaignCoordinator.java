package com.plus33.erp.fleet.ota;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class CampaignCoordinator {
    @Autowired PlatformOtaCampaignRepository campaignRepo;
    @Autowired PlatformOtaNodeExecutionRepository executionRepo;
    @Autowired PlatformOtaAuditLogRepository auditRepo;

    @Transactional
    public PlatformOtaCampaign createCampaign(String name, Long packageId, Long nodeId) {
        PlatformOtaCampaign c = new PlatformOtaCampaign();
        c.setCampaignName(name);
        c.setPackageId(packageId);
        c.setScheduledStart(LocalDateTime.now());
        c.setScheduledEnd(LocalDateTime.now().plusDays(2));
        c.setRolloutPercentage(100);
        c.setBatchSize(5);
        c.setFailureThreshold(3);
        c.setRollbackEnabled(true);
        c.setStatus("ACTIVE");
        c = campaignRepo.save(c);

        PlatformOtaNodeExecution exec = new PlatformOtaNodeExecution();
        exec.setCampaignId(c.getId());
        exec.setNodeId(nodeId);
        exec.setDownloadStarted(LocalDateTime.now());
        exec.setDownloadCompleted(LocalDateTime.now());
        exec.setInstallStarted(LocalDateTime.now());
        exec.setInstallCompleted(LocalDateTime.now());
        exec.setRebootRequired(true);
        exec.setRebootCompleted(true);
        exec.setExecutionStatus("SUCCESS");
        executionRepo.save(exec);

        PlatformOtaAuditLog audit = new PlatformOtaAuditLog();
        audit.setCampaignId(c.getId());
        audit.setOperator("fleet-operator");
        audit.setActionType("CREATE_CAMPAIGN");
        audit.setNewConfig("{ \"packageId\": " + packageId + " }");
        audit.setTraceId("TRACE-ID-OTA-INIT");
        auditRepo.save(audit);

        return c;
    }
}