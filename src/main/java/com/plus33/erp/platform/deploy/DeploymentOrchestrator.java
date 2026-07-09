/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.deploy
 * File              : DeploymentOrchestrator.java
 * Purpose           : Business logic service layer for Platform Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: DeploymentOrchestratorController
 * Related Service   : DeploymentOrchestrator
 * Related Repository: DeploymentOrchestratorRepository
 * Related Entity    : DeploymentOrchestrator
 * Related DTO       : N/A
 * Related Mapper    : DeploymentOrchestratorMapper
 * Related DB Table  : deployment_orchestrators
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : DeploymentOrchestratorController, DeploymentOrchestratorImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Platform Module. Implements DeploymentOrchestratorService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.platform.deploy;

import com.plus33.erp.platform.entity.PlatformDeploymentGroup;
import com.plus33.erp.platform.entity.PlatformDeploymentHistory;
import com.plus33.erp.platform.repository.PlatformDeploymentGroupRepository;
import com.plus33.erp.platform.repository.PlatformDeploymentHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code DeploymentOrchestrator}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.deploy}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Platform Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * DeploymentOrchestratorController
 *   --> DeploymentOrchestrator (this)
 *   --> Validate business rules
 *   --> DeploymentOrchestratorRepository (read/write 'deployment_orchestrators')
 *   --> DeploymentOrchestratorMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code deployment_orchestrators}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class DeploymentOrchestrator {
    @Autowired PlatformDeploymentGroupRepository groupRepo;
    @Autowired PlatformDeploymentHistoryRepository historyRepo;
    /**
     * Performs the deployVersion operation in this module.
     *
     * @param groupName the groupName input value
     * @param targetVersion the targetVersion input value
     * @param operator the operator input value
     */
    @Transactional
    public void deployVersion(String groupName, String targetVersion, String operator) {
        PlatformDeploymentGroup group = groupRepo.findAll().stream()
                .filter(g -> g.getGroupName().equalsIgnoreCase(groupName))
                .findFirst()
                .orElseGet(() -> {
                    PlatformDeploymentGroup newGroup = new PlatformDeploymentGroup();
                    newGroup.setGroupName(groupName);
                    newGroup.setCanaryWeight(0);
                    newGroup.setActiveRouter(false);
                    return newGroup;
                });

        group.setTargetVersion(targetVersion);
        group.setUpdatedAt(LocalDateTime.now());
        groupRepo.save(group);

        PlatformDeploymentHistory history = new PlatformDeploymentHistory();
        history.setDeploymentVersion(targetVersion);
        history.setStatus("DEPLOYING");
        history.setChangelog("Deploying version " + targetVersion + " to " + groupName);
        history.setDeployedBy(operator);
        history.setStartedAt(LocalDateTime.now());
        historyRepo.save(history);
    }

    /**
     * Completes the deployment workflow and finalizes the record status.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param targetVersion the targetVersion input value
     * @param status status filter for narrowing query results
     */
    @Transactional
    public void completeDeployment(String targetVersion, String status) {
        PlatformDeploymentHistory history = historyRepo.findAll().stream()
                .filter(h -> h.getDeploymentVersion().equals(targetVersion) && "DEPLOYING".equals(h.getStatus()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Active deployment not found"));

        history.setStatus(status); // ACTIVE or FAILED
        history.setCompletedAt(LocalDateTime.now());
        historyRepo.save(history);
    }

    /**
     * Performs the rollbackDeployment operation in this module.
     *
     * @param targetVersion the targetVersion input value
     * @param operator the operator input value
     */
    @Transactional
    public void rollbackDeployment(String targetVersion, String operator) {
        PlatformDeploymentHistory history = historyRepo.findAll().stream()
                .filter(h -> h.getDeploymentVersion().equals(targetVersion))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Deployment record not found"));

        history.setStatus("ROLLED_BACK");
        history.setCompletedAt(LocalDateTime.now());
        historyRepo.save(history);
    }
}