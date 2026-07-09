/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.repository
 * File              : TreasuryLimitRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TreasuryLimitController
 * Related Service   : TreasuryLimitService, TreasuryLimitServiceImpl
 * Related Repository: TreasuryLimitRepository
 * Related Entity    : TreasuryLimit
 * Related DTO       : N/A
 * Related Mapper    : TreasuryLimitMapper
 * Related DB Table  : treasury_limits
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : TreasuryLimitService, TreasuryLimitServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'treasury_limits' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.repository;

import com.plus33.erp.finance.treasury.entity.TreasuryLimit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code TreasuryLimitRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.treasury.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'treasury_limits' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code treasury_limits}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface TreasuryLimitRepository extends JpaRepository<TreasuryLimit, Long> {
    List<TreasuryLimit> findByCompanyId(Long companyId);
    Optional<TreasuryLimit> findByCompanyIdAndLimitTypeAndCurrencyCodeAndCountryCodeAndTargetBankId(
            Long companyId, String limitType, String currencyCode, String countryCode, Long targetBankId);
}