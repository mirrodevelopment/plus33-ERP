/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.tax.repository
 * File              : TaxPostingProfileRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TaxPostingProfileController
 * Related Service   : TaxPostingProfileService, TaxPostingProfileServiceImpl
 * Related Repository: TaxPostingProfileRepository
 * Related Entity    : TaxPostingProfile
 * Related DTO       : N/A
 * Related Mapper    : TaxPostingProfileMapper
 * Related DB Table  : tax_posting_profiles
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : TaxPostingProfileService, TaxPostingProfileServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'tax_posting_profiles' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.tax.repository;

import com.plus33.erp.finance.tax.entity.TaxPostingProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code TaxPostingProfileRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.tax.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'tax_posting_profiles' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code tax_posting_profiles}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface TaxPostingProfileRepository extends JpaRepository<TaxPostingProfile, Long> {
    Optional<TaxPostingProfile> findByCompanyIdAndCategoryId(Long companyId, Long categoryId);
    Optional<TaxPostingProfile> findByCompanyIdAndCategoryCode(Long companyId, String categoryCode);
}