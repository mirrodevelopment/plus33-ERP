/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.tax.service
 * File              : TaxPostingProfileServiceImpl.java
 * Purpose           : Business logic service layer for Finance Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TaxPostingProfileController
 * Related Service   : TaxPostingProfileServiceImpl
 * Related Repository: TaxPostingProfileRepository
 * Related Entity    : TaxPostingProfile
 * Related DTO       : N/A
 * Related Mapper    : TaxPostingProfileMapper
 * Related DB Table  : tax_posting_profiles
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : TaxPostingProfileController, TaxPostingProfileServiceImplImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Finance Module. Implements TaxPostingProfileService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.finance.tax.service;

import com.plus33.erp.finance.tax.entity.TaxPostingProfile;
import com.plus33.erp.finance.tax.repository.TaxPostingProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code TaxPostingProfileServiceImpl}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.tax.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Finance Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * TaxPostingProfileController
 *   --> TaxPostingProfileServiceImpl (this)
 *   --> Validate business rules
 *   --> TaxPostingProfileRepository (read/write 'tax_posting_profiles')
 *   --> TaxPostingProfileMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code tax_posting_profiles}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class TaxPostingProfileServiceImpl implements TaxPostingProfileService {

    private final TaxPostingProfileRepository postingProfileRepository;

    /**
     * Retrieves posting profile data from the database.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param categoryId the categoryId input value
     * @return the TaxPostingProfile result
     * @throws ResourceNotFoundException if the entity is not found
     */
    /**
     * Retrieves posting profile data from the database.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param categoryId the categoryId input value
     * @return the TaxPostingProfile result
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    public TaxPostingProfile getPostingProfile(Long companyId, Long categoryId) {
        return postingProfileRepository.findByCompanyIdAndCategoryId(companyId, categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Tax Posting Profile not found for company " + companyId + " and category " + categoryId));
    }

    /**
     * Retrieves posting profile by category code data from the database.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param categoryCode the categoryCode input value
     * @return the TaxPostingProfile result
     * @throws ResourceNotFoundException if the entity is not found
     */
    /**
     * Retrieves posting profile by category code data from the database.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param categoryCode the categoryCode input value
     * @return the TaxPostingProfile result
     * @throws ResourceNotFoundException if the entity is not found
     */
    @Override
    public TaxPostingProfile getPostingProfileByCategoryCode(Long companyId, String categoryCode) {
        return postingProfileRepository.findByCompanyIdAndCategoryCode(companyId, categoryCode)
                .orElseThrow(() -> new IllegalArgumentException("Tax Posting Profile not found for company " + companyId + " and category code " + categoryCode));
    }
}