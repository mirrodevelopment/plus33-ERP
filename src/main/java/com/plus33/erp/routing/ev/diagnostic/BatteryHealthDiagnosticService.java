package com.plus33.erp.routing.ev.diagnostic;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class BatteryHealthDiagnosticService {
    @Autowired PlatformEvBatteryHealthLogRepository healthRepo;

    @Transactional
    public PlatformEvBatteryHealthLog diagnoseBattery(Long vehicleId) {
        PlatformEvBatteryHealthLog log = new PlatformEvBatteryHealthLog();
        log.setVehicleId(vehicleId);
        log.setDegradationPercentage(BigDecimal.valueOf(1.80));
        log.setInternalResistanceMohm(BigDecimal.valueOf(120.50));
        log.setCellVoltageVariance(BigDecimal.valueOf(0.012));
        log.setThermalBalanceScore(BigDecimal.valueOf(98.50));
        log.setEstimatedRemainingCycles(1850);
        log.setEstimatedEndOfLife(LocalDateTime.now().plusYears(8));
        log.setHealthScore(BigDecimal.valueOf(97.20));
        log.setDiagnosticVersion("v59.0-BMS-DIAG");
        log.setPredictionConfidence(BigDecimal.valueOf(94.50));
        log.setLoggedAt(LocalDateTime.now());
        return healthRepo.save(log);
    }
}