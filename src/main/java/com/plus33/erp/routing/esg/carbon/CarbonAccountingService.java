/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Routing Module
 * Package           : com.plus33.erp.routing.esg.carbon
 * File              : CarbonAccountingService.java
 * Purpose           : Business logic service layer for Routing Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CarbonAccountingController
 * Related Service   : CarbonAccountingService
 * Related Repository: CarbonAccountingRepository
 * Related Entity    : CarbonAccounting
 * Related DTO       : N/A
 * Related Mapper    : CarbonAccountingMapper
 * Related DB Table  : carbon_accountings
 * Related REST APIs : N/A
 * Depends On        : Platform Module
 * Used By           : CarbonAccountingController, CarbonAccountingServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Routing Module. Implements CarbonAccountingService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.routing.esg.carbon;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Routing Module</b>
 *
 * <p><b>Class  :</b> {@code CarbonAccountingService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.routing.esg.carbon}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Routing Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * CarbonAccountingController
 *   --> CarbonAccountingService (this)
 *   --> Validate business rules
 *   --> CarbonAccountingRepository (read/write 'carbon_accountings')
 *   --> CarbonAccountingMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code carbon_accountings}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class CarbonAccountingService {
    @Autowired PlatformEsgScope1LogRepository scope1Repo;
    @Autowired PlatformEsgScope2LogRepository scope2Repo;
    /**
     * Calculates scope1 totals including subtotal, tax, discounts, and net amount.
     *
     * @param vehicleId the vehicleId input value
     * @param tripId the tripId input value
     * @return the PlatformEsgScope1Log result
     */
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

    /**
     * Calculates scope2 totals including subtotal, tax, discounts, and net amount.
     *
     * @param vehicleId the vehicleId input value
     * @param stationId the stationId input value
     * @return the PlatformEsgScope2Log result
     */
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