package com.plus33.erp.platform.featureflag;

import com.plus33.erp.platform.entity.PlatformFeatureFlag;
import com.plus33.erp.platform.entity.PlatformFeatureFlagHistory;
import com.plus33.erp.platform.repository.PlatformFeatureFlagHistoryRepository;
import com.plus33.erp.platform.repository.PlatformFeatureFlagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class FeatureFlagService {
    @Autowired PlatformFeatureFlagRepository flagRepo;
    @Autowired PlatformFeatureFlagHistoryRepository historyRepo;

    public boolean isEnabled(String flagKey, String userId) {
        PlatformFeatureFlag flag = flagRepo.findAll().stream()
                .filter(f -> f.getFlagKey().equals(flagKey))
                .findFirst()
                .orElse(null);

        if (flag == null || !"ENABLED".equalsIgnoreCase(flag.getStatus())) {
            return false;
        }

        // Apply progressive rollout check
        if (flag.getRolloutPercentage() > 0) {
            int hash = Math.abs(userId.hashCode()) % 100;
            return hash < flag.getRolloutPercentage();
        }

        return true;
    }

    @Transactional
    public void updateFlag(String flagKey, String status, int rolloutPercentage, String reason, String operator) {
        PlatformFeatureFlag flag = flagRepo.findAll().stream()
                .filter(f -> f.getFlagKey().equals(flagKey))
                .findFirst()
                .orElseGet(() -> {
                    PlatformFeatureFlag f = new PlatformFeatureFlag();
                    f.setFlagKey(flagKey);
                    f.setCreatedAt(LocalDateTime.now());
                    return f;
                });

        String previousValue = flag.getStatus();
        flag.setStatus(status);
        flag.setRolloutPercentage(rolloutPercentage);
        flag.setUpdatedAt(LocalDateTime.now());
        flagRepo.save(flag);

        PlatformFeatureFlagHistory history = new PlatformFeatureFlagHistory();
        history.setFlagKey(flagKey);
        history.setPreviousValue(previousValue);
        history.setNewValue(status);
        history.setOperator(operator);
        history.setReason(reason);
        history.setRolloutPercentage(rolloutPercentage);
        history.setChangedAt(LocalDateTime.now());
        historyRepo.save(history);
    }
}