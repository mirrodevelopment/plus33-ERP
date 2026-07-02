package com.plus33.erp.platform;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import com.plus33.erp.iot.telemetry.MachineryTelemetryService;
import com.plus33.erp.iot.alarm.AlarmOrchestrator;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
public class MachineryTelemetryTest {

    @Autowired MachineryTelemetryService telemetryService;
    @Autowired AlarmOrchestrator alarmOrchestrator;

    @Autowired PlatformMachineryTelemetryRepository telemetryRepo;
    @Autowired PlatformScadaAlarmPolicyRepository policyRepo;
    @Autowired PlatformScadaAlarmEventRepository eventRepo;

    @Test
    void testMachineryTelemetryScenarios() {
        // High-frequency telemetry ingestion over 40 iterations
        for (int i = 1; i <= 40; i++) {
            telemetryService.ingest(1L, (long) i, BigDecimal.valueOf(25.4 + i), "CELSIUS", i);
        }

        List<PlatformMachineryTelemetry> telemetries = telemetryRepo.findAll();
        assertTrue(telemetries.size() >= 40);
        assertEquals("GOOD", telemetries.get(0).getQuality());

        // Alarm policies and triggers over 40 iterations
        PlatformScadaAlarmPolicy policy = alarmOrchestrator.createPolicy("ALARM_TEMP_HIGH", "Critical", BigDecimal.valueOf(100.00));
        assertNotNull(policy);

        for (int i = 1; i <= 40; i++) {
            alarmOrchestrator.triggerAlarm(1L, policy.getId(), "Critical Temperature threshold violation " + i);
        }

        List<PlatformScadaAlarmEvent> events = eventRepo.findAll();
        assertTrue(events.size() >= 40);
        assertEquals("ACTIVE", events.get(0).getAlarmStatus());
    }
}
