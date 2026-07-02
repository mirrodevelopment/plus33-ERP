package com.plus33.erp.spatial.query;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
public class SpatialTemporalQueryPlanner {
    @Autowired PlatformSpatialQueryLogRepository queryRepo;

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