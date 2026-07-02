package com.plus33.erp.routing.ev.scheduler;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class ChargingStationScheduler {
    @Autowired PlatformEvChargingStationRepository stationRepo;
    @Autowired PlatformEvChargingScheduleRepository scheduleRepo;

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