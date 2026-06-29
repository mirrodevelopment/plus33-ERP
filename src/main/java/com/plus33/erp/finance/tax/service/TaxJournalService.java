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
