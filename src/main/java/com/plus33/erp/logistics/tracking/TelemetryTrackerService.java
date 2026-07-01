package com.plus33.erp.logistics.tracking;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class TelemetryTrackerService {
    @Autowired PlatformVehicleTelemetryRepository telemetryRepo;

    @Transactional
    public PlatformVehicleTelemetry track(Long vehicleId, BigDecimal lat, BigDecimal lon, BigDecimal speed) {
        PlatformVehicleTelemetry t = new PlatformVehicleTelemetry();
        t.setVehicleId(vehicleId);
        t.setLatitude(lat);
        t.setLongitude(lon);
        t.setSpeedKmh(speed);
        t.setRecordedAt(LocalDateTime.now());
        return telemetryRepo.save(t);
    }
}