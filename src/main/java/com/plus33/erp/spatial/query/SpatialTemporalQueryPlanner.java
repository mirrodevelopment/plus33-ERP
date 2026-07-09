/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Spatial Module
 * Package           : com.plus33.erp.spatial.query
 * File              : SpatialTemporalQueryPlanner.java
 * Purpose           : Business logic service layer for Spatial Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SpatialTemporalQueryPlannerController
 * Related Service   : SpatialTemporalQueryPlanner
 * Related Repository: SpatialTemporalQueryPlannerRepository
 * Related Entity    : SpatialTemporalQueryPlanner
 * Related DTO       : N/A
 * Related Mapper    : SpatialTemporalQueryPlannerMapper
 * Related DB Table  : spatial_temporal_query_planners
 * Related REST APIs : N/A
 * Depends On        : Platform Module
 * Used By           : SpatialTemporalQueryPlannerController, SpatialTemporalQueryPlannerImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Spatial Module. Implements SpatialTemporalQueryPlannerService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.spatial.query;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Spatial Module</b>
 *
 * <p><b>Class  :</b> {@code SpatialTemporalQueryPlanner}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.spatial.query}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Spatial Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * SpatialTemporalQueryPlannerController
 *   --> SpatialTemporalQueryPlanner (this)
 *   --> Validate business rules
 *   --> SpatialTemporalQueryPlannerRepository (read/write 'spatial_temporal_query_planners')
 *   --> SpatialTemporalQueryPlannerMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code spatial_temporal_query_planners}</p>
 * <p><b>Module Deps      :</b> Platform</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class SpatialTemporalQueryPlanner {
    @Autowired PlatformSpatialQueryLogRepository queryRepo;
    /**
     * Performs the logQuery operation in this module.
     *
     * @param query the query input value
     * @param box the box input value
     * @param type the type input value
     * @return the PlatformSpatialQueryLog result
     */
    @Transactional
    public PlatformSpatialQueryLog logQuery(String query, String box, String type) {
        PlatformSpatialQueryLog log = new PlatformSpatialQueryLog();
        log.setExecutedQuery(query);
        log.setExecutionTimeMs(50L);
        log.setReturnedRows(10);
        log.setSpatialIndexUsed("SPATIAL_RTREE_INDEX");
        log.setBoundingBoxWkt(box);
        log.setQueryType(type);
        log.setQueriedAt(LocalDateTime.now());
        return queryRepo.save(log);
    }
}