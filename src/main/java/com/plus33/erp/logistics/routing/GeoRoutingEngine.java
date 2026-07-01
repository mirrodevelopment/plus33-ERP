package com.plus33.erp.logistics.routing;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GeoRoutingEngine {
    @Autowired PlatformTransitRouteRepository routeRepo;

    @Transactional
    public PlatformTransitRoute planRoute(Long vehicleId, Long origin, Long dest, String path) {
        PlatformTransitRoute r = new PlatformTransitRoute();
        r.setVehicleId(vehicleId);
        r.setOriginNodeId(origin);
        r.setDestNodeId(dest);
        r.setRoutePathJson(path);
        r.setStatus("IN_TRANSIT");
        return routeRepo.save(r);
    }
}