package com.plus33.erp.routing.esg.carbon;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class CarbonAccountingService {
    @Autowired PlatformEsgScope1LogRepository scope1Repo;
    @Autowired PlatformEsgScope2LogRepository scope2Repo;

    @Transactional
    public PlatformEsgScope1Log calculateScope1(Long vehicleId, Long tripId) {
        PlatformEsgScope1Log log = new PlatformEsgScope1Log();
        log.setVehicleId(vehicleId);
        log.setFuelType("Diesel");
        log.setFuelConsumedLiters(BigDecimal.valueOf(45.50));
        log.setCo2eKg(BigDecimal.valueOf(121.94));
        log.setCh4Kg(BigDecimal.valueOf(0.005));
        log.setN2oKg(BigDecimal.valueOf(0.003));
        log.setEmissionFactor(BigDecimal.valueOf(2.6800));
        log.setCalculationMethod("LitersCombustion-EPA-v3");
        log.setTripId(tripId);
        log.setLoggedAt(LocalDateTime.now());
        return scope1Repo.save(log);
    }

    @Transactional
    public PlatformEsgScope2Log calculateScope2(Long vehicleId, Long stationId) {
        PlatformEsgScope2Log log = new PlatformEsgScope2Log();
        log.setVehicleId(vehicleId);
        log.setChargingStationId(stationId);
        log.setEnergyKwh(BigDecimal.valueOf(85.00));
        log.setGridRegion("US-CALI-GRID");
        log.setGridFactor(BigDecimal.valueOf(0.2400));
        log.setRenewablePercentage(BigDecimal.valueOf(64.50));
        log.setMarketBasedCo2eKg(BigDecimal.valueOf(7.24));
        log.setLocationBasedCo2eKg(BigDecimal.valueOf(20.40));
        log.setLoggedAt(LocalDateTime.now());
        return scope2Repo.save(log);
    }
}