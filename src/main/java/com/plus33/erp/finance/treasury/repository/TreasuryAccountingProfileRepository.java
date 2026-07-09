/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.repository
 * File              : TreasuryAccountingProfileRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TreasuryAccountingProfileController
 * Related Service   : TreasuryAccountingProfileService, TreasuryAccountingProfileServiceImpl
 * Related Repository: TreasuryAccountingProfileRepository
 * Related Entity    : TreasuryAccountingProfile
 * Related DTO       : N/A
 * Related Mapper    : TreasuryAccountingProfileMapper
 * Related DB Table  : treasury_accounting_profiles
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : TreasuryAccountingProfileService, TreasuryAccountingProfileServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'treasury_accounting_profiles' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.repository;

import com.plus33.erp.finance.treasury.entity.TreasuryAccountingProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code TreasuryAccountingProfileRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.treasury.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'treasury_accounting_profiles' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code treasury_accounting_profiles}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface TreasuryAccountingProfileRepository extends JpaRepository<TreasuryAccountingProfile, Long> {

    Optional<TreasuryAccountingProfile> findByCompanyIdAndProfileCode(Long companyId, String profileCode);

    Optional<TreasuryAccountingProfile> findFirstByCompanyIdOrderByProfileCodeAsc(Long companyId);
}