package com.plus33.erp.spatial.geofence;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class GeofenceManager {
    @Autowired PlatformGeofenceDefinitionRepository geofenceRepo;
    @Autowired PlatformGeofenceAuditLogRepository auditRepo;

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