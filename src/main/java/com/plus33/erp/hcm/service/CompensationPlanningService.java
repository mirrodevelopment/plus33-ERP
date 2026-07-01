package com.plus33.erp.hcm.service;

import com.plus33.erp.hcm.entity.CompensationHistory;
import com.plus33.erp.hcm.repository.CompensationHistoryRepository;
import com.plus33.erp.hcm.event.HcmEventBus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class CompensationPlanningService {

    private final CompensationHistoryRepository compensationRepository;
    private final HcmEventBus eventBus;

    public CompensationPlanningService(CompensationHistoryRepository compensationRepository, HcmEventBus eventBus) {
        this.compensationRepository = compensationRepository;
        this.eventBus = eventBus;
    }

    @Transactional
    public CompensationHistory reviseSalary(Long employeeId, BigDecimal newSalary, String notes) {
        CompensationHistory history = new CompensationHistory();
        history.setEmployeeId(employeeId);
        history.setEffectiveDate(LocalDate.now());
        history.setBaseSalary(newSalary);
        history.setNotes(notes);
        compensationRepository.save(history);

        eventBus.publish("SalaryChanged", 1L, employeeId, "Salary updated to " + newSalary);
        return history;
    }
}
