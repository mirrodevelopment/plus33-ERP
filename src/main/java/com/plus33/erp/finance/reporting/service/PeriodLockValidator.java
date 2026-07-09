/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.reporting.service
 * File              : PeriodLockValidator.java
 * Purpose           : Component of Finance Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PeriodLockValidatorController
 * Related Service   : PeriodLockValidatorService, PeriodLockValidatorServiceImpl
 * Related Repository: PeriodLockRepository
 * Related Entity    : PeriodLockValidator
 * Related DTO       : N/A
 * Related Mapper    : PeriodLockValidatorMapper
 * Related DB Table  : period_lock_validators
 * Related REST APIs : N/A
 * Depends On        : Common Module, Organization Module
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Finance Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
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

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code PeriodLockValidator}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.reporting.service}</p>
 * <p><b>Layer  :</b> Spring Component: shared utility or infrastructure helper for Finance Module.</p>
 *
 * <p><b>Module Deps      :</b> Common, Finance, Organization</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Component
@RequiredArgsConstructor
public class PeriodLockValidator {

    private final PeriodLockRepository periodLockRepository;
    private final PeriodLockOverrideLogger periodLockOverrideLogger;

    /**
     * Validates business rules and constraints for not locked.
     *
     * @param company the company input value
     * @param date the date input value
     * @param journalEntry the journalEntry input value
     * @throws BusinessException if a business rule is violated
     */
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