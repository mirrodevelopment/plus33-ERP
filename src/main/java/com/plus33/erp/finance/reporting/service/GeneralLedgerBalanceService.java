/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.reporting.service
 * File              : GeneralLedgerBalanceService.java
 * Purpose           : Business logic service layer for Finance Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: GeneralLedgerBalanceController
 * Related Service   : GeneralLedgerBalanceService
 * Related Repository: ExchangeRateRepository
 * Related Entity    : GeneralLedgerBalance
 * Related DTO       : N/A
 * Related Mapper    : GeneralLedgerBalanceMapper
 * Related DB Table  : general_ledger_balances
 * Related REST APIs : N/A
 * Depends On        : Common Module
 * Used By           : GeneralLedgerBalanceController, GeneralLedgerBalanceServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Finance Module. Implements GeneralLedgerBalanceService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.finance.reporting.service;

import com.plus33.erp.common.exception.BusinessException;
import com.plus33.erp.finance.entity.JournalEntry;
import com.plus33.erp.finance.entity.JournalEntryLine;
import com.plus33.erp.finance.reporting.dto.LedgerBalanceSnapshot;
import com.plus33.erp.finance.reporting.entity.ExchangeRate;
import com.plus33.erp.finance.reporting.entity.ExchangeRateType;
import com.plus33.erp.finance.reporting.repository.ExchangeRateRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code GeneralLedgerBalanceService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.reporting.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Finance Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * GeneralLedgerBalanceController
 *   --> GeneralLedgerBalanceService (this)
 *   --> Validate business rules
 *   --> GeneralLedgerBalanceRepository (read/write 'general_ledger_balances')
 *   --> GeneralLedgerBalanceMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code general_ledger_balances}</p>
 * <p><b>Module Deps      :</b> Common, Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
@RequiredArgsConstructor
public class GeneralLedgerBalanceService {

    @PersistenceContext
    private final EntityManager entityManager;
    private final ExchangeRateRepository exchangeRateRepository;

    /**
     * Retrieves balance snapshot data from the database.
     *
     * @return the LedgerBalanceSnapshot result
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Transactional(readOnly = true)
    public LedgerBalanceSnapshot getBalanceSnapshot(
            Long companyId,
            LocalDate startDate,
            LocalDate endDate,
            String currency,
            String rateTypeStr,
            boolean excludeClosing,
            boolean isFunctionalCurrencyMode
    ) {
        ExchangeRateType rateType = ExchangeRateType.SPOT;
        if (rateTypeStr != null && !rateTypeStr.isBlank()) {
            try {
                rateType = ExchangeRateType.valueOf(rateTypeStr.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new BusinessException("Invalid exchange rate type: " + rateTypeStr);
            }
        }

        // 1. Fetch all posted journal lines in the period
        String jpql = "SELECT line FROM JournalEntryLine line " +
                      "WHERE line.journalEntry.company.id = :companyId " +
                      "  AND line.journalEntry.status = 'POSTED' " +
                      "  AND (:excludeClosing = false OR line.journalEntry.closingEntry = false) " +
                      "  AND line.journalEntry.entryDate >= :startDate " +
                      "  AND line.journalEntry.entryDate <= :endDate";

        List<JournalEntryLine> lines = entityManager.createQuery(jpql, JournalEntryLine.class)
                .setParameter("companyId", companyId)
                .setParameter("excludeClosing", excludeClosing)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getResultList();

        Map<Long, BigDecimal> debitBalances = new HashMap<>();
        Map<Long, BigDecimal> creditBalances = new HashMap<>();
        Map<String, BigDecimal> exchangeRateCache = new HashMap<>();

        // 2. Aggregate balances
        for (JournalEntryLine line : lines) {
            JournalEntry je = line.getJournalEntry();
            Long accountId = line.getAccount().getId();
            BigDecimal debit = line.getDebitAmount();
            BigDecimal credit = line.getCreditAmount();

            BigDecimal convertedDebit = debit;
            BigDecimal convertedCredit = credit;

            if (currency != null && !currency.isBlank()) {
                if (isFunctionalCurrencyMode) {
                    // Convert transaction currency to functional currency
                    if (!je.getCurrencyCode().equalsIgnoreCase(currency)) {
                        BigDecimal rate = getExchangeRate(companyId, je.getCurrencyCode(), currency, rateType, je.getEntryDate(), exchangeRateCache);
                        convertedDebit = debit.multiply(rate).setScale(2, RoundingMode.HALF_UP);
                        convertedCredit = credit.multiply(rate).setScale(2, RoundingMode.HALF_UP);
                    }
                } else {
                    // Transaction Currency Mode: filter to only include matching currency
                    if (!je.getCurrencyCode().equalsIgnoreCase(currency)) {
                        continue; // Skip this line
                    }
                }
            }

            debitBalances.put(accountId, debitBalances.getOrDefault(accountId, BigDecimal.ZERO).add(convertedDebit));
            creditBalances.put(accountId, creditBalances.getOrDefault(accountId, BigDecimal.ZERO).add(convertedCredit));
        }

        return new LedgerBalanceSnapshot(debitBalances, creditBalances, startDate, endDate, currency, rateType);
    }

    private BigDecimal getExchangeRate(
            Long companyId,
            String fromCurrency,
            String toCurrency,
            ExchangeRateType rateType,
            LocalDate date,
            Map<String, BigDecimal> cache
    ) {
        // Same-currency bypass
        if (fromCurrency.equalsIgnoreCase(toCurrency)) {
            return BigDecimal.ONE;
        }

        String cacheKey = fromCurrency + "_" + toCurrency + "_" + rateType + "_" + date;
        if (cache.containsKey(cacheKey)) {
            return cache.get(cacheKey);
        }

        // Query the latest rate effective on or before the date
        Optional<ExchangeRate> optRate = exchangeRateRepository
                .findFirstByCompanyIdAndFromCurrencyAndToCurrencyAndRateTypeAndEffectiveDateLessThanEqualOrderByEffectiveDateDescIdDesc(
                        companyId, fromCurrency, toCurrency, rateType, date
                );

        if (optRate.isEmpty()) {
            throw new BusinessException("Exchange rate not found for " + fromCurrency + " to " + toCurrency +
                    " (Type: " + rateType + ") on or before date: " + date);
        }

        BigDecimal rate = optRate.get().getRate();
        cache.put(cacheKey, rate);
        return rate;
    }
}