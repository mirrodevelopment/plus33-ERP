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
