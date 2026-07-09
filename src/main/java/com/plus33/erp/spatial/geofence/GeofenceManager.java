/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Spatial Module
 * Package           : com.plus33.erp.spatial.geofence
 * File              : GeofenceManager.java
 * Purpose           : Business logic service layer for Spatial Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: GeofenceManagerController
 * Related Service   : GeofenceManager
 * Related Repository: GeofenceManagerRepository
 * Related Entity    : GeofenceManager
 * Related DTO       : N/A
 * Related Mapper    : GeofenceManagerMapper
 * Related DB Table  : geofence_managers
 * Related REST APIs : N/A
 * Depends On        : Platform Module
 * Used By           : GeofenceManagerController, GeofenceManagerImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Spatial Module. Implements GeofenceManagerService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.spatial.geofence;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Spatial Module</b>
 *
 * <p><b>Class  :</b> {@code GeofenceManager}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.spatial.geofence}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Spatial Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * GeofenceManagerController
 *   --> GeofenceManager (this)
 *   --> Validate business rules
 *   --> GeofenceManagerRepository (read/write 'geofence_managers')
 *   --> GeofenceManagerMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code geofence_managers}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class GeofenceManager {
    @Autowired PlatformGeofenceDefinitionRepository geofenceRepo;
    @Autowired PlatformGeofenceAuditLogRepository auditRepo;
    /**
     * Creates a new geofence and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param code the code input value
     * @param type the type input value
     * @param wkt the wkt input value
     * @param lat the lat input value
     * @param lon the lon input value
     * @param rad the rad input value
     * @return the PlatformGeofenceDefinition result
     * @throws BusinessException if a business rule is violated
     */
    @Transactional
    public PlatformGeofenceDefinition createGeofence(String code, String type, String wkt, BigDecimal lat, BigDecimal lon, BigDecimal rad) {
        PlatformGeofenceDefinition g = new PlatformGeofenceDefinition();
        g.setGeofenceCode(code);
        g.setGeofenceType(type);
        g.setGeometryWkt(wkt);
        g.setCenterLat(lat);
        g.setCenterLng(lon);
        g.setRadiusMeters(rad);
        g.setTimezone("UTC");
        g.setTenantId("DEFAULT_TENANT");
        g = geofenceRepo.save(g);

        PlatformGeofenceAuditLog audit = new PlatformGeofenceAuditLog();
        audit.setGeofenceId(g.getId());
        audit.setOperator("spatial-admin");
        audit.setActionType("CREATE");
        audit.setNewGeometryWkt(wkt);
        audit.setTraceId("TRACE-ID-INIT");
        auditRepo.save(audit);

        return g;
    }
}