package com.plus33.erp.finance.tax.service;

import com.plus33.erp.common.exception.BusinessException;
import com.plus33.erp.finance.entity.Account;
import com.plus33.erp.finance.entity.JournalEntry;
import com.plus33.erp.finance.entity.JournalEntryLine;
import com.plus33.erp.finance.repository.AccountRepository;
import com.plus33.erp.finance.tax.dto.TaxCalculationLineResult;
import com.plus33.erp.finance.tax.dto.TaxCalculationResult;
import com.plus33.erp.finance.tax.dto.TaxComponentResult;
import com.plus33.erp.organization.entity.Company;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * Centralized service for creating tax-related journal entry lines.
 * Replaces the inline tax posting logic previously duplicated across
 * CustomerInvoiceServiceImpl and SupplierInvoiceServiceImpl.
 */
@Service
@RequiredArgsConstructor
public class TaxJournalServiceImpl implements TaxJournalService {

    private final AccountRepository accountRepository;

    @Override
    public void createTaxJournalLines(JournalEntry journalEntry, Company company,
                                       TaxCalculationResult taxResult, boolean isPurchase) {
        if (taxResult == null) {
            return;
        }

        if (isPurchase) {
            createPurchaseTaxLines(journalEntry, company, taxResult);
        } else {
            createSalesTaxLines(journalEntry, company, taxResult);
        }
    }

    /**
     * For sales documents: Credit tax to output tax accounts.
     */
    private void createSalesTaxLines(JournalEntry journalEntry, Company company, TaxCalculationResult taxResult) {
        Map<Long, BigDecimal> taxPostings = new HashMap<>();

        for (TaxCalculationLineResult lineRes : taxResult.getLines()) {
            for (TaxComponentResult comp : lineRes.getTaxComponents()) {
                if (comp.getTaxAmount().compareTo(BigDecimal.ZERO) > 0) {
                    Long accountId = comp.getOutputTaxAccountId();
                    if (accountId == null) {
                        Account fallbackTaxAcc = accountRepository.findByCompanyIdAndAccountCode(company.getId(), "2200")
                                .orElseThrow(() -> new BusinessException("Tax Payable account (2200) not found"));
                        accountId = fallbackTaxAcc.getId();
                    }
                    taxPostings.merge(accountId, comp.getTaxAmount(), BigDecimal::add);
                }
            }
        }

        for (Map.Entry<Long, BigDecimal> entry : taxPostings.entrySet()) {
            Account taxAcc = accountRepository.findById(entry.getKey())
                    .orElseThrow(() -> new BusinessException("Tax account not found for ID: " + entry.getKey()));
            JournalEntryLine taxLine = JournalEntryLine.builder()
                    .journalEntry(journalEntry)
                    .account(taxAcc)
                    .debitAmount(BigDecimal.ZERO)
                    .creditAmount(entry.getValue())
                    .build();
            journalEntry.getLines().add(taxLine);
        }
    }

    /**
     * For purchase documents: Debit recoverable tax, handle reverse charge (debit + credit offset),
     * and capitalize non-recoverable tax into expense.
     */
    private void createPurchaseTaxLines(JournalEntry journalEntry, Company company, TaxCalculationResult taxResult) {
        Map<Long, BigDecimal> taxDebits = new HashMap<>();
        Map<Long, BigDecimal> taxCredits = new HashMap<>();

        for (TaxCalculationLineResult lineRes : taxResult.getLines()) {
            for (TaxComponentResult comp : lineRes.getTaxComponents()) {
                BigDecimal compTax = comp.getTaxAmount();
                if (compTax.compareTo(BigDecimal.ZERO) > 0) {
                    if (comp.getReverseChargeAccountId() != null) {
                        // Reverse charge: debit recoverable, credit reverse charge account
                        Long debitAccId = comp.getRecoverableAccountId() != null
                                ? comp.getRecoverableAccountId()
                                : comp.getInputTaxAccountId();
                        if (debitAccId == null) {
                            Account fallback = accountRepository.findByCompanyIdAndAccountCode(company.getId(), "2200")
                                    .orElseThrow(() -> new BusinessException("Tax Payable account (2200) not found"));
                            debitAccId = fallback.getId();
                        }
                        Long creditAccId = comp.getReverseChargeAccountId();
                        taxDebits.merge(debitAccId, compTax, BigDecimal::add);
                        taxCredits.merge(creditAccId, compTax, BigDecimal::add);
                    } else if (comp.isRecoverable()) {
                        // Recoverable tax: debit to recoverable account
                        Long debitAccId = comp.getRecoverableAccountId() != null
                                ? comp.getRecoverableAccountId()
                                : comp.getInputTaxAccountId();
                        if (debitAccId == null) {
                            Account fallback = accountRepository.findByCompanyIdAndAccountCode(company.getId(), "2200")
                                    .orElseThrow(() -> new BusinessException("Tax Payable account (2200) not found"));
                            debitAccId = fallback.getId();
                        }
                        taxDebits.merge(debitAccId, compTax, BigDecimal::add);
                    }
                    // Non-recoverable tax is NOT posted here — it's capitalized into the item's debit
                    // by the calling service (e.g., SupplierInvoiceServiceImpl adds it to itemDebit).
                }
            }
        }

        // Add tax debit lines
        for (Map.Entry<Long, BigDecimal> entry : taxDebits.entrySet()) {
            Account taxAcc = accountRepository.findById(entry.getKey())
                    .orElseThrow(() -> new BusinessException("Tax account not found for ID: " + entry.getKey()));
            JournalEntryLine taxLine = JournalEntryLine.builder()
                    .journalEntry(journalEntry)
                    .account(taxAcc)
                    .debitAmount(entry.getValue())
                    .creditAmount(BigDecimal.ZERO)
                    .build();
            journalEntry.getLines().add(taxLine);
        }

        // Add tax credit lines (e.g. reverse charge)
        for (Map.Entry<Long, BigDecimal> entry : taxCredits.entrySet()) {
            Account taxAcc = accountRepository.findById(entry.getKey())
                    .orElseThrow(() -> new BusinessException("Tax account not found for ID: " + entry.getKey()));
            JournalEntryLine taxLine = JournalEntryLine.builder()
                    .journalEntry(journalEntry)
                    .account(taxAcc)
                    .debitAmount(BigDecimal.ZERO)
                    .creditAmount(entry.getValue())
                    .build();
            journalEntry.getLines().add(taxLine);
        }
    }
}
