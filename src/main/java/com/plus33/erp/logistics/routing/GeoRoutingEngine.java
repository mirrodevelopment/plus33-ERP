/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Logistics Module
 * Package           : com.plus33.erp.logistics.routing
 * File              : GeoRoutingEngine.java
 * Purpose           : Business logic service layer for Logistics Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: GeoRoutingEngineController
 * Related Service   : GeoRoutingEngine
 * Related Repository: GeoRoutingEngineRepository
 * Related Entity    : GeoRoutingEngine
 * Related DTO       : N/A
 * Related Mapper    : GeoRoutingEngineMapper
 * Related DB Table  : geo_routing_engines
 * Related REST APIs : N/A
 * Depends On        : Platform Module
 * Used By           : GeoRoutingEngineController, GeoRoutingEngineImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Logistics Module. Implements GeoRoutingEngineService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.logistics.routing;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <b>PLUS33 Coffee ERP -- Logistics Module</b>
 *
 * <p><b>Class  :</b> {@code GeoRoutingEngine}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.logistics.routing}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Logistics Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * GeoRoutingEngineController
 *   --> GeoRoutingEngine (this)
 *   --> Validate business rules
 *   --> GeoRoutingEngineRepository (read/write 'geo_routing_engines')
 *   --> GeoRoutingEngineMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code geo_routing_engines}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class GeoRoutingEngine {
    @Autowired PlatformTransitRouteRepository routeRepo;
    /**
     * Performs the planRoute operation in this module.
     *
     * @param vehicleId the vehicleId input value
     * @param origin the origin input value
     * @param dest the dest input value
     * @param path the path input value
     * @return the PlatformTransitRoute result
     */
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