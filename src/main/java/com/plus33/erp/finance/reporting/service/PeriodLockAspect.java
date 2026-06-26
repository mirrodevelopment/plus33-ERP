package com.plus33.erp.finance.reporting.service;

import com.plus33.erp.common.exception.BusinessException;
import com.plus33.erp.finance.entity.JournalEntry;
import com.plus33.erp.finance.entity.Payment;
import com.plus33.erp.finance.entity.SupplierInvoice;
import com.plus33.erp.finance.repository.JournalEntryRepository;
import com.plus33.erp.sales.entity.CustomerInvoice;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Aspect
@Component
@RequiredArgsConstructor
public class PeriodLockAspect {

    private final PeriodLockValidator periodLockValidator;
    private final JournalEntryRepository journalEntryRepository;

    @Before("@annotation(com.plus33.erp.finance.reporting.service.CheckPeriodLock) || execution(* com.plus33.erp.finance.service.*.*(..)) || execution(* com.plus33.erp.sales.service.*.*(..)) || (target(com.plus33.erp.finance.repository.JournalEntryRepository) && (execution(* save(..)) || execution(* saveAndFlush(..))))")
    public void checkPeriodLock(JoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        if (args == null) {
            return;
        }

        for (Object arg : args) {
            if (arg == null) {
                continue;
            }

            if (arg instanceof JournalEntry) {
                JournalEntry je = (JournalEntry) arg;
                if (je.getId() != null) {
                    boolean wasClosing = journalEntryRepository.findById(je.getId())
                            .map(JournalEntry::getClosingEntry)
                            .orElse(false);
                    if (wasClosing) {
                        throw new BusinessException("Closing journal entries are immutable and cannot be modified.");
                    }
                }
                periodLockValidator.verifyNotLocked(je.getCompany(), je.getEntryDate(), je);
            } else if (arg instanceof Payment) {
                Payment p = (Payment) arg;
                periodLockValidator.verifyNotLocked(p.getCompany(), p.getPaymentDate(), p.getJournalEntry());
            } else if (arg instanceof SupplierInvoice) {
                SupplierInvoice si = (SupplierInvoice) arg;
                periodLockValidator.verifyNotLocked(si.getCompany(), si.getInvoiceDate(), null);
            } else if (arg instanceof CustomerInvoice) {
                CustomerInvoice ci = (CustomerInvoice) arg;
                periodLockValidator.verifyNotLocked(ci.getCompany(), ci.getInvoiceDate(), null);
            }
        }
    }
}
