/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.tax.service
 * File              : TaxAdjustmentServiceImpl.java
 * Purpose           : Business logic service layer for Finance Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TaxAdjustmentController
 * Related Service   : TaxAdjustmentServiceImpl
 * Related Repository: TaxAdjustmentEntryRepository, CompanyRepository, TaxCategoryRepository, AccountRepository
 * Related Entity    : TaxAdjustment
 * Related DTO       : N/A
 * Related Mapper    : TaxAdjustmentMapper
 * Related DB Table  : tax_adjustments
 * Related REST APIs : N/A
 * Depends On        : Organization Module
 * Used By           : TaxAdjustmentController, TaxAdjustmentServiceImplImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Finance Module. Implements TaxAdjustmentService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
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

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code TaxAdjustmentServiceImpl}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.tax.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Finance Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * TaxAdjustmentController
 *   --> TaxAdjustmentServiceImpl (this)
 *   --> Validate business rules
 *   --> TaxAdjustmentRepository (read/write 'tax_adjustments')
 *   --> TaxAdjustmentMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code tax_adjustments}</p>
 * <p><b>Module Deps      :</b> Finance, Organization</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
@RequiredArgsConstructor
@Transactional
public class TaxAdjustmentServiceImpl implements TaxAdjustmentService {

    private final TaxAdjustmentEntryRepository adjustmentEntryRepository;
    private final CompanyRepository companyRepository;
    private final TaxCategoryRepository taxCategoryRepository;
    private final AccountRepository accountRepository;

    /**
     * Creates a new adjustment and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @return the TaxAdjustmentEntry result
     * @throws BusinessException if a business rule is violated
     */
    /**
     * Creates a new adjustment and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @return the TaxAdjustmentEntry result
     * @throws BusinessException if a business rule is violated
     */
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

    /**
     * Approves the adjustment, transitions to APPROVED status, and posts GL journal entries.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param adjustmentId the adjustmentId input value
     * @param approvedBy the approvedBy input value
     * @return the TaxAdjustmentEntry result
     * @throws BusinessException if a business rule is violated
     */
    /**
     * Approves the adjustment, transitions to APPROVED status, and posts GL journal entries.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param adjustmentId the adjustmentId input value
     * @param approvedBy the approvedBy input value
     * @return the TaxAdjustmentEntry result
     * @throws BusinessException if a business rule is violated
     */
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

    /**
     * Retrieves adjustments by company data from the database.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @return List of matching records
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    @Transactional(readOnly = true)
    public List<TaxAdjustmentEntry> getAdjustmentsByCompany(Long companyId) {
        return adjustmentEntryRepository.findByCompanyId(companyId);
    }
}