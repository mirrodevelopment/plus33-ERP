package com.plus33.erp.routing.ev.energy;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class EvEnergyManagementService {
    @Autowired PlatformEvTelemetryLogRepository telemetryRepo;
    @Autowired PlatformEvEnergyAuditLogRepository auditRepo;

    @Transactional
    public PlatformEvTelemetryLog recordTelemetry(Long vehicleId, String batteryPackId) {
        PlatformEvTelemetryLog log = new PlatformEvTelemetryLog();
        log.setVehicleId(vehicleId);
        log.setBatteryPackId(batteryPackId);
        log.setStateOfChargePct(BigDecimal.valueOf(82.50));
        log.setStateOfHealthPct(BigDecimal.valueOf(98.20));
        log.setBatteryVoltage(BigDecimal.valueOf(398.50));
        log.setBatteryCurrent(BigDecimal.valueOf(42.00));
        log.setBatteryTemperatureC(BigDecimal.valueOf(28.40));
        log.setChargingPowerKw(BigDecimal.valueOf(50.00));
        log.setDischargePowerKw(BigDecimal.valueOf(0.00));
        log.setRegenerativePowerKw(BigDecimal.valueOf(15.20));
        log.setRemainingRangeKm(BigDecimal.valueOf(310.40));
        log.setEnergyConsumedKwh(BigDecimal.valueOf(24.50));
        log.setEnergyRegeneratedKwh(BigDecimal.valueOf(4.20));
        log.setOdometerKm(BigDecimal.valueOf(4580.40));
        log.setGpsLatitude(BigDecimal.valueOf(37.774900));
        log.setGpsLongitude(BigDecimal.valueOf(-122.419400));
        log.setLoggedAt(LocalDateTime.now());
        log = telemetryRepo.save(log);

        PlatformEvEnergyAuditLog audit = new PlatformEvEnergyAuditLog();
        audit.setOptimizationAlgorithm("EvEnergyBalancer-v1.2");
        audit.setEnergyBeforeKwh(BigDecimal.valueOf(82.50));
        audit.setEnergyAfterKwh(BigDecimal.valueOf(98.20));
        audit.setEstimatedCost(BigDecimal.valueOf(18.50));
        audit.setEstimatedSavings(BigDecimal.valueOf(5.40));
        audit.setCarbonOffsetKg(BigDecimal.valueOf(12.40));
        audit.setOperator("sustainability-officer");
        audit.setTraceId("TRACE-ID-EV-ENERGY");
        audit.setExecutionDurationMs(140L);
        auditRepo.save(audit);

        return log;
    }
}