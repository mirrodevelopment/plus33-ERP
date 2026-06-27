package com.plus33.erp.finance.treasury.service;

import com.plus33.erp.finance.treasury.entity.TreasuryJournalEntry;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * Centralized double-entry GL posting service for all treasury events.
 *
 * Every treasury transaction (cash transfer, FX deal, investment, interest accrual,
 * bank fee, netting settlement, etc.) must post through this service to guarantee
 * that debits always equal credits and that postings are idempotent.
 *
 * <h3>Idempotency</h3>
 * Each posting method accepts an {@code idempotencyKey}. If an entry with the same
 * key already exists the method returns the existing entries without posting again.
 */
public interface TreasuryJournalService {

    /**
     * Posts journal entries for a cash transfer between two bank accounts.
     *
     * @param companyId       owning company
     * @param cashTransferId  source event ID
     * @param amount          transfer amount
     * @param currencyCode    transfer currency
     * @param exchangeRate    rate to base currency
     * @param postingDate     GL posting date
     * @param actor           user posting the entries
     * @param idempotencyKey  unique key to prevent duplicate postings
     * @return the pair of journal entries (debit, credit)
     */
    List<TreasuryJournalEntry> postCashTransfer(
            Long companyId, Long cashTransferId,
            BigDecimal amount, String currencyCode, BigDecimal exchangeRate,
            LocalDate postingDate, String actor, String idempotencyKey);

    /**
     * Posts journal entries for an FX deal booking (deal date).
     */
    List<TreasuryJournalEntry> postFxDealBooking(
            Long companyId, Long fxDealId,
            BigDecimal buyAmount, String buyCurrency,
            BigDecimal sellAmount, String sellCurrency,
            BigDecimal rate, LocalDate postingDate,
            String actor, String idempotencyKey);

    /**
     * Posts FX revaluation gain or loss entries.
     */
    List<TreasuryJournalEntry> postFxRevaluation(
            Long companyId, Long bankAccountId,
            BigDecimal gainLossAmount, String baseCurrency,
            LocalDate postingDate, String actor, String idempotencyKey);

    /**
     * Posts investment purchase entries.
     */
    List<TreasuryJournalEntry> postInvestmentPurchase(
            Long companyId, Long investmentId,
            BigDecimal principalAmount, String currencyCode,
            BigDecimal exchangeRate, LocalDate postingDate,
            String actor, String idempotencyKey);

    /**
     * Posts investment maturity entries (principal return + interest).
     */
    List<TreasuryJournalEntry> postInvestmentMaturity(
            Long companyId, Long investmentId,
            BigDecimal principalAmount, BigDecimal interestAmount,
            String currencyCode, BigDecimal exchangeRate,
            LocalDate postingDate, String actor, String idempotencyKey);

    /**
     * Posts interest accrual entries (daily/monthly).
     */
    List<TreasuryJournalEntry> postInterestAccrual(
            Long companyId, Long sourceId, String sourceType,
            BigDecimal accrualAmount, String currencyCode,
            LocalDate accrualDate, String actor, String idempotencyKey);

    /**
     * Posts bank fee entries.
     */
    List<TreasuryJournalEntry> postBankFee(
            Long companyId, Long bankAccountId,
            BigDecimal feeAmount, String currencyCode,
            LocalDate postingDate, String actor, String idempotencyKey);

    /**
     * Retrieves all journal entries for a given source event.
     */
    List<TreasuryJournalEntry> getEntriesForEvent(String eventType, Long eventId);
}
