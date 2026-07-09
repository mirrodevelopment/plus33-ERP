/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.service
 * File              : PayrollJournalService.java
 * Purpose           : Service interface contract defining the API for Workforce Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PayrollJournalController
 * Related Service   : PayrollJournalService, PayrollJournalServiceImpl
 * Related Repository: PayrollJournalRepository
 * Related Entity    : PayrollJournal
 * Related DTO       : N/A
 * Related Mapper    : PayrollJournalMapper
 * Related DB Table  : payroll_journals
 * Related REST APIs : N/A
 * Depends On        : Finance Module
 * Used By           : Workforce Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Workforce Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.workforce.service;

import com.plus33.erp.finance.entity.JournalEntry;
import com.plus33.erp.workforce.entity.PayrollRun;

public interface PayrollJournalService {
    JournalEntry postPayrollJournal(PayrollRun payrollRun);
}
