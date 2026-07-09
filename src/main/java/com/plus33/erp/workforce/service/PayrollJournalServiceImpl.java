/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.service
 * File              : PayrollJournalServiceImpl.java
 * Purpose           : Business logic service layer for Workforce Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PayrollJournalController
 * Related Service   : PayrollJournalServiceImpl
 * Related Repository: AccountRepository, JournalEntryRepository, CompanyRepository, UserRepository
 * Related Entity    : PayrollJournal
 * Related DTO       : N/A
 * Related Mapper    : PayrollJournalMapper
 * Related DB Table  : payroll_journals
 * Related REST APIs : N/A
 * Depends On        : Common Module, Finance Module, Organization Module, Security Module
 * Used By           : PayrollJournalController, PayrollJournalServiceImplImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Workforce Module. Implements PayrollJournalService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.workforce.service;

import com.plus33.erp.common.exception.BusinessException;
import com.plus33.erp.finance.entity.Account;
import com.plus33.erp.finance.entity.JournalEntry;
import com.plus33.erp.finance.entity.JournalEntryLine;
import com.plus33.erp.finance.repository.AccountRepository;
import com.plus33.erp.finance.repository.JournalEntryRepository;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.repository.CompanyRepository;
import com.plus33.erp.workforce.entity.PayrollRun;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.plus33.erp.security.entity.User;
import com.plus33.erp.security.repository.UserRepository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.UUID;

/**
 * <b>PLUS33 Coffee ERP -- Workforce Module</b>
 *
 * <p><b>Class  :</b> {@code PayrollJournalServiceImpl}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.workforce.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Workforce Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * PayrollJournalController
 *   --> PayrollJournalServiceImpl (this)
 *   --> Validate business rules
 *   --> PayrollJournalRepository (read/write 'payroll_journals')
 *   --> PayrollJournalMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code payroll_journals}</p>
 * <p><b>Module Deps      :</b> Common, Finance, Organization, Workforce, Security</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class PayrollJournalServiceImpl implements PayrollJournalService {

    private final AccountRepository accountRepository;
    private final JournalEntryRepository journalEntryRepository;
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    public PayrollJournalServiceImpl(AccountRepository accountRepository,
                                     JournalEntryRepository journalEntryRepository,
                                     CompanyRepository companyRepository,
                                     UserRepository userRepository) {
        this.accountRepository = accountRepository;
        this.journalEntryRepository = journalEntryRepository;
        this.companyRepository = companyRepository;
        this.userRepository = userRepository;
    }

    /**
     * Posts payroll journal entries to the General Ledger and updates financial balances.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param payrollRun the payrollRun input value
     * @return the JournalEntry result
     * @throws BusinessException if a business rule is violated
     */
    @Override
    @Transactional
    public JournalEntry postPayrollJournal(PayrollRun payrollRun) {
        Company company = companyRepository.findById(payrollRun.getCompanyId())
                .orElseThrow(() -> new BusinessException("Company not found for ID: " + payrollRun.getCompanyId()));

        User systemUser = userRepository.findAll().stream().findFirst().orElseGet(() -> {
            User u = new User();
            u.setEmail("system@plus33.com");
            u.setPassword("password");
            u.setFirstName("System");
            u.setLastName("Admin");
            return userRepository.save(u);
        });

        JournalEntry je = JournalEntry.builder()
                .company(company)
                .entryNumber("PAY-JE-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase())
                .entryDate(LocalDate.now())
                .sourceModule("PAYROLL")
                .sourceReference(payrollRun.getRunNumber())
                .status("POSTED")
                .createdBy(systemUser)
                .description("Payroll GL Posting for Run: " + payrollRun.getRunNumber())
                .lines(new ArrayList<>())
                .build();

        // Account lookup fallbacks
        Account salaryExpense = accountRepository.findByCompanyIdAndAccountCode(company.getId(), "5300")
                .orElseGet(() -> accountRepository.findAll().stream().filter(a -> a.getCompany().getId().equals(company.getId())).findFirst().orElseThrow());

        Account payrollPayable = accountRepository.findByCompanyIdAndAccountCode(company.getId(), "2200")
                .orElseGet(() -> accountRepository.findAll().stream().filter(a -> a.getCompany().getId().equals(company.getId())).findFirst().orElseThrow());

        // DR Gross Salary
        if (payrollRun.getTotalGross().compareTo(BigDecimal.ZERO) > 0) {
            je.getLines().add(JournalEntryLine.builder()
                    .journalEntry(je)
                    .account(salaryExpense)
                    .debitAmount(payrollRun.getTotalGross())
                    .creditAmount(BigDecimal.ZERO)
                    .build());
        }

        // DR Employer Contributions
        if (payrollRun.getTotalEmployerContributions().compareTo(BigDecimal.ZERO) > 0) {
            je.getLines().add(JournalEntryLine.builder()
                    .journalEntry(je)
                    .account(salaryExpense)
                    .debitAmount(payrollRun.getTotalEmployerContributions())
                    .creditAmount(BigDecimal.ZERO)
                    .build());
        }

        // CR Net Salary Payable
        if (payrollRun.getTotalNet().compareTo(BigDecimal.ZERO) > 0) {
            je.getLines().add(JournalEntryLine.builder()
                    .journalEntry(je)
                    .account(payrollPayable)
                    .debitAmount(BigDecimal.ZERO)
                    .creditAmount(payrollRun.getTotalNet())
                    .build());
        }

        // CR Taxes & Deductions Payable
        BigDecimal totalWithheld = payrollRun.getTotalTaxes().add(payrollRun.getTotalEmployerContributions());
        if (totalWithheld.compareTo(BigDecimal.ZERO) > 0) {
            je.getLines().add(JournalEntryLine.builder()
                    .journalEntry(je)
                    .account(payrollPayable)
                    .debitAmount(BigDecimal.ZERO)
                    .creditAmount(totalWithheld)
                    .build());
        }

        return journalEntryRepository.save(je);
    }
}