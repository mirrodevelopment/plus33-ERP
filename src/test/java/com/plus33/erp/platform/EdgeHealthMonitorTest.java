package com.plus33.erp.platform;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import com.plus33.erp.edge.monitoring.EdgeHealthMonitor;

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
public class EdgeHealthMonitorTest {

    @Autowired EdgeHealthMonitor healthMonitor;

    @Autowired PlatformEdgeHealthMetricRepository healthRepo;

    @Test
    void testEdgeHealthMonitorScenarios() {
        // Edge node CPU/memory health metrics records over 40 iterations
        for (int i = 1; i <= 40; i++) {
            healthMonitor.recordMetrics(1L, BigDecimal.valueOf(1.25 * i), BigDecimal.valueOf(2.00 * i), BigDecimal.valueOf(0.5 * i));
        }

        List<PlatformEdgeHealthMetric> metrics = healthRepo.findAll();
        assertTrue(metrics.size() >= 40);
        assertEquals(15, metrics.get(0).getNetworkLatencyMs());
    }
}
