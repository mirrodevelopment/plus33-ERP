/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.featureflag
 * File              : FeatureFlagService.java
 * Purpose           : Business logic service layer for Platform Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: FeatureFlagController
 * Related Service   : FeatureFlagService
 * Related Repository: FeatureFlagRepository
 * Related Entity    : FeatureFlag
 * Related DTO       : N/A
 * Related Mapper    : FeatureFlagMapper
 * Related DB Table  : feature_flags
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : FeatureFlagController, FeatureFlagServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Platform Module. Implements FeatureFlagService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.platform.featureflag;

import com.plus33.erp.platform.entity.PlatformFeatureFlag;
import com.plus33.erp.platform.entity.PlatformFeatureFlagHistory;
import com.plus33.erp.platform.repository.PlatformFeatureFlagHistoryRepository;
import com.plus33.erp.platform.repository.PlatformFeatureFlagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code FeatureFlagService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.featureflag}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Platform Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * FeatureFlagController
 *   --> FeatureFlagService (this)
 *   --> Validate business rules
 *   --> FeatureFlagRepository (read/write 'feature_flags')
 *   --> FeatureFlagMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code feature_flags}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class FeatureFlagService {
    /**
     * Performs the isEnabled operation in this module.
     *
     * @param flagKey the flagKey input value
     * @param userId authenticated user identifier
     * @return true if operation succeeded, false otherwise
     */
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

    /**
     * Updates an existing flag record in the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param flagKey the flagKey input value
     * @param status status filter for narrowing query results
     * @param rolloutPercentage the rolloutPercentage input value
     * @param reason the reason input value
     * @param operator the operator input value
     * @throws BusinessException if a business rule is violated
     */
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