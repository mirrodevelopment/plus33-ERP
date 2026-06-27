package com.plus33.erp.finance.tax.service;

import com.plus33.erp.finance.entity.Account;
import com.plus33.erp.finance.repository.AccountRepository;
import com.plus33.erp.finance.tax.entity.TaxAdjustmentEntry;
import com.plus33.erp.finance.tax.entity.TaxCategory;
import com.plus33.erp.finance.tax.repository.TaxAdjustmentEntryRepository;
import com.plus33.erp.finance.tax.repository.TaxCategoryRepository;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TaxAdjustmentServiceImpl implements TaxAdjustmentService {

    private final TaxAdjustmentEntryRepository adjustmentEntryRepository;
    private final CompanyRepository companyRepository;
    private final TaxCategoryRepository taxCategoryRepository;
    private final AccountRepository accountRepository;

    @Override
    public TaxAdjustmentEntry createAdjustment(
            Long companyId,
            LocalDate date,
            Long taxCategoryId,
            Long glAccountId,
            BigDecimal debitAmount,
            BigDecimal creditAmount,
            String description,
            String reasonCode
    ) {
        Company company = companyRepository.findById(companyId)
                .orElseThrow(() -> new IllegalArgumentException("Company not found: " + companyId));

        TaxCategory category = taxCategoryRepository.findById(taxCategoryId)
                .orElseThrow(() -> new IllegalArgumentException("Tax Category not found: " + taxCategoryId));

        Account account = accountRepository.findById(glAccountId)
                .orElseThrow(() -> new IllegalArgumentException("GL Account not found: " + glAccountId));

        TaxAdjustmentEntry entry = TaxAdjustmentEntry.builder()
                .company(company)
                .adjustmentDate(date)
                .taxCategory(category)
                .glAccount(account)
                .debitAmount(debitAmount != null ? debitAmount : BigDecimal.ZERO)
                .creditAmount(creditAmount != null ? creditAmount : BigDecimal.ZERO)
                .description(description)
                .reasonCode(reasonCode)
                .status("DRAFT")
                .build();

        return adjustmentEntryRepository.save(entry);
    }

    @Override
    public TaxAdjustmentEntry approveAdjustment(Long adjustmentId, String approvedBy) {
        TaxAdjustmentEntry entry = adjustmentEntryRepository.findById(adjustmentId)
                .orElseThrow(() -> new IllegalArgumentException("Tax Adjustment not found: " + adjustmentId));

        if (!"DRAFT".equals(entry.getStatus())) {
            throw new IllegalStateException("Only DRAFT adjustments can be approved");
        }

        entry.setStatus("APPROVED");
        entry.setApprovedBy(approvedBy);
        entry.setApprovedAt(LocalDateTime.now());

        return adjustmentEntryRepository.save(entry);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TaxAdjustmentEntry> getAdjustmentsByCompany(Long companyId) {
        return adjustmentEntryRepository.findByCompanyId(companyId);
    }
}
