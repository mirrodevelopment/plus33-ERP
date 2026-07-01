package com.plus33.erp.hcm.service;

import com.plus33.erp.hcm.entity.EmployeeLifecycle;
import com.plus33.erp.hcm.repository.EmployeeLifecycleRepository;
import com.plus33.erp.hcm.event.HcmEventBus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class EmployeeLifecycleService {

    private final EmployeeLifecycleRepository lifecycleRepository;
    private final HcmEventBus eventBus;

    public EmployeeLifecycleService(EmployeeLifecycleRepository lifecycleRepository, HcmEventBus eventBus) {
        this.lifecycleRepository = lifecycleRepository;
        this.eventBus = eventBus;
    }

    @Transactional
    public EmployeeLifecycle initializeLifecycle(Long employeeId) {
        EmployeeLifecycle lc = new EmployeeLifecycle();
        lc.setEmployeeId(employeeId);
        lc.setLifecycleStatus("CANDIDATE");
        lifecycleRepository.save(lc);
        return lc;
    }

    @Transactional
    public void transitionStatus(Long employeeId, String targetStatus) {
        EmployeeLifecycle lc = lifecycleRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Lifecycle profile not found"));

        lc.setLifecycleStatus(targetStatus);
        lc.setUpdatedAt(LocalDateTime.now());
        lifecycleRepository.save(lc);

        if ("CONFIRMED".equals(targetStatus)) {
            eventBus.publish("EmployeeConfirmed", 1L, employeeId, "Employee status confirmed");
        } else if ("SEPARATED".equals(targetStatus)) {
            eventBus.publish("EmployeeSeparated", 1L, employeeId, "Employee separated");
        }
    }
}
