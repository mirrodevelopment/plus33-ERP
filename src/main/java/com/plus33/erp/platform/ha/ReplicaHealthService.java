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

@Service
public class ReplicaHealthService {
    @Autowired PlatformRegionProfileRepository profileRepo;
    @Autowired PlatformReplicationLagLogRepository lagRepo;

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

    public String getOptimalRegion() {
        return profileRepo.findAll().stream()
                .filter(PlatformRegionProfile::getActive)
                .max(Comparator.comparingInt(PlatformRegionProfile::getHealthScore))
                .map(PlatformRegionProfile::getRegionCode)
                .orElse("US-EAST");
    }
}