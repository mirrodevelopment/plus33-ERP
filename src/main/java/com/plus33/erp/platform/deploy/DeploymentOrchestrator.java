package com.plus33.erp.platform.deploy;

import com.plus33.erp.platform.entity.PlatformDeploymentGroup;
import com.plus33.erp.platform.entity.PlatformDeploymentHistory;
import com.plus33.erp.platform.repository.PlatformDeploymentGroupRepository;
import com.plus33.erp.platform.repository.PlatformDeploymentHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class DeploymentOrchestrator {
    @Autowired PlatformDeploymentGroupRepository groupRepo;
    @Autowired PlatformDeploymentHistoryRepository historyRepo;

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