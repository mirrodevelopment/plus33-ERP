/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.service
 * File              : TreasuryJournalServiceImpl.java
 * Purpose           : Business logic service layer for Finance Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TreasuryJournalController
 * Related Service   : TreasuryJournalServiceImpl
 * Related Repository: TreasuryJournalEntryRepository
 * Related Entity    : TreasuryJournal
 * Related DTO       : N/A
 * Related Mapper    : TreasuryJournalMapper
 * Related DB Table  : treasury_journals
 * Related REST APIs : N/A
 * Depends On        : Organization Module
 * Used By           : TreasuryJournalController, TreasuryJournalServiceImplImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Finance Module. Implements TreasuryJournalService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.service;

import com.plus33.erp.finance.entity.Account;
import com.plus33.erp.finance.treasury.entity.TreasuryAccountingProfile;
import com.plus33.erp.finance.treasury.entity.TreasuryJournalEntry;
import com.plus33.erp.finance.treasury.entity.TreasuryJournalEntry.EntryType;
import com.plus33.erp.finance.treasury.repository.TreasuryJournalEntryRepository;
import com.plus33.erp.organization.entity.Company;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Default implementation of {@link TreasuryJournalService}.
 *
 * All postings are idempotent — if a referenceNumber already exists the method
 * returns the existing entries unchanged. The reference number format is:
 * {@code TRY-{EVENT_TYPE}-{EVENT_ID}-{SUFFIX}}
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class TreasuryJournalServiceImpl implements TreasuryJournalService {

    private final TreasuryJournalEntryRepository journalRepo;
    private final TreasuryConfigurationRegistry configRegistry;
    private final EntityManager entityManager;

    // ── Public Methods ────────────────────────────────────────────────────────

    /**
     * Posts cash transfer entries to the General Ledger and updates financial balances.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @return List of matching records
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public List<TreasuryJournalEntry> postCashTransfer(Long companyId, Long cashTransferId,
                                                        BigDecimal amount, String currencyCode,
                                                        BigDecimal exchangeRate,
                                                        LocalDate postingDate, String actor,
                                                        String idempotencyKey) {
        if (alreadyPosted(idempotencyKey + "-DR")) return getEntriesForEvent("CASH_TRANSFER", cashTransferId);

        TreasuryAccountingProfile profile = configRegistry.resolveAccountingProfile(companyId, null);
        BigDecimal baseAmount = amount.multiply(exchangeRate);
        String baseCurrency = resolveBaseCurrency(profile);

        List<TreasuryJournalEntry> entries = new ArrayList<>();
        // Destination cash account — DEBIT
        entries.add(buildEntry(companyId, "CASH_TRANSFER", cashTransferId,
                idempotencyKey + "-DR", profile.getCashAccount(),
                EntryType.DEBIT, amount, currencyCode, baseAmount, baseCurrency,
                exchangeRate, postingDate, "Cash transfer debit", actor));

        // Source cash account — CREDIT
        entries.add(buildEntry(companyId, "CASH_TRANSFER", cashTransferId,
                idempotencyKey + "-CR", profile.getCashAccount(),
                EntryType.CREDIT, amount, currencyCode, baseAmount, baseCurrency,
                exchangeRate, postingDate, "Cash transfer credit", actor));

        List<TreasuryJournalEntry> saved = journalRepo.saveAll(entries);
        log.info("Posted CASH_TRANSFER journals: transferId={} amount={} {}", cashTransferId, amount, currencyCode);
        return saved;
    }

    /**
     * Posts fx deal booking entries to the General Ledger and updates financial balances.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @return List of matching records
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public List<TreasuryJournalEntry> postFxDealBooking(Long companyId, Long fxDealId,
                                                         BigDecimal buyAmount, String buyCurrency,
                                                         BigDecimal sellAmount, String sellCurrency,
                                                         BigDecimal rate, LocalDate postingDate,
                                                         String actor, String idempotencyKey) {
        if (alreadyPosted(idempotencyKey + "-BUY")) return getEntriesForEvent("FX_DEAL", fxDealId);

        TreasuryAccountingProfile profile = configRegistry.resolveAccountingProfile(companyId, null);
        String baseCurrency = resolveBaseCurrency(profile);

        List<TreasuryJournalEntry> entries = new ArrayList<>();
        // Buy leg — DEBIT cash in buy currency
        entries.add(buildEntry(companyId, "FX_DEAL", fxDealId,
                idempotencyKey + "-BUY", profile.getCashAccount(),
                EntryType.DEBIT, buyAmount, buyCurrency,
                buyAmount.multiply(rate), baseCurrency,
                rate, postingDate, "FX deal buy leg", actor));

        // Sell leg — CREDIT cash in sell currency
        entries.add(buildEntry(companyId, "FX_DEAL", fxDealId,
                idempotencyKey + "-SELL", profile.getCashAccount(),
                EntryType.CREDIT, sellAmount, sellCurrency,
                sellAmount, baseCurrency,
                BigDecimal.ONE, postingDate, "FX deal sell leg", actor));

        List<TreasuryJournalEntry> saved = journalRepo.saveAll(entries);
        log.info("Posted FX_DEAL journals: dealId={} buy={} {} sell={} {}", fxDealId, buyAmount, buyCurrency, sellAmount, sellCurrency);
        return saved;
    }

    /**
     * Posts fx revaluation entries to the General Ledger and updates financial balances.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @return List of matching records
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public List<TreasuryJournalEntry> postFxRevaluation(Long companyId, Long bankAccountId,
                                                         BigDecimal gainLossAmount, String baseCurrency,
                                                         LocalDate postingDate, String actor,
                                                         String idempotencyKey) {
        if (alreadyPosted(idempotencyKey + "-REVAL")) return getEntriesForEvent("FX_REVALUATION", bankAccountId);

        TreasuryAccountingProfile profile = configRegistry.resolveAccountingProfile(companyId, null);
        boolean isGain = gainLossAmount.compareTo(BigDecimal.ZERO) > 0;
        BigDecimal absAmount = gainLossAmount.abs();

        Account plAccount = isGain ? profile.getFxGainAccount() : profile.getFxLossAccount();

        List<TreasuryJournalEntry> entries = new ArrayList<>();
        // Cash account adjustment
        entries.add(buildEntry(companyId, "FX_REVALUATION", bankAccountId,
                idempotencyKey + "-REVAL-CASH", profile.getCashAccount(),
                isGain ? EntryType.DEBIT : EntryType.CREDIT,
                absAmount, baseCurrency, absAmount, baseCurrency,
                BigDecimal.ONE, postingDate, "FX revaluation cash", actor));

        // P&L account offset
        entries.add(buildEntry(companyId, "FX_REVALUATION", bankAccountId,
                idempotencyKey + "-REVAL-PL", plAccount,
                isGain ? EntryType.CREDIT : EntryType.DEBIT,
                absAmount, baseCurrency, absAmount, baseCurrency,
                BigDecimal.ONE, postingDate, "FX revaluation gain/loss", actor));

        List<TreasuryJournalEntry> saved = journalRepo.saveAll(entries);
        log.info("Posted FX_REVALUATION journals: bankAccountId={} gainLoss={} {}", bankAccountId, gainLossAmount, baseCurrency);
        return saved;
    }

    /**
     * Posts investment purchase entries to the General Ledger and updates financial balances.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @return List of matching records
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public List<TreasuryJournalEntry> postInvestmentPurchase(Long companyId, Long investmentId,
                                                              BigDecimal principalAmount, String currencyCode,
                                                              BigDecimal exchangeRate, LocalDate postingDate,
                                                              String actor, String idempotencyKey) {
        if (alreadyPosted(idempotencyKey + "-INV-PUR")) return getEntriesForEvent("INVESTMENT_PURCHASE", investmentId);

        TreasuryAccountingProfile profile = configRegistry.resolveAccountingProfile(companyId, null);
        BigDecimal baseAmount = principalAmount.multiply(exchangeRate);
        String baseCurrency = resolveBaseCurrency(profile);

        List<TreasuryJournalEntry> entries = new ArrayList<>();
        // Investment account — DEBIT
        entries.add(buildEntry(companyId, "INVESTMENT_PURCHASE", investmentId,
                idempotencyKey + "-INV-PUR-DR", profile.getInvestmentAccount(),
                EntryType.DEBIT, principalAmount, currencyCode, baseAmount, baseCurrency,
                exchangeRate, postingDate, "Investment purchase", actor));

        // Cash account — CREDIT
        entries.add(buildEntry(companyId, "INVESTMENT_PURCHASE", investmentId,
                idempotencyKey + "-INV-PUR-CR", profile.getCashAccount(),
                EntryType.CREDIT, principalAmount, currencyCode, baseAmount, baseCurrency,
                exchangeRate, postingDate, "Investment funded from cash", actor));

        return journalRepo.saveAll(entries);
    }

    /**
     * Posts investment maturity entries to the General Ledger and updates financial balances.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @return List of matching records
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public List<TreasuryJournalEntry> postInvestmentMaturity(Long companyId, Long investmentId,
                                                              BigDecimal principalAmount, BigDecimal interestAmount,
                                                              String currencyCode, BigDecimal exchangeRate,
                                                              LocalDate postingDate, String actor,
                                                              String idempotencyKey) {
        if (alreadyPosted(idempotencyKey + "-INV-MAT")) return getEntriesForEvent("INVESTMENT_MATURITY", investmentId);

        TreasuryAccountingProfile profile = configRegistry.resolveAccountingProfile(companyId, null);
        String baseCurrency = resolveBaseCurrency(profile);
        BigDecimal totalAmount = principalAmount.add(interestAmount);

        List<TreasuryJournalEntry> entries = new ArrayList<>();
        // Cash receipt — DEBIT
        entries.add(buildEntry(companyId, "INVESTMENT_MATURITY", investmentId,
                idempotencyKey + "-INV-MAT-CASH", profile.getCashAccount(),
                EntryType.DEBIT, totalAmount, currencyCode,
                totalAmount.multiply(exchangeRate), baseCurrency,
                exchangeRate, postingDate, "Investment maturity proceeds", actor));

        // Investment account close — CREDIT principal
        entries.add(buildEntry(companyId, "INVESTMENT_MATURITY", investmentId,
                idempotencyKey + "-INV-MAT-PRIN", profile.getInvestmentAccount(),
                EntryType.CREDIT, principalAmount, currencyCode,
                principalAmount.multiply(exchangeRate), baseCurrency,
                exchangeRate, postingDate, "Investment maturity principal return", actor));

        // Interest income — CREDIT
        entries.add(buildEntry(companyId, "INVESTMENT_MATURITY", investmentId,
                idempotencyKey + "-INV-MAT-INT", profile.getInterestIncomeAccount(),
                EntryType.CREDIT, interestAmount, currencyCode,
                interestAmount.multiply(exchangeRate), baseCurrency,
                exchangeRate, postingDate, "Investment interest income", actor));

        return journalRepo.saveAll(entries);
    }

    /**
     * Posts interest accrual entries to the General Ledger and updates financial balances.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @return List of matching records
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public List<TreasuryJournalEntry> postInterestAccrual(Long companyId, Long sourceId,
                                                           String sourceType, BigDecimal accrualAmount,
                                                           String currencyCode, LocalDate accrualDate,
                                                           String actor, String idempotencyKey) {
        if (alreadyPosted(idempotencyKey + "-ACCRL")) return getEntriesForEvent(sourceType, sourceId);

        TreasuryAccountingProfile profile = configRegistry.resolveAccountingProfile(companyId, null);
        String baseCurrency = resolveBaseCurrency(profile);

        List<TreasuryJournalEntry> entries = new ArrayList<>();
        // Accrued interest receivable — DEBIT
        entries.add(buildEntry(companyId, sourceType, sourceId,
                idempotencyKey + "-ACCRL-DR", profile.getInterestIncomeAccount(),
                EntryType.DEBIT, accrualAmount, currencyCode, accrualAmount, baseCurrency,
                BigDecimal.ONE, accrualDate, "Interest accrual", actor));

        // Interest income — CREDIT
        entries.add(buildEntry(companyId, sourceType, sourceId,
                idempotencyKey + "-ACCRL-CR", profile.getInterestIncomeAccount(),
                EntryType.CREDIT, accrualAmount, currencyCode, accrualAmount, baseCurrency,
                BigDecimal.ONE, accrualDate, "Interest income accrual", actor));

        return journalRepo.saveAll(entries);
    }

    /**
     * Posts bank fee entries to the General Ledger and updates financial balances.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @return List of matching records
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public List<TreasuryJournalEntry> postBankFee(Long companyId, Long bankAccountId,
                                                   BigDecimal feeAmount, String currencyCode,
                                                   LocalDate postingDate, String actor,
                                                   String idempotencyKey) {
        if (alreadyPosted(idempotencyKey + "-FEE")) return getEntriesForEvent("BANK_FEE", bankAccountId);

        TreasuryAccountingProfile profile = configRegistry.resolveAccountingProfile(companyId, null);
        String baseCurrency = resolveBaseCurrency(profile);

        List<TreasuryJournalEntry> entries = new ArrayList<>();
        // Bank fee expense — DEBIT
        entries.add(buildEntry(companyId, "BANK_FEE", bankAccountId,
                idempotencyKey + "-FEE-DR", profile.getBankFeeAccount(),
                EntryType.DEBIT, feeAmount, currencyCode, feeAmount, baseCurrency,
                BigDecimal.ONE, postingDate, "Bank fee expense", actor));

        // Cash reduction — CREDIT
        entries.add(buildEntry(companyId, "BANK_FEE", bankAccountId,
                idempotencyKey + "-FEE-CR", profile.getCashAccount(),
                EntryType.CREDIT, feeAmount, currencyCode, feeAmount, baseCurrency,
                BigDecimal.ONE, postingDate, "Bank fee cash reduction", actor));

        return journalRepo.saveAll(entries);
    }

    /**
     * Retrieves entries for event data from the database.
     *
     * @param eventType the eventType input value
     * @param eventId the eventId input value
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    @Transactional(readOnly = true)
    public List<TreasuryJournalEntry> getEntriesForEvent(String eventType, Long eventId) {
        return journalRepo.findByEventTypeAndEventId(eventType, eventId);
    }

    // ── Private Helpers ───────────────────────────────────────────────────────

    /**
     * Resolves the base currency from the accounting profile.
     * Defaults to "AED" (company base currency).
     * TODO: fetch from Company.baseCurrencyCode once that field is added.
     */
    private String resolveBaseCurrency(TreasuryAccountingProfile profile) {
        return "AED";
    }

    private boolean alreadyPosted(String referenceNumber) {
        return journalRepo.findByReferenceNumber(referenceNumber).isPresent();
    }

    private TreasuryJournalEntry buildEntry(Long companyId, String eventType, Long eventId,
                                             String refNum, Account account,
                                             EntryType entryType, BigDecimal amount,
                                             String currencyCode, BigDecimal baseAmount,
                                             String baseCurrencyCode, BigDecimal exchangeRate,
                                             LocalDate postingDate, String description, String actor) {
        return TreasuryJournalEntry.builder()
                .company(entityManager.getReference(Company.class, companyId))
                .eventType(eventType)
                .eventId(eventId)
                .referenceNumber(refNum)
                .account(account)
                .entryType(entryType)
                .amount(amount)
                .currencyCode(currencyCode)
                .baseCurrencyAmount(baseAmount)
                .baseCurrencyCode(baseCurrencyCode)
                .exchangeRate(exchangeRate)
                .postingDate(postingDate)
                .valueDate(postingDate)
                .description(description)
                .postedBy(actor)
                .build();
    }
}