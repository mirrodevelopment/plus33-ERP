package com.plus33.erp.platform;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import com.plus33.erp.routing.optimization.carbon.CarbonFootprintEstimator;

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
public class CarbonFootprintTest {

    @Autowired CarbonFootprintEstimator carbonEstimator;

    @Autowired PlatformCarbonFootprintLogRepository carbonRepo;

    @Test
    void testCarbonFootprintScenarios() {
        // Vehicle carbon footprint logs registrations over 40 iterations
        for (int i = 1; i <= 40; i++) {
            carbonEstimator.recordCarbonEmissions(1L, 1L, "Diesel", BigDecimal.valueOf(125.50));
        }

        List<PlatformCarbonFootprintLog> logs = carbonRepo.findAll();
        assertTrue(logs.size() >= 40);
        assertEquals("Diesel", logs.get(0).getFuelType());
        assertEquals("v2026.1", logs.get(0).getEmissionFactorVersion());
        assertEquals("EPA_MOBILE_EMISSION_FACTOR", logs.get(0).getCalculationMethod());
    }
}
