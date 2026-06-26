package com.plus33.erp.finance.reporting.service;

import com.plus33.erp.common.exception.BusinessException;
import com.plus33.erp.finance.entity.JournalEntry;
import com.plus33.erp.finance.reporting.entity.PeriodLock;
import com.plus33.erp.finance.reporting.entity.PeriodLockType;
import com.plus33.erp.finance.reporting.repository.PeriodLockRepository;
import com.plus33.erp.organization.entity.Company;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
@RequiredArgsConstructor
public class PeriodLockValidator {

    private final PeriodLockRepository periodLockRepository;
    private final PeriodLockOverrideLogger periodLockOverrideLogger;

    public void verifyNotLocked(Company company, LocalDate date, JournalEntry journalEntry) {
        if (company == null || date == null) {
            return;
        }
        
        if (journalEntry != null && Boolean.TRUE.equals(journalEntry.getClosingEntry())) {
            return;
        }
        
        periodLockRepository.findByCompanyId(company.getId()).ifPresent(lock -> {
            if (!date.isAfter(lock.getLockDate())) {
                if (lock.getLockType() == PeriodLockType.SOFT) {
                    // Soft Lock: check if current user is a Finance Manager or Ultimate Admin
                    if (hasAnyAuthority("FINANCE_MANAGER", "ULTIMATE_ADMIN")) {
                        String email = getCurrentUserEmail();
                        periodLockOverrideLogger.logOverride(
                            company, 
                            email, 
                            lock.getLockDate(), 
                            date, 
                            journalEntry, 
                            "Soft lock override posting on date: " + date
                        );
                        return; // Allowed
                    }
                }
                throw new BusinessException("Accounting period is locked for date: " + date + 
                    " (Locked up to: " + lock.getLockDate() + " with " + lock.getLockType() + " lock)");
            }
        });
    }

    private boolean hasAnyAuthority(String... authorities) {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return true; // Allow overrides in non-authenticated contexts (e.g. system tasks, tests)
        }
        return auth.getAuthorities().stream()
                .anyMatch(a -> java.util.Arrays.asList(authorities).contains(a.getAuthority()));
    }

    private String getCurrentUserEmail() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            return "system@plus33.com";
        }
        return auth.getName();
    }
}
