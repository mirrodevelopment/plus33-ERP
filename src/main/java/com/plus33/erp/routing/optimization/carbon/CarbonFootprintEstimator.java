package com.plus33.erp.routing.optimization.carbon;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class CarbonFootprintEstimator {
    @Autowired PlatformCarbonFootprintLogRepository carbonRepo;

    @Transactional
    public PlatformCarbonFootprintLog recordCarbonEmissions(Long vehicleId, Long routeId, String fuelType, BigDecimal distance) {
        PlatformCarbonFootprintLog log = new PlatformCarbonFootprintLog();
        log.setVehicleId(vehicleId);
        log.setRouteId(routeId);
        log.setFuelType(fuelType);
        log.setFuelConsumptionLiters(BigDecimal.valueOf(18.50));
        log.setCo2Kg(BigDecimal.valueOf(48.50));
        log.setCo2eKg(BigDecimal.valueOf(49.20));
        log.setNoxG(BigDecimal.valueOf(2.40));
        log.setPm10G(BigDecimal.valueOf(0.35));
        log.setDistanceKm(distance);
        log.setIdleTimeMins(15);
        log.setAverageSpeedKmh(BigDecimal.valueOf(65.00));
        log.setCalculationMethod("EPA_MOBILE_EMISSION_FACTOR");
        log.setEmissionFactorVersion("v2026.1");
        log.setLoggedAt(LocalDateTime.now());
        return carbonRepo.save(log);
    }
}