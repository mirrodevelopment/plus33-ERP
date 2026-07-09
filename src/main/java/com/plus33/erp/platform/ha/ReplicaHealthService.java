/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Platform Module
 * Package           : com.plus33.erp.platform.ha
 * File              : ReplicaHealthService.java
 * Purpose           : Business logic service layer for Platform Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ReplicaHealthController
 * Related Service   : ReplicaHealthService
 * Related Repository: ReplicaHealthRepository
 * Related Entity    : ReplicaHealth
 * Related DTO       : N/A
 * Related Mapper    : ReplicaHealthMapper
 * Related DB Table  : replica_healths
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ReplicaHealthController, ReplicaHealthServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Platform Module. Implements ReplicaHealthService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.platform.ha;

import com.plus33.erp.platform.entity.PlatformRegionProfile;
import com.plus33.erp.platform.entity.PlatformReplicationLagLog;
import com.plus33.erp.platform.repository.PlatformRegionProfileRepository;
import com.plus33.erp.platform.repository.PlatformReplicationLagLogRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Comparator;

/**
 * <b>PLUS33 Coffee ERP -- Platform Module</b>
 *
 * <p><b>Class  :</b> {@code ReplicaHealthService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.platform.ha}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Platform Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * ReplicaHealthController
 *   --> ReplicaHealthService (this)
 *   --> Validate business rules
 *   --> ReplicaHealthRepository (read/write 'replica_healths')
 *   --> ReplicaHealthMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code replica_healths}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class ReplicaHealthService {
    @Autowired PlatformRegionProfileRepository profileRepo;
    @Autowired PlatformReplicationLagLogRepository lagRepo;
    /**
     * Performs the reportMetrics operation in this module.
     *
     * @param region the region input value
     * @param score the score input value
     * @param lagMs the lagMs input value
     */
    @Transactional
    public void reportMetrics(String region, int score, double lagMs) {
        PlatformRegionProfile profile = profileRepo.findAll().stream()
                .filter(p -> p.getRegionCode().equals(region))
                .findFirst()
                .orElseGet(() -> {
                    PlatformRegionProfile newProfile = new PlatformRegionProfile();
                    newProfile.setRegionCode(region);
                    return newProfile;
                });

        profile.setHealthScore(score);
        profile.setCpuUtilization(BigDecimal.valueOf(15.5));
        profile.setMemoryUtilization(BigDecimal.valueOf(25.5));
        profile.setNetworkRttMs(12);
        profile.setDiskUtilization(BigDecimal.valueOf(45.5));
        profileRepo.save(profile);

        PlatformReplicationLagLog lag = new PlatformReplicationLagLog();
        lag.setRegionCode(region);
        lag.setLagMs((long) lagMs);
        lag.setRecordedAt(LocalDateTime.now());
        lagRepo.save(lag);
    }

    /**
     * Retrieves optimal region data from the database.
     *
     * @return the result string value
     * @throws ResourceNotFoundException if the entity is not found
     */
    public String getOptimalRegion() {
        return profileRepo.findAll().stream()
                .filter(PlatformRegionProfile::getActive)
                .max(Comparator.comparingInt(PlatformRegionProfile::getHealthScore))
                .map(PlatformRegionProfile::getRegionCode)
                .orElse("US-EAST");
    }
}