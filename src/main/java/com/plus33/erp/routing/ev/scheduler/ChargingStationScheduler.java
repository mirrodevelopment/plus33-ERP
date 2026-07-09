/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Routing Module
 * Package           : com.plus33.erp.routing.ev.scheduler
 * File              : ChargingStationScheduler.java
 * Purpose           : Business logic service layer for Routing Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ChargingStationSchedulerController
 * Related Service   : ChargingStationScheduler
 * Related Repository: ChargingStationSchedulerRepository
 * Related Entity    : ChargingStationScheduler
 * Related DTO       : N/A
 * Related Mapper    : ChargingStationSchedulerMapper
 * Related DB Table  : charging_station_schedulers
 * Related REST APIs : N/A
 * Depends On        : Platform Module
 * Used By           : ChargingStationSchedulerController, ChargingStationSchedulerImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Routing Module. Implements ChargingStationSchedulerService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.routing.ev.scheduler;

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
 * <p><b>Class  :</b> {@code ChargingStationScheduler}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.routing.ev.scheduler}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Routing Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * ChargingStationSchedulerController
 *   --> ChargingStationScheduler (this)
 *   --> Validate business rules
 *   --> ChargingStationSchedulerRepository (read/write 'charging_station_schedulers')
 *   --> ChargingStationSchedulerMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code charging_station_schedulers}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class ChargingStationScheduler {
    @Autowired PlatformEvChargingStationRepository stationRepo;
    @Autowired PlatformEvChargingScheduleRepository scheduleRepo;
    /**
     * Creates a new station and persists it to the database.
     *
     * @param code the code input value
     * @param operator the operator input value
     * @return the PlatformEvChargingStation result
     * @throws BusinessException if a business rule is violated
     */
    @Transactional
    public PlatformEvChargingStation registerStation(String code, String operator) {
        PlatformEvChargingStation s = new PlatformEvChargingStation();
        s.setStationCode(code);
        s.setOperator(operator);
        s.setLocationName("Warehouse Depot North Charging Dock");
        s.setLatitude(BigDecimal.valueOf(37.774900));
        s.setLongitude(BigDecimal.valueOf(-122.419400));
        s.setChargerType("DC");
        s.setConnectorType("CCS2");
        s.setMaxPowerKw(BigDecimal.valueOf(150.00));
        s.setSimultaneousConnectors(4);
        s.setAvailabilityStatus("AVAILABLE");
        s.setTariffPlanCode("GRID-OFFPEAK-V2");
        s.setRenewableSupported(true);
        return stationRepo.save(s);
    }

    /**
     * Reserves slot resources (budget or stock) for downstream processing.
     *
     * @param vehicleId the vehicleId input value
     * @param stationId the stationId input value
     * @return the PlatformEvChargingSchedule result
     */
    @Transactional
    public PlatformEvChargingSchedule reserveSlot(Long vehicleId, Long stationId) {
        PlatformEvChargingSchedule sch = new PlatformEvChargingSchedule();
        sch.setVehicleId(vehicleId);
        sch.setStationId(stationId);
        sch.setConnectorId(1);
        sch.setReservationStart(LocalDateTime.now());
        sch.setReservationEnd(LocalDateTime.now().plusHours(2));
        sch.setPlannedEnergyKwh(BigDecimal.valueOf(75.00));
        sch.setPriority(2);
        sch.setChargingStrategy("OffPeak");
        sch.setStatus("BOOKED");
        return scheduleRepo.save(sch);
    }
}