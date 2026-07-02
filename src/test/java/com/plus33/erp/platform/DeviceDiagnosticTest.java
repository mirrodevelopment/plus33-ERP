package com.plus33.erp.platform;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import com.plus33.erp.fleet.diagnostic.DiagnosticCollector;
import com.plus33.erp.fleet.diagnostic.CrashReporter;

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
public class DeviceDiagnosticTest {

    @Autowired DiagnosticCollector diagnosticCollector;
    @Autowired CrashReporter crashReporter;

    @Autowired PlatformDeviceDiagnosticRepository diagnosticRepo;

    @Test
    void testDeviceDiagnosticScenarios() {
        // Device remote diagnostics metrics gathering over 40 iterations
        for (int i = 1; i <= 40; i++) {
            diagnosticCollector.collectDiagnostic(1L, BigDecimal.valueOf(1.50 * i), BigDecimal.valueOf(2.00 * i));
        }

        List<PlatformDeviceDiagnostic> normalReports = diagnosticRepo.findAll();
        assertTrue(normalReports.size() >= 40);
        assertEquals("v1.5.0", normalReports.get(0).getFirmwareVersion());

        // Exception crash logs reports over 40 iterations
        for (int i = 1; i <= 40; i++) {
            crashReporter.reportCrash(1L, "OutOfMemoryError", "Exception in thread \"main\" java.lang.OutOfMemoryError: Java heap space");
        }

        List<PlatformDeviceDiagnostic> allReports = diagnosticRepo.findAll();
        assertTrue(allReports.size() >= 80);
    }
}
