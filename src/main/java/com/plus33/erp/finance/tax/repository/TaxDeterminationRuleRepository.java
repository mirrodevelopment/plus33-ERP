/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.tax.repository
 * File              : TaxDeterminationRuleRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TaxDeterminationRuleController
 * Related Service   : TaxDeterminationRuleService, TaxDeterminationRuleServiceImpl
 * Related Repository: TaxDeterminationRuleRepository
 * Related Entity    : TaxDeterminationRule
 * Related DTO       : N/A
 * Related Mapper    : TaxDeterminationRuleMapper
 * Related DB Table  : tax_determination_rules
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : TaxDeterminationRuleService, TaxDeterminationRuleServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'tax_determination_rules' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.tax.repository;

import com.plus33.erp.finance.tax.entity.TaxDeterminationRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code TaxDeterminationRuleRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.tax.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'tax_determination_rules' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code tax_determination_rules}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface TaxDeterminationRuleRepository extends JpaRepository<TaxDeterminationRule, Long> {
    @Query("SELECT r FROM TaxDeterminationRule r WHERE r.company.id = :companyId AND r.active = true " +
           "AND r.effectiveFrom <= :date AND (r.effectiveTo IS NULL OR r.effectiveTo >= :date) " +
           "AND r.documentType = :documentType " +
           "AND (r.customerTaxProfile IS NULL OR r.customerTaxProfile = :customerTaxProfile) " +
           "AND (r.supplierTaxProfile IS NULL OR r.supplierTaxProfile = :supplierTaxProfile) " +
           "AND (r.productTaxCategory IS NULL OR r.productTaxCategory = :productTaxCategory) " +
           "AND (r.originCountry IS NULL OR r.originCountry = :originCountry) " +
           "AND (r.originState IS NULL OR r.originState = :originState) " +
           "AND (r.destCountry IS NULL OR r.destCountry = :destCountry) " +
           "AND (r.destState IS NULL OR r.destState = :destState) " +
           "AND (r.incoterms IS NULL OR r.incoterms = :incoterms) " +
           "ORDER BY r.priority ASC")
    List<TaxDeterminationRule> findMatchingRules(
            @Param("companyId") Long companyId,
            @Param("documentType") String documentType,
            @Param("customerTaxProfile") String customerTaxProfile,
            @Param("supplierTaxProfile") String supplierTaxProfile,
            @Param("productTaxCategory") String productTaxCategory,
            @Param("originCountry") String originCountry,
            @Param("originState") String originState,
            @Param("destCountry") String destCountry,
            @Param("destState") String destState,
            @Param("incoterms") String incoterms,
            @Param("date") LocalDate date
    );
}