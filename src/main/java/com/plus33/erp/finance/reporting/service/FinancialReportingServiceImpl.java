/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.reporting.service
 * File              : FinancialReportingServiceImpl.java
 * Purpose           : Business logic service layer for Finance Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: FinancialReportingController
 * Related Service   : FinancialReportingServiceImpl
 * Related Repository: FiscalYearRepository, PeriodLockRepository, CompanyRepository, AccountRepository, JournalEntryRepository, UserRepository
 * Related Entity    : FinancialReporting
 * Related DTO       : BalanceSheetResponse, FinancialReportExportRequest, FiscalYearCloseRequest, FiscalYearCloseResponse, IncomeStatementResponse
 * Related Mapper    : FinancialReportingMapper
 * Related DB Table  : financial_reportings
 * Related REST APIs : N/A
 * Depends On        : Common Module, Organization Module, Security Module
 * Used By           : FinancialReportingController, FinancialReportingServiceImplImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Finance Module. Implements FinancialReportingService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.finance.reporting.service;

import com.plus33.erp.common.exception.BusinessException;
import com.plus33.erp.common.exception.ResourceNotFoundException;
import com.plus33.erp.finance.entity.*;
import com.plus33.erp.finance.repository.*;
import com.plus33.erp.finance.reporting.dto.*;
import com.plus33.erp.finance.reporting.entity.*;
import com.plus33.erp.finance.reporting.repository.*;
import com.plus33.erp.finance.reporting.service.export.FinancialReportExporter;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.repository.CompanyRepository;
import com.plus33.erp.security.entity.User;
import com.plus33.erp.security.repository.UserRepository;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code FinancialReportingServiceImpl}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.reporting.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Finance Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * FinancialReportingController
 *   --> FinancialReportingServiceImpl (this)
 *   --> Validate business rules
 *   --> FinancialReportingRepository (read/write 'financial_reportings')
 *   --> FinancialReportingMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code financial_reportings}</p>
 * <p><b>Module Deps      :</b> Common, Finance, Organization, Security</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class FinancialReportingServiceImpl implements FinancialReportingService {

    private final GeneralLedgerBalanceService generalLedgerBalanceService;
    private final FiscalYearRepository fiscalYearRepository;
    private final PeriodLockRepository periodLockRepository;
    private final CompanyRepository companyRepository;
    private final AccountRepository accountRepository;
    private final JournalEntryRepository journalEntryRepository;
    private final UserRepository userRepository;
    private final List<FinancialReportExporter> exporters;
    private final EntityManager entityManager;

    /**
     * Retrieves trial balance data from the database.
     *
     * @return the TrialBalanceResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    @Transactional(readOnly = true)
    public TrialBalanceResponse getTrialBalance(
            Long companyId,
            LocalDate startDate,
            LocalDate endDate,
            String currency,
            String rateType,
            boolean excludeClosing
    ) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));

        validateCompanyFiscalCalendar(company);

        List<Account> accounts = accountRepository.findByCompanyId(companyId);
        boolean isFunctional = (rateType != null && !rateType.isBlank());

        // 1. Get Opening Balances Snapshot (Dynamic from inception to day before start)
        LocalDate openingStart = LocalDate.of(1970, 1, 1);
        LocalDate openingEnd = startDate.minusDays(1);
        LedgerBalanceSnapshot openingSnapshot = generalLedgerBalanceService.getBalanceSnapshot(
                companyId, openingStart, openingEnd, currency, rateType, excludeClosing, isFunctional
        );

        // 2. Get Period Movements Snapshot
        LedgerBalanceSnapshot periodSnapshot = generalLedgerBalanceService.getBalanceSnapshot(
                companyId, startDate, endDate, currency, rateType, excludeClosing, isFunctional
        );

        List<TrialBalanceEntry> entries = new ArrayList<>();
        BigDecimal totalDebits = BigDecimal.ZERO;
        BigDecimal totalCredits = BigDecimal.ZERO;

        for (Account acc : accounts) {
            Long accId = acc.getId();

            // Raw sums
            BigDecimal rawOpDebit = openingSnapshot.debitBalances().getOrDefault(accId, BigDecimal.ZERO);
            BigDecimal rawOpCredit = openingSnapshot.creditBalances().getOrDefault(accId, BigDecimal.ZERO);
            BigDecimal perDebit = periodSnapshot.debitBalances().getOrDefault(accId, BigDecimal.ZERO);
            BigDecimal perCredit = periodSnapshot.creditBalances().getOrDefault(accId, BigDecimal.ZERO);

            // Net opening based on type
            BigDecimal opDebit = BigDecimal.ZERO;
            BigDecimal opCredit = BigDecimal.ZERO;
            if (isDebitNormal(acc.getAccountType())) {
                if (rawOpDebit.compareTo(rawOpCredit) >= 0) {
                    opDebit = rawOpDebit.subtract(rawOpCredit);
                } else {
                    opCredit = rawOpCredit.subtract(rawOpDebit);
                }
            } else {
                if (rawOpCredit.compareTo(rawOpDebit) >= 0) {
                    opCredit = rawOpCredit.subtract(rawOpDebit);
                } else {
                    opDebit = rawOpDebit.subtract(rawOpCredit);
                }
            }

            // Closing totals
            BigDecimal closingDebitSum = opDebit.add(perDebit);
            BigDecimal closingCreditSum = opCredit.add(perCredit);

            BigDecimal clDebit = BigDecimal.ZERO;
            BigDecimal clCredit = BigDecimal.ZERO;
            if (isDebitNormal(acc.getAccountType())) {
                if (closingDebitSum.compareTo(closingCreditSum) >= 0) {
                    clDebit = closingDebitSum.subtract(closingCreditSum);
                } else {
                    clCredit = closingCreditSum.subtract(closingDebitSum);
                }
            } else {
                if (closingCreditSum.compareTo(closingDebitSum) >= 0) {
                    clCredit = closingCreditSum.subtract(closingDebitSum);
                } else {
                    clDebit = closingDebitSum.subtract(closingCreditSum);
                }
            }

            entries.add(new TrialBalanceEntry(
                    accId, acc.getAccountCode(), acc.getAccountName(), acc.getAccountType(),
                    acc.getParentAccount() != null ? acc.getParentAccount().getId() : null,
                    opDebit, opCredit, perDebit, perCredit, clDebit, clCredit
            ));

            totalDebits = totalDebits.add(clDebit);
            totalCredits = totalCredits.add(clCredit);
        }

        BigDecimal difference = totalDebits.subtract(totalCredits);
        boolean balanced = difference.compareTo(BigDecimal.ZERO) == 0;
        String valMessage = balanced ? "Trial Balance is perfectly balanced." : "Trial Balance out of balance by " + difference.abs();

        // 3. Dynamic Audit Warnings
        List<ReportWarning> warnings = new ArrayList<>();
        if (!balanced) {
            warnings.add(new ReportWarning("ERROR", "Trial Balance is out of balance by " + difference.abs(), "TB_UNBALANCED"));
        }

        boolean hasRetainedEarnings = accounts.stream().anyMatch(a -> a.getAccountCode().equals("3100"));
        if (!hasRetainedEarnings) {
            warnings.add(new ReportWarning("WARNING", "Missing Retained Earnings account (3100) for this company", "MISSING_RETAINED_EARNINGS"));
        }

        // Check for unposted draft journals in the period
        String draftJpql = "SELECT COUNT(j) FROM JournalEntry j " +
                           "WHERE j.company.id = :companyId " +
                           "  AND j.status = 'DRAFT' " +
                           "  AND j.entryDate >= :startDate " +
                           "  AND j.entryDate <= :endDate";
        Long draftCount = entityManager.createQuery(draftJpql, Long.class)
                .setParameter("companyId", companyId)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getSingleResult();
        if (draftCount > 0) {
            warnings.add(new ReportWarning("INFO", "Unposted (DRAFT) journal entries (" + draftCount + ") exist in the period and were excluded", "DRAFT_JOURNALS_EXCLUDED"));
        }

        // Check for inactive accounts with balance
        for (TrialBalanceEntry entry : entries) {
            Account acc = accounts.stream().filter(a -> a.getId().equals(entry.accountId())).findFirst().orElse(null);
            if (acc != null && !acc.getActive()) {
                if (entry.closingDebit().compareTo(BigDecimal.ZERO) > 0 || entry.closingCredit().compareTo(BigDecimal.ZERO) > 0) {
                    warnings.add(new ReportWarning("WARNING", "Inactive account " + acc.getAccountCode() + " has a non-zero balance: " + entry.closingDebit().add(entry.closingCredit()), "INACTIVE_ACCOUNT_WITH_BALANCE"));
                }
            }
        }

        return new TrialBalanceResponse(
                entries, startDate, endDate, totalDebits, totalCredits, difference, balanced, valMessage,
                warnings, accounts.size(), LocalDateTime.now(), getCurrentUserEmail()
        );
    }

    /**
     * Retrieves income statement data from the database.
     *
     * @return the IncomeStatementResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    @Transactional(readOnly = true)
    public IncomeStatementResponse getIncomeStatement(
            Long companyId,
            LocalDate startDate,
            LocalDate endDate,
            String currency,
            String rateType,
            boolean excludeClosing
    ) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));

        validateCompanyFiscalCalendar(company);

        boolean isFunctional = (rateType != null && !rateType.isBlank());
        LedgerBalanceSnapshot snapshot = generalLedgerBalanceService.getBalanceSnapshot(
                companyId, startDate, endDate, currency, rateType, excludeClosing, isFunctional
        );

        List<Account> accounts = accountRepository.findByCompanyId(companyId);
        List<IncomeStatementEntry> revenues = new ArrayList<>();
        List<IncomeStatementEntry> expenses = new ArrayList<>();

        BigDecimal totalRevenue = BigDecimal.ZERO;
        BigDecimal totalExpenses = BigDecimal.ZERO;

        for (Account acc : accounts) {
            Long accId = acc.getId();
            BigDecimal debit = snapshot.debitBalances().getOrDefault(accId, BigDecimal.ZERO);
            BigDecimal credit = snapshot.creditBalances().getOrDefault(accId, BigDecimal.ZERO);

            if (acc.getAccountType().equals("REVENUE")) {
                BigDecimal balance = credit.subtract(debit);
                if (balance.compareTo(BigDecimal.ZERO) != 0) {
                    revenues.add(new IncomeStatementEntry(accId, acc.getAccountCode(), acc.getAccountName(), balance));
                    totalRevenue = totalRevenue.add(balance);
                }
            } else if (acc.getAccountType().equals("EXPENSE")) {
                BigDecimal balance = debit.subtract(credit);
                if (balance.compareTo(BigDecimal.ZERO) != 0) {
                    expenses.add(new IncomeStatementEntry(accId, acc.getAccountCode(), acc.getAccountName(), balance));
                    totalExpenses = totalExpenses.add(balance);
                }
            }
        }

        BigDecimal netIncome = totalRevenue.subtract(totalExpenses);

        return new IncomeStatementResponse(
                revenues, expenses, totalRevenue, totalExpenses, netIncome,
                startDate, endDate, LocalDateTime.now(), getCurrentUserEmail()
        );
    }

    /**
     * Retrieves balance sheet data from the database.
     *
     * @return the BalanceSheetResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    @Transactional(readOnly = true)
    public BalanceSheetResponse getBalanceSheet(
            Long companyId,
            LocalDate asOfDate,
            String currency,
            String rateType,
            boolean excludeClosing
    ) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));

        validateCompanyFiscalCalendar(company);

        boolean isFunctional = (rateType != null && !rateType.isBlank());

        // 1. Double-Counting Protection: Calculate current unclosed year's Net Income once and cache
        LocalDate fiscalStart = getFiscalYearStartDateForDate(company, asOfDate);
        IncomeStatementResponse currentYearIS = getIncomeStatement(companyId, fiscalStart, asOfDate, currency, rateType, excludeClosing);
        BigDecimal cachedNetIncome = currentYearIS.netIncome();

        // 2. Fetch cumulative snapshot up to asOfDate
        LedgerBalanceSnapshot snapshot = generalLedgerBalanceService.getBalanceSnapshot(
                companyId, LocalDate.of(1970, 1, 1), asOfDate, currency, rateType, excludeClosing, isFunctional
        );

        List<Account> accounts = accountRepository.findByCompanyId(companyId);
        List<BalanceSheetEntry> assets = new ArrayList<>();
        List<BalanceSheetEntry> liabilities = new ArrayList<>();
        List<BalanceSheetEntry> equities = new ArrayList<>();

        BigDecimal totalAssets = BigDecimal.ZERO;
        BigDecimal totalLiabilities = BigDecimal.ZERO;
        BigDecimal totalEquity = BigDecimal.ZERO;

        boolean reAccountFound = false;

        for (Account acc : accounts) {
            Long accId = acc.getId();
            BigDecimal debit = snapshot.debitBalances().getOrDefault(accId, BigDecimal.ZERO);
            BigDecimal credit = snapshot.creditBalances().getOrDefault(accId, BigDecimal.ZERO);

            if (acc.getAccountType().equals("ASSET")) {
                BigDecimal balance = debit.subtract(credit);
                assets.add(new BalanceSheetEntry(accId, acc.getAccountCode(), acc.getAccountName(), balance));
                totalAssets = totalAssets.add(balance);
            } else if (acc.getAccountType().equals("LIABILITY")) {
                BigDecimal balance = credit.subtract(debit);
                liabilities.add(new BalanceSheetEntry(accId, acc.getAccountCode(), acc.getAccountName(), balance));
                totalLiabilities = totalLiabilities.add(balance);
            } else if (acc.getAccountType().equals("EQUITY")) {
                BigDecimal ledgerBalance = credit.subtract(debit);
                BigDecimal finalBalance = ledgerBalance;

                if (acc.getAccountCode().equals("3100")) {
                    reAccountFound = true;
                    // Add current unclosed year's dynamic net income to the historic closed ledger balance of 3100
                    finalBalance = ledgerBalance.add(cachedNetIncome);
                }

                equities.add(new BalanceSheetEntry(accId, acc.getAccountCode(), acc.getAccountName(), finalBalance));
                totalEquity = totalEquity.add(finalBalance);
            }
        }

        // If Retained Earnings account is missing from COA, virtualize the dynamic Net Income to ensure balancing
        if (!reAccountFound) {
            equities.add(new BalanceSheetEntry(null, "3100", "Retained Earnings (Current Year)", cachedNetIncome));
            totalEquity = totalEquity.add(cachedNetIncome);
        }

        BigDecimal difference = totalAssets.subtract(totalLiabilities).subtract(totalEquity);
        boolean balanced = difference.compareTo(BigDecimal.ZERO) == 0;
        String valMessage = balanced ? "Balance Sheet is perfectly balanced." : "Balance Sheet out of balance by " + difference.abs();

        return new BalanceSheetResponse(
                assets, liabilities, equities, totalAssets, totalLiabilities, totalEquity, difference,
                balanced, valMessage, asOfDate, LocalDateTime.now(), getCurrentUserEmail()
        );
    }

    /**
     * Exports report data as a report or downloadable file.
     *
     * @return the result string value
     */
    @Override
    @Transactional(readOnly = true)
    public String exportReport(
            Long companyId,
            String reportType,
            LocalDate startDate,
            LocalDate endDate,
            String currency,
            String rateType,
            boolean excludeClosing,
            String format
    ) {
        FinancialReportExporter exporter = exporters.stream()
                .filter(e -> e.getFormat().equalsIgnoreCase(format))
                .findFirst()
                .orElseThrow(() -> new BusinessException("Unsupported export format: " + format));

        FinancialReportExportRequest context = new FinancialReportExportRequest(
                companyId, reportType, startDate, endDate, currency, rateType, excludeClosing
        );

        if (reportType.equalsIgnoreCase("TRIAL_BALANCE")) {
            TrialBalanceResponse data = getTrialBalance(companyId, startDate, endDate, currency, rateType, excludeClosing);
            return exporter.exportTrialBalance(data, context);
        } else if (reportType.equalsIgnoreCase("INCOME_STATEMENT")) {
            IncomeStatementResponse data = getIncomeStatement(companyId, startDate, endDate, currency, rateType, excludeClosing);
            return exporter.exportIncomeStatement(data, context);
        } else if (reportType.equalsIgnoreCase("BALANCE_SHEET")) {
            BalanceSheetResponse data = getBalanceSheet(companyId, endDate, currency, rateType, excludeClosing);
            return exporter.exportBalanceSheet(data, context);
        } else {
            throw new BusinessException("Unsupported report type: " + reportType);
        }
    }

    /**
     * Performs the lockPeriod operation in this module.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param request the validated request DTO containing input data
     * @return the PeriodLockResponse result
     */
    @Override
    @Transactional
    public PeriodLockResponse lockPeriod(Long companyId, PeriodLockRequest request) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));

        PeriodLockType type;
        try {
            type = PeriodLockType.valueOf(request.lockType().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new BusinessException("Invalid lock type: " + request.lockType());
        }

        PeriodLock lock = periodLockRepository.findByCompanyId(companyId)
                .orElseGet(() -> PeriodLock.builder().company(company).build());

        lock.setLockDate(request.lockDate());
        lock.setLockType(type);
        lock.setLockedBy(getCurrentUserEmail());
        lock.setLockedAt(LocalDateTime.now());
        lock.setReason(request.reason());

        PeriodLock saved = periodLockRepository.save(lock);

        return new PeriodLockResponse(
                saved.getId(), saved.getCompany().getId(), saved.getLockDate(),
                saved.getLockType().name(), saved.getLockedBy(), saved.getLockedAt(), saved.getReason()
        );
    }

    /**
     * Retrieves period lock data from the database.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @return the PeriodLockResponse result
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    @Transactional(readOnly = true)
    public PeriodLockResponse getPeriodLock(Long companyId) {
        PeriodLock lock = periodLockRepository.findByCompanyId(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("No period lock configured for company"));

        return new PeriodLockResponse(
                lock.getId(), lock.getCompany().getId(), lock.getLockDate(),
                lock.getLockType().name(), lock.getLockedBy(), lock.getLockedAt(), lock.getReason()
        );
    }

    /**
     * Completes the fiscal year workflow and finalizes the record status.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param request the validated request DTO containing input data
     * @return the FiscalYearCloseResponse result
     */
    @Override
    @Transactional
    public FiscalYearCloseResponse closeFiscalYear(Long companyId, FiscalYearCloseRequest request) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));

        validateCompanyFiscalCalendar(company);

        Integer year = request.fiscalYear();

        // 1. Verify year not already closed
        Optional<FiscalYear> optFy = fiscalYearRepository.findByCompanyIdAndFiscalYear(companyId, year);
        if (optFy.isPresent() && optFy.get().getStatus() == FiscalYearStatus.CLOSED) {
            throw new BusinessException("Fiscal year " + year + " is already closed.");
        }

        LocalDate startDate = getFiscalYearStartDate(company, year);
        LocalDate endDate = getFiscalYearEndDate(company, year);

        // 2. Completeness Check: Ensure no unposted (DRAFT) journal entries in the period
        String countDRAFTJpql = "SELECT COUNT(j) FROM JournalEntry j " +
                                "WHERE j.company.id = :companyId " +
                                "  AND j.status = 'DRAFT' " +
                                "  AND j.entryDate >= :startDate " +
                                "  AND j.entryDate <= :endDate";
        Long draftCount = entityManager.createQuery(countDRAFTJpql, Long.class)
                .setParameter("companyId", companyId)
                .setParameter("startDate", startDate)
                .setParameter("endDate", endDate)
                .getSingleResult();
        if (draftCount > 0) {
            throw new BusinessException("Cannot close fiscal year. There are " + draftCount + " unposted (DRAFT) journal entries in the period.");
        }

        // 3. Query all REVENUE and EXPENSE account balances for the year
        // We run a snapshot excluding closing entries to get the real operating balances
        LedgerBalanceSnapshot snapshot = generalLedgerBalanceService.getBalanceSnapshot(
                companyId, startDate, endDate, null, null, true, false
        );

        List<Account> accounts = accountRepository.findByCompanyId(companyId);
        Account reAccount = accountRepository.findByCompanyIdAndAccountCode(companyId, "3100")
                .orElseThrow(() -> new BusinessException("Retained Earnings account (3100) not found in Chart of Accounts. Close aborted."));

        User currentUser = getCurrentUser();
        Long nextSeq = journalEntryRepository.getNextSequenceValue();
        String jeNumber = String.format("CL-%d-%d-%06d", companyId, year, nextSeq);

        JournalEntry je = JournalEntry.builder()
                .entryNumber(jeNumber)
                .company(company)
                .entryDate(endDate)
                .description("Fiscal Year Closing Journal " + year)
                .sourceModule("FISCAL_CLOSE")
                .sourceReference("FY-" + year)
                .status("POSTED")
                .closingEntry(true)
                .closingType(ClosingEntryType.NORMAL)
                .postedAt(LocalDateTime.now())
                .createdBy(currentUser)
                .currencyCode("AED") // Base system currency
                .lines(new ArrayList<>())
                .build();

        BigDecimal netRevenue = BigDecimal.ZERO;
        BigDecimal netExpenses = BigDecimal.ZERO;

        for (Account acc : accounts) {
            Long accId = acc.getId();
            BigDecimal debit = snapshot.debitBalances().getOrDefault(accId, BigDecimal.ZERO);
            BigDecimal credit = snapshot.creditBalances().getOrDefault(accId, BigDecimal.ZERO);

            if (acc.getAccountType().equals("REVENUE")) {
                BigDecimal balance = credit.subtract(debit);
                if (balance.compareTo(BigDecimal.ZERO) != 0) {
                    // Debit Revenue account to zero it
                    je.getLines().add(JournalEntryLine.builder()
                            .journalEntry(je)
                            .account(acc)
                            .debitAmount(balance)
                            .creditAmount(BigDecimal.ZERO)
                            .build());
                    netRevenue = netRevenue.add(balance);
                }
            } else if (acc.getAccountType().equals("EXPENSE")) {
                BigDecimal balance = debit.subtract(credit);
                if (balance.compareTo(BigDecimal.ZERO) != 0) {
                    // Credit Expense account to zero it
                    je.getLines().add(JournalEntryLine.builder()
                            .journalEntry(je)
                            .account(acc)
                            .debitAmount(BigDecimal.ZERO)
                            .creditAmount(balance)
                            .build());
                    netExpenses = netExpenses.add(balance);
                }
            }
        }

        BigDecimal netIncome = netRevenue.subtract(netExpenses);
        if (netIncome.compareTo(BigDecimal.ZERO) != 0) {
            // Post the net difference to Retained Earnings (3100)
            if (netIncome.compareTo(BigDecimal.ZERO) > 0) {
                // Profit: Credit Retained Earnings
                je.getLines().add(JournalEntryLine.builder()
                        .journalEntry(je)
                        .account(reAccount)
                        .debitAmount(BigDecimal.ZERO)
                        .creditAmount(netIncome)
                        .build());
            } else {
                // Loss: Debit Retained Earnings
                je.getLines().add(JournalEntryLine.builder()
                        .journalEntry(je)
                        .account(reAccount)
                        .debitAmount(netIncome.abs())
                        .creditAmount(BigDecimal.ZERO)
                        .build());
            }
        }

        // Save closing journal entry
        JournalEntry savedJE = journalEntryRepository.save(je);

        // 4. Create/Update FiscalYear record
        FiscalYear fy = optFy.orElseGet(() -> FiscalYear.builder()
                .company(company)
                .fiscalYear(year)
                .startDate(startDate)
                .endDate(endDate)
                .build());

        fy.setStatus(FiscalYearStatus.CLOSED);
        fy.setClosingJournal(savedJE);
        fy.setClosedBy(getCurrentUserEmail());
        fy.setClosedAt(LocalDateTime.now());
        fiscalYearRepository.save(fy);

        // 5. Apply HARD period lock up to the closing date
        PeriodLock lock = periodLockRepository.findByCompanyId(companyId)
                .orElseGet(() -> PeriodLock.builder().company(company).build());
        lock.setLockDate(endDate);
        lock.setLockType(PeriodLockType.HARD);
        lock.setLockedBy(getCurrentUserEmail());
        lock.setLockedAt(LocalDateTime.now());
        lock.setReason("Automated lock upon closing fiscal year " + year);
        periodLockRepository.save(lock);

        return new FiscalYearCloseResponse(
                fy.getId(), fy.getCompany().getId(), fy.getFiscalYear(),
                fy.getStartDate(), fy.getEndDate(), fy.getStatus().name(),
                savedJE.getId(), fy.getClosedBy(), fy.getClosedAt()
        );
    }

    /**
     * Performs the reopenFiscalYear operation in this module.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param fiscalYear the fiscalYear input value
     */
    @Override
    @Transactional
    public void reopenFiscalYear(Long companyId, Integer fiscalYear) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new ResourceNotFoundException("Company not found"));

        validateCompanyFiscalCalendar(company);

        FiscalYear fy = fiscalYearRepository.findByCompanyIdAndFiscalYear(companyId, fiscalYear)
                .orElseThrow(() -> new ResourceNotFoundException("Closed fiscal year record not found"));

        if (fy.getStatus() != FiscalYearStatus.CLOSED) {
            throw new BusinessException("Fiscal year " + fiscalYear + " is not closed.");
        }

        // 1. Chronological Safeguard: Prevent reopen if a later fiscal year is closed
        String checkJpql = "SELECT COUNT(fy) FROM FiscalYear fy " +
                           "WHERE fy.company.id = :companyId " +
                           "  AND fy.fiscalYear > :fiscalYear " +
                           "  AND fy.status = 'CLOSED'";
        Long closedLaterCount = entityManager.createQuery(checkJpql, Long.class)
                .setParameter("companyId", companyId)
                .setParameter("fiscalYear", fiscalYear)
                .getSingleResult();
        if (closedLaterCount > 0) {
            throw new BusinessException("Cannot reopen fiscal year " + fiscalYear + 
                    ". A subsequent fiscal year is currently closed. Reopen later years first.");
        }

        // 2. Audit Trail: Generate and post an Automatic Reversing Journal
        JournalEntry originalClosingJE = fy.getClosingJournal();
        if (originalClosingJE != null) {
            Long nextSeq = journalEntryRepository.getNextSequenceValue();
            String jeNumber = String.format("CL-REV-%d-%d-%06d", companyId, fiscalYear, nextSeq);

            JournalEntry reversalJE = JournalEntry.builder()
                    .entryNumber(jeNumber)
                    .company(company)
                    .entryDate(fy.getEndDate())
                    .description("Reversal of Fiscal Year Closing Journal " + fiscalYear)
                    .sourceModule("FISCAL_CLOSE")
                    .sourceReference("FY-" + fiscalYear)
                    .status("POSTED")
                    .closingEntry(true)
                    .closingType(ClosingEntryType.REOPEN_REVERSAL)
                    .reversalEntry(originalClosingJE)
                    .postedAt(LocalDateTime.now())
                    .createdBy(getCurrentUser())
                    .currencyCode("AED")
                    .lines(new ArrayList<>())
                    .build();

            for (JournalEntryLine originalLine : originalClosingJE.getLines()) {
                JournalEntryLine reversalLine = JournalEntryLine.builder()
                        .journalEntry(reversalJE)
                        .account(originalLine.getAccount())
                        .debitAmount(originalLine.getCreditAmount()) // Swap debit & credit
                        .creditAmount(originalLine.getDebitAmount())
                        .build();
                reversalJE.getLines().add(reversalLine);
            }

            journalEntryRepository.save(reversalJE);
        }

        // 3. Mark FiscalYear status as OPEN and clear audit fields
        fy.setStatus(FiscalYearStatus.OPEN);
        fy.setClosedBy(null);
        fy.setClosedAt(null);
        fiscalYearRepository.save(fy);

        // 4. Update the period lock to the end of the previous fiscal year (if any closed year exists)
        LocalDate prevEndDate = fy.getStartDate().minusDays(1);
        PeriodLock lock = periodLockRepository.findByCompanyId(companyId)
                .orElseGet(() -> PeriodLock.builder().company(company).build());
        lock.setLockDate(prevEndDate);
        lock.setLockedBy(getCurrentUserEmail());
        lock.setLockedAt(LocalDateTime.now());
        lock.setReason("Reopened fiscal year " + fiscalYear);
        periodLockRepository.save(lock);
    }

    private LocalDate getFiscalYearStartDate(Company company, int fiscalYear) {
        int month = company.getFiscalYearStartMonth() != null ? company.getFiscalYearStartMonth() : 1;
        int day = company.getFiscalYearStartDay() != null ? company.getFiscalYearStartDay() : 1;
        try {
            return LocalDate.of(fiscalYear, month, day);
        } catch (java.time.DateTimeException e) {
            throw new BusinessException("Invalid fiscal year start date combination: Month " + month + ", Day " + day + " for year " + fiscalYear);
        }
    }

    private LocalDate getFiscalYearEndDate(Company company, int fiscalYear) {
        LocalDate startDate = getFiscalYearStartDate(company, fiscalYear);
        return startDate.plusYears(1).minusDays(1);
    }

    private LocalDate getFiscalYearStartDateForDate(Company company, LocalDate date) {
        int month = company.getFiscalYearStartMonth() != null ? company.getFiscalYearStartMonth() : 1;
        int day = company.getFiscalYearStartDay() != null ? company.getFiscalYearStartDay() : 1;
        try {
            LocalDate candidate = LocalDate.of(date.getYear(), month, day);
            if (!date.isBefore(candidate)) {
                return candidate;
            } else {
                return candidate.minusYears(1);
            }
        } catch (java.time.DateTimeException e) {
            throw new BusinessException("Invalid fiscal year start date combination: Month " + month + ", Day " + day);
        }
    }

    private boolean isDebitNormal(String accountType) {
        return accountType.equals("ASSET") || accountType.equals("EXPENSE");
    }

    private String getCurrentUserEmail() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || auth.getName() == null) {
            return "system@plus33.com";
        }
        return auth.getName();
    }

    private User getCurrentUser() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null) {
            return userRepository.findByEmail("admin@plus33.com")
                    .orElseThrow(() -> new ResourceNotFoundException("Default admin user not found"));
        }
        return userRepository.findByEmail(auth.getName())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + auth.getName()));
    }

    private void validateCompanyFiscalCalendar(Company company) {
        int month = company.getFiscalYearStartMonth() != null ? company.getFiscalYearStartMonth() : 1;
        int day = company.getFiscalYearStartDay() != null ? company.getFiscalYearStartDay() : 1;
        try {
            LocalDate.of(2024, month, day); // 2024 is a leap year to allow Feb 29
        } catch (java.time.DateTimeException e) {
            throw new BusinessException("Invalid fiscal year start date combination: Month " + month + ", Day " + day);
        }
    }
}