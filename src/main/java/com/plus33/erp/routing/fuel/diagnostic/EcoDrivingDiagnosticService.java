package com.plus33.erp.routing.fuel.diagnostic;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class EcoDrivingDiagnosticService {
    @Autowired PlatformEcoDrivingLogRepository diagnosticRepo;

    @Transactional
    public PlatformEcoDrivingLog logEcoDrivingMetrics(Long driverId, Long tripId) {
        PlatformEcoDrivingLog log = new PlatformEcoDrivingLog();
        log.setDriverId(driverId);
        log.setTripId(tripId);
        log.setHarshAccelerationCount(2);
        log.setHarshBrakingCount(1);
        log.setHarshCorneringCount(0);
        log.setExcessiveIdleSeconds(120);
        log.setOverspeedDurationSecs(45);
        log.setCruiseControlUsagePct(BigDecimal.valueOf(74.50));
        log.setDriverScore(BigDecimal.valueOf(92.50));
        log.setTripScore(BigDecimal.valueOf(94.00));
        log.setCoachingStatus("OK");
        log.setLoggedAt(LocalDateTime.now());
        return diagnosticRepo.save(log);
    }
}