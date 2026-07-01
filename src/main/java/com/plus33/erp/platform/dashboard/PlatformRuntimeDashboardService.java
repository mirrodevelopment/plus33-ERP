package com.plus33.erp.platform.dashboard;

import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.HashMap;
import java.util.Map;

@Service
public class PlatformRuntimeDashboardService {
    @Autowired PlatformCacheNodeRepository cacheNodeRepo;
    @Autowired PlatformK8sPodStatusRepository podRepo;
    @Autowired PlatformRegionProfileRepository regionRepo;
    @Autowired PlatformCircuitBreakerStatsRepository breakerRepo;

    public Map<String, Object> getDashboardData() {
        Map<String, Object> data = new HashMap<>();
        data.put("totalCacheNodes", cacheNodeRepo.count());
        data.put("totalPods", podRepo.count());
        data.put("totalRegions", regionRepo.count());
        data.put("totalBreakers", breakerRepo.count());
        data.put("status", "HEALTHY");
        return data;
    }
}