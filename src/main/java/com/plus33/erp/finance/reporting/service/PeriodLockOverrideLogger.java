package com.plus33.erp.finance.reporting.service;

import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.finance.entity.JournalEntry;
import com.plus33.erp.finance.reporting.entity.PeriodLockOverride;
import com.plus33.erp.finance.reporting.repository.PeriodLockOverrideRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class PeriodLockOverrideLogger {

    private final PeriodLockOverrideRepository periodLockOverrideRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void logOverride(Company company, String email, LocalDate originalLockDate, LocalDate transactionDate, JournalEntry journalEntry, String reason) {
        JournalEntry targetJE = (journalEntry != null && journalEntry.getId() != null) ? journalEntry : null;
        PeriodLockOverride override = PeriodLockOverride.builder()
                .company(company)
                .userEmail(email)
                .overrideAt(LocalDateTime.now())
                .originalLockDate(originalLockDate)
                .transactionDate(transactionDate)
                .journalEntry(targetJE)
                .reason(reason)
                .build();
        periodLockOverrideRepository.save(override);
    }
}
