package com.plus33.erp.workforce.service;

import com.plus33.erp.finance.entity.JournalEntry;
import com.plus33.erp.workforce.entity.PayrollRun;

public interface PayrollJournalService {
    JournalEntry postPayrollJournal(PayrollRun payrollRun);
}
