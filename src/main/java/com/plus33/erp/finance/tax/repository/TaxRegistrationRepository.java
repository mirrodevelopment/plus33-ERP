/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.tax.repository
 * File              : TaxRegistrationRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TaxRegistrationController
 * Related Service   : TaxRegistrationService, TaxRegistrationServiceImpl
 * Related Repository: TaxRegistrationRepository
 * Related Entity    : TaxRegistration
 * Related DTO       : N/A
 * Related Mapper    : TaxRegistrationMapper
 * Related DB Table  : tax_registrations
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : TaxRegistrationService, TaxRegistrationServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'tax_registrations' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.tax.repository;

import com.plus33.erp.finance.tax.entity.TaxRegistration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code TaxRegistrationRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.tax.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'tax_registrations' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code tax_registrations}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface TaxRegistrationRepository extends JpaRepository<TaxRegistration, Long> {
    List<TaxRegistration> findByEntityTypeAndEntityIdAndActiveTrue(String entityType, Long entityId);
    
    Optional<TaxRegistration> findByEntityTypeAndEntityIdAndTaxSchemeAndActiveTrue(String entityType, Long entityId, String taxScheme);
}