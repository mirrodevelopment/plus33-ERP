package com.plus33.erp.platform.resilience;

import com.plus33.erp.platform.entity.PlatformCircuitBreakerStats;
import com.plus33.erp.platform.repository.PlatformCircuitBreakerStatsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class PlatformResilienceEngine {
    @Autowired PlatformCircuitBreakerStatsRepository statsRepo;

    @Transactional
    public void recordTrip(String breakerName, String status, double successRatio) {
        PlatformCircuitBreakerStats stats = statsRepo.findAll().stream()
                .filter(s -> s.getBreakerName().equals(breakerName))
                .findFirst()
                .orElseGet(() -> {
                    PlatformCircuitBreakerStats newStats = new PlatformCircuitBreakerStats();
                    newStats.setBreakerName(breakerName);
                    return newStats;
                });

        stats.setStatus(status);
        stats.setSuccessRatio(BigDecimal.valueOf(successRatio));
        if ("OPEN".equals(status)) {
            stats.setFailuresCount(stats.getFailuresCount() + 1);
            stats.setLastTripTime(LocalDateTime.now());
        }
        statsRepo.save(stats);
    }
}