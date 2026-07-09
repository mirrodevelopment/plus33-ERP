/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.tax.service
 * File              : TaxJournalService.java
 * Purpose           : Service interface contract defining the API for Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TaxJournalController
 * Related Service   : TaxJournalService, TaxJournalServiceImpl
 * Related Repository: TaxJournalRepository
 * Related Entity    : TaxJournal
 * Related DTO       : N/A
 * Related Mapper    : TaxJournalMapper
 * Related DB Table  : tax_journals
 * Related REST APIs : N/A
 * Depends On        : Organization Module
 * Used By           : Finance Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Finance Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.finance.tax.service;

import com.plus33.erp.finance.entity.JournalEntry;
import com.plus33.erp.finance.tax.dto.TaxCalculationResult;
import com.plus33.erp.organization.entity.Company;

/**
 * Centralized service for creating tax-related journal entry lines.
 * All modules (CustomerInvoice, SupplierInvoice, etc.) should delegate
 * tax GL posting through this service instead of building tax lines directly.
 */
public interface TaxJournalService {

    /**
     * Appends tax journal entry lines to the given journal entry based on the tax calculation result.
     *
     * @param journalEntry The journal entry to append tax lines to
     * @param company      The company context
     * @param taxResult    The tax calculation result containing component-level details
     * @param isPurchase   true for purchase/AP documents, false for sales/AR documents
     */
    void createTaxJournalLines(JournalEntry journalEntry, Company company, TaxCalculationResult taxResult, boolean isPurchase);
}
