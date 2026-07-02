package com.plus33.erp.spatial.analytics;

import com.plus33.erp.platform.entity.*;
import com.plus33.erp.platform.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class DeviancyDetector {
    @Autowired PlatformRouteDeviancyLogRepository deviancyRepo;

    @Transactional
    public PlatformRouteDeviancyLog recordDeviancy(Long routeId, String expected, String actual, BigDecimal dist) {
        PlatformRouteDeviancyLog log = new PlatformRouteDeviancyLog();
        log.setTransitRouteId(routeId);
        log.setExpectedRouteWkt(expected);
        log.setActualRouteWkt(actual);
        log.setDeviationDistance(dist);
        log.setDeviationDurationMinutes(15);
        log.setSeverity("High");
        log.setAutomaticRecovery(true);
        log.setRerouteTriggered(false);
        log.setDetectedAt(LocalDateTime.now());
        return deviancyRepo.save(log);
    }
}