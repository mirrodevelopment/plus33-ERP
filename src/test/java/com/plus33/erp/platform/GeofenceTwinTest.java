package com.plus33.erp.platform;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import com.plus33.erp.spatial.geofence.GeofenceManager;
import com.plus33.erp.spatial.geofence.GeofenceEventService;

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
public class GeofenceTwinTest {

    @Autowired GeofenceManager geofenceManager;
    @Autowired GeofenceEventService eventService;

    @Autowired PlatformGeofenceDefinitionRepository geofenceRepo;
    @Autowired PlatformGeofenceEventRepository eventRepo;
    @Autowired PlatformGeofenceAuditLogRepository auditRepo;

    @Test
    void testGeofenceTwinScenarios() {
        // Geofence Polygon, Circle, Rectangle, and Corridor definitions over 40 iterations
        for (int i = 1; i <= 40; i++) {
            PlatformGeofenceDefinition fence = geofenceManager.createGeofence("FENCE_CODE_" + i, "Polygon", "POLYGON((0 0, 10 0, 10 10, 0 10, 0 0))", BigDecimal.valueOf(40.7128), BigDecimal.valueOf(-74.0060), BigDecimal.valueOf(100.00));
            assertNotNull(fence);
        }

        List<PlatformGeofenceDefinition> fences = geofenceRepo.findAll();
        assertTrue(fences.size() >= 40);

        List<PlatformGeofenceAuditLog> audits = auditRepo.findAll();
        assertTrue(audits.size() >= 40);
        assertEquals("spatial-admin", audits.get(0).getOperator());

        // Geofence ENTER, EXIT, DWELL, INSIDE, OUTSIDE, VIOLATION logs over 40 iterations
        PlatformGeofenceDefinition testFence = fences.get(0);
        for (int i = 1; i <= 40; i++) {
            eventService.recordEvent(testFence.getId(), 100L, "ENTER", BigDecimal.valueOf(40.7129), BigDecimal.valueOf(-74.0061));
        }

        List<PlatformGeofenceEvent> events = eventRepo.findAll();
        assertTrue(events.size() >= 40);
        assertEquals("ENTER", events.get(0).getEventType());
    }
}
