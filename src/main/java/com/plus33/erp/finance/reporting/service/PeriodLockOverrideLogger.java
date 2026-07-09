/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.reporting.service
 * File              : PeriodLockOverrideLogger.java
 * Purpose           : Component of Finance Module within the PLUS33 Coffee ERP platform
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PeriodLockOverrideLoggerController
 * Related Service   : PeriodLockOverrideLoggerService, PeriodLockOverrideLoggerServiceImpl
 * Related Repository: PeriodLockOverrideRepository
 * Related Entity    : PeriodLockOverrideLogger
 * Related DTO       : N/A
 * Related Mapper    : PeriodLockOverrideLoggerMapper
 * Related DB Table  : period_lock_override_loggers
 * Related REST APIs : N/A
 * Depends On        : Organization Module
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Finance Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
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

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code PeriodLockOverrideLogger}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.reporting.service}</p>
 * <p><b>Layer  :</b> Spring Component: shared utility or infrastructure helper for Finance Module.</p>
 *
 * <p><b>Module Deps      :</b> Organization, Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Component
@RequiredArgsConstructor
public class PeriodLockOverrideLogger {

    private final PeriodLockOverrideRepository periodLockOverrideRepository;

    /**
     * Performs the logOverride operation in this module.
     *
     * @param company the company input value
     * @param email the email input value
     * @param originalLockDate the originalLockDate input value
     * @param transactionDate the transactionDate input value
     * @param journalEntry the journalEntry input value
     * @param reason the reason input value
     */
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