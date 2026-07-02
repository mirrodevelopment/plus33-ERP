package com.plus33.erp.platform;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import com.plus33.erp.spatial.analytics.DeviancyDetector;

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
public class RouteDeviancyTest {

    @Autowired DeviancyDetector deviancyDetector;

    @Autowired PlatformRouteDeviancyLogRepository deviancyRepo;

    @Test
    void testRouteDeviancyScenarios() {
        // Location-aware route deviancy tracking logs over 40 iterations
        for (int i = 1; i <= 40; i++) {
            deviancyDetector.recordDeviancy(500L, "LINESTRING(0 0, 10 10)", "LINESTRING(0 0, 9 9, 15 15)", BigDecimal.valueOf(500.00 * i));
        }

        List<PlatformRouteDeviancyLog> logs = deviancyRepo.findAll();
        assertTrue(logs.size() >= 40);
        assertEquals("High", logs.get(0).getSeverity());
    }
}
