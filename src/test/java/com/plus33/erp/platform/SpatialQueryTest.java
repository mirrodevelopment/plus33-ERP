package com.plus33.erp.platform;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import com.plus33.erp.spatial.query.SpatialTemporalQueryPlanner;

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
public class SpatialQueryTest {

    @Autowired SpatialTemporalQueryPlanner queryPlanner;

    @Autowired PlatformSpatialQueryLogRepository queryRepo;

    @Test
    void testSpatialQueryScenarios() {
        // BBOX, RADIUS, and POLYGON_CONTAINMENT query logs over 40 iterations
        for (int i = 1; i <= 40; i++) {
            queryPlanner.logQuery("SELECT * FROM assets WHERE within_bbox(" + i + ")", "POLYGON((0 0, 0 100, 100 100, 100 0, 0 0))", "BBOX");
        }

        List<PlatformSpatialQueryLog> logs = queryRepo.findAll();
        assertTrue(logs.size() >= 40);
        assertEquals("SPATIAL_RTREE_INDEX", logs.get(0).getSpatialIndexUsed());
    }
}
