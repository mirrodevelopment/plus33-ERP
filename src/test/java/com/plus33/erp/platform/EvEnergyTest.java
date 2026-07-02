package com.plus33.erp.platform;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import com.plus33.erp.routing.ev.energy.EvEnergyManagementService;

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
public class EvEnergyTest {

    @Autowired EvEnergyManagementService energyService;

    @Autowired PlatformEvTelemetryLogRepository telemetryRepo;
    @Autowired PlatformEvEnergyAuditLogRepository auditRepo;

    @Test
    void testEvEnergyScenarios() {
        // EV battery telemetry and audits over 40 iterations
        for (int i = 1; i <= 40; i++) {
            energyService.recordTelemetry((long) i, "BATT_PACK_" + i);
        }

        List<PlatformEvTelemetryLog> logs = telemetryRepo.findAll();
        assertTrue(logs.size() >= 40);
        assertEquals("BATT_PACK_1", logs.get(0).getBatteryPackId());

        List<PlatformEvEnergyAuditLog> audits = auditRepo.findAll();
        assertTrue(audits.size() >= 40);
        assertEquals("EvEnergyBalancer-v1.2", audits.get(0).getOptimizationAlgorithm());
    }
}
