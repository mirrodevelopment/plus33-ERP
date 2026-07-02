package com.plus33.erp.routing.fuel.telemetry;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class FuelTelemetryService {
    @Autowired PlatformFuelTelemetryLogRepository telemetryRepo;

    @Transactional
    public PlatformFuelTelemetryLog recordTelemetry(Long vehicleId, Long gatewayId) {
        PlatformFuelTelemetryLog log = new PlatformFuelTelemetryLog();
        log.setVehicleId(vehicleId);
        log.setGatewayId(gatewayId);
        log.setEngineRpm(BigDecimal.valueOf(1450.00));
        log.setThrottlePositionPct(BigDecimal.valueOf(45.20));
        log.setInstantaneousFuelRate(BigDecimal.valueOf(22.40));
        log.setAverageFuelRate(BigDecimal.valueOf(24.50));
        log.setFuelLevelPct(BigDecimal.valueOf(88.00));
        log.setCoolantTemperatureC(BigDecimal.valueOf(85.00));
        log.setEngineLoadPct(BigDecimal.valueOf(62.00));
        log.setOdometerKm(BigDecimal.valueOf(12580.40));
        log.setTripDistanceKm(BigDecimal.valueOf(145.50));
        log.setIdleTimeSeconds(180);
        log.setGpsLatitude(BigDecimal.valueOf(37.774900));
        log.setGpsLongitude(BigDecimal.valueOf(-122.419400));
        log.setLoggedAt(LocalDateTime.now());
        return telemetryRepo.save(log);
    }
}