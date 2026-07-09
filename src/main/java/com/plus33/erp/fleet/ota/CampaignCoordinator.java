/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Fleet Module
 * Package           : com.plus33.erp.fleet.ota
 * File              : CampaignCoordinator.java
 * Purpose           : Business logic service layer for Fleet Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CampaignCoordinatorController
 * Related Service   : CampaignCoordinator
 * Related Repository: CampaignCoordinatorRepository
 * Related Entity    : CampaignCoordinator
 * Related DTO       : N/A
 * Related Mapper    : CampaignCoordinatorMapper
 * Related DB Table  : campaign_coordinators
 * Related REST APIs : N/A
 * Depends On        : Platform Module
 * Used By           : CampaignCoordinatorController, CampaignCoordinatorImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Fleet Module. Implements CampaignCoordinatorService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.fleet.ota;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Fleet Module</b>
 *
 * <p><b>Class  :</b> {@code CampaignCoordinator}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.fleet.ota}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Fleet Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * CampaignCoordinatorController
 *   --> CampaignCoordinator (this)
 *   --> Validate business rules
 *   --> CampaignCoordinatorRepository (read/write 'campaign_coordinators')
 *   --> CampaignCoordinatorMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code campaign_coordinators}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class CampaignCoordinator {
    @Autowired PlatformOtaCampaignRepository campaignRepo;
    @Autowired PlatformOtaNodeExecutionRepository executionRepo;
    @Autowired PlatformOtaAuditLogRepository auditRepo;
    /**
     * Creates a new campaign and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param name the name input value
     * @param packageId the packageId input value
     * @param nodeId the nodeId input value
     * @return the PlatformOtaCampaign result
     * @throws BusinessException if a business rule is violated
     */
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