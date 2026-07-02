package com.plus33.erp.platform;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import com.plus33.erp.iot.control.ScadaControlService;

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
public class ScadaControlTest {

    @Autowired ScadaControlService controlService;

    @Autowired PlatformScadaWriteCommandRepository commandRepo;
    @Autowired PlatformScadaAuditTrailRepository auditRepo;

    @Test
    void testScadaControlScenarios() {
        // Secure SCADA write executions and audit logs over 40 iterations
        for (int i = 1; i <= 40; i++) {
            controlService.executeWrite(1L, (long) i, BigDecimal.valueOf(10.00 * i), "operator-sys-admin", "CRYPTOGRAPHIC-SIG-SHA256-" + i);
        }

        List<PlatformScadaWriteCommand> commands = commandRepo.findAll();
        assertTrue(commands.size() >= 40);
        assertTrue(commands.get(0).getRollbackSupported());

        List<PlatformScadaAuditTrail> audits = auditRepo.findAll();
        assertTrue(audits.size() >= 40);
        assertEquals("VERIFIED", audits.get(0).getStatus());
    }
}
