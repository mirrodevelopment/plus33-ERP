package com.plus33.erp.platform;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import com.plus33.erp.logistics.network.LogisticsNetworkService;
import com.plus33.erp.logistics.tracking.TelemetryTrackerService;
import com.plus33.erp.logistics.routing.GeoRoutingEngine;
import com.plus33.erp.logistics.fleet.FleetManager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.math.BigDecimal;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
public class GeoRoutingEngineTest {

    @Autowired LogisticsNetworkService networkService;
    @Autowired TelemetryTrackerService trackingService;
    @Autowired GeoRoutingEngine routingEngine;
    @Autowired FleetManager fleetManager;

    @Autowired PlatformLogisticsNodeRepository nodeRepo;
    @Autowired PlatformShippingLaneRepository laneRepo;
    @Autowired PlatformVehicleRepository vehicleRepo;
    @Autowired PlatformVehicleTelemetryRepository telemetryRepo;
    @Autowired PlatformTransitRouteRepository routeRepo;

    @Test
    void testGeoRoutingEngineScenarios() {
        // Node & Shipping lane connections over 40 iterations
        PlatformLogisticsNode srcNode = networkService.addNode("N_SRC", "factory", BigDecimal.valueOf(40.7128), BigDecimal.valueOf(-74.0060), "NY", "EST", 100);
        assertNotNull(srcNode);

        for (int i = 1; i <= 40; i++) {
            PlatformLogisticsNode destNode = networkService.addNode("N_DEST_" + i, "warehouse", BigDecimal.valueOf(34.0522), BigDecimal.valueOf(-118.2437), "LA", "PST", 50);
            assertNotNull(destNode);

            PlatformShippingLane lane = networkService.addLane(srcNode.getId(), destNode.getId(), BigDecimal.valueOf(4500.00), 3000, "truck");
            assertNotNull(lane);
        }

        List<PlatformLogisticsNode> nodes = nodeRepo.findAll();
        assertTrue(nodes.size() >= 41);

        List<PlatformShippingLane> lanes = laneRepo.findAll();
        assertTrue(lanes.size() >= 40);

        // Fleet management availability & transit projection tracks over 40 iterations
        PlatformVehicle vehicle = fleetManager.registerVehicle("V_TRUCK_001", BigDecimal.valueOf(15000.00));
        assertNotNull(vehicle);

        for (int i = 1; i <= 40; i++) {
            trackingService.track(vehicle.getId(), BigDecimal.valueOf(40.00 + i/100.00), BigDecimal.valueOf(-74.00), BigDecimal.valueOf(85.50));
            routingEngine.planRoute(vehicle.getId(), srcNode.getId(), 2L, "{\"steps\": [\"point-" + i + "\"]}");
        }

        List<PlatformVehicleTelemetry> telemetries = telemetryRepo.findAll();
        assertTrue(telemetries.size() >= 40);

        List<PlatformTransitRoute> routes = routeRepo.findAll();
        assertTrue(routes.size() >= 40);
    }
}
