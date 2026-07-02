package com.plus33.erp.spatial.geofence;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class GeofenceEventService {
    @Autowired PlatformGeofenceEventRepository eventRepo;

    @Transactional
    public PlatformGeofenceEvent recordEvent(Long geofenceId, Long assetId, String type, BigDecimal lat, BigDecimal lon) {
        PlatformGeofenceEvent e = new PlatformGeofenceEvent();
        e.setGeofenceId(geofenceId);
        e.setAssetId(assetId);
        e.setEventType(type);
        e.setLatitude(lat);
        e.setLongitude(lon);
        e.setSpeedKmh(BigDecimal.valueOf(65.50));
        e.setHeadingDegrees(BigDecimal.valueOf(180.00));
        e.setGpsAccuracyMeters(BigDecimal.valueOf(2.50));
        e.setRecordedAt(LocalDateTime.now());
        return eventRepo.save(e);
    }
}