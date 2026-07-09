/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.tax.repository
 * File              : TaxOverrideRequestRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TaxOverrideRequestController
 * Related Service   : TaxOverrideRequestService, TaxOverrideRequestServiceImpl
 * Related Repository: TaxOverrideRequestRepository
 * Related Entity    : TaxOverrideRequest
 * Related DTO       : TaxOverrideRequest
 * Related Mapper    : TaxOverrideRequestMapper
 * Related DB Table  : tax_override_requests
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : TaxOverrideRequestService, TaxOverrideRequestServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'tax_override_requests' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.tax.repository;

import com.plus33.erp.finance.tax.entity.TaxOverrideRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code TaxOverrideRequestRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.tax.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'tax_override_requests' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code tax_override_requests}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface TaxOverrideRequestRepository extends JpaRepository<TaxOverrideRequest, Long> {
    List<TaxOverrideRequest> findByCompanyId(Long companyId);
    Optional<TaxOverrideRequest> findByCompanyIdAndDocumentTypeAndDocumentId(Long companyId, String documentType, Long documentId);
}