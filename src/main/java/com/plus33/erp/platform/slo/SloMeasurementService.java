package com.plus33.erp.platform.slo;

import com.plus33.erp.platform.entity.PlatformSlo;
import com.plus33.erp.platform.entity.PlatformSloMeasurement;
import com.plus33.erp.platform.repository.PlatformSloMeasurementRepository;
import com.plus33.erp.platform.repository.PlatformSloRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Service
public class SloMeasurementService {
    @Autowired PlatformSloRepository sloRepo;
    @Autowired PlatformSloMeasurementRepository measurementRepo;

    @Transactional
    public void createSlo(String name, double target, String service) {
        PlatformSlo slo = new PlatformSlo();
        slo.setName(name);
        slo.setTargetPercentage(BigDecimal.valueOf(target));
        slo.setServiceName(service);
        sloRepo.save(slo);
    }

    @Transactional
    public void recordMeasurement(String name, double current, double budget) {
        PlatformSlo slo = sloRepo.findAll().stream()
                .filter(s -> s.getName().equals(name))
                .findFirst().orElseThrow(() -> new IllegalArgumentException("SLO not found"));

        PlatformSloMeasurement m = new PlatformSloMeasurement();
        m.setSloId(slo.getId());
        m.setCurrentPercentage(BigDecimal.valueOf(current));
        m.setErrorBudget(BigDecimal.valueOf(budget));
        m.setRecordedAt(LocalDateTime.now());
        measurementRepo.save(m);
    }
}