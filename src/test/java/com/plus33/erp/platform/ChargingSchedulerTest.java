package com.plus33.erp.platform;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import com.plus33.erp.routing.ev.scheduler.ChargingStationScheduler;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class ChargingSchedulerTest {

    @Autowired ChargingStationScheduler scheduler;

    @Autowired PlatformEvChargingStationRepository stationRepo;
    @Autowired PlatformEvChargingScheduleRepository scheduleRepo;

    @Test
    void testChargingSchedulerScenarios() {
        // Charging stations and bookings over 40 iterations
        for (int i = 1; i <= 40; i++) {
            scheduler.registerStation("CS_STATION_" + i, "GRID-OPERATOR");
            scheduler.reserveSlot(1L, (long) i);
        }

        List<PlatformEvChargingStation> stations = stationRepo.findAll();
        assertTrue(stations.size() >= 40);
        assertEquals("GRID-OPERATOR", stations.get(0).getOperator());

        List<PlatformEvChargingSchedule> schedules = scheduleRepo.findAll();
        assertTrue(schedules.size() >= 40);
        assertEquals("OffPeak", schedules.get(0).getChargingStrategy());
        assertEquals("BOOKED", schedules.get(0).getStatus());
    }
}
