/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.repository
 * File              : TreasuryInvestmentRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TreasuryInvestmentController
 * Related Service   : TreasuryInvestmentService, TreasuryInvestmentServiceImpl
 * Related Repository: TreasuryInvestmentRepository
 * Related Entity    : TreasuryInvestment
 * Related DTO       : N/A
 * Related Mapper    : TreasuryInvestmentMapper
 * Related DB Table  : treasury_investments
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : TreasuryInvestmentService, TreasuryInvestmentServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'treasury_investments' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.repository;

import com.plus33.erp.finance.treasury.entity.TreasuryInvestment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code TreasuryInvestmentRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.treasury.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'treasury_investments' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code treasury_investments}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface TreasuryInvestmentRepository extends JpaRepository<TreasuryInvestment, Long> {
    List<TreasuryInvestment> findByBankAccountCompanyId(Long companyId);
    Optional<TreasuryInvestment> findByReferenceNumber(String referenceNumber);
    List<TreasuryInvestment> findByStatus(String status);
}