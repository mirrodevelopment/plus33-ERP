/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Procurement Module
 * Package           : com.plus33.erp.procurement.repository
 * File              : PurchaseRequestRepository.java
 * Purpose           : JPA Repository providing database CRUD for Procurement Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PurchaseRequestController
 * Related Service   : PurchaseRequestService, PurchaseRequestServiceImpl
 * Related Repository: PurchaseRequestRepository
 * Related Entity    : PurchaseRequest
 * Related DTO       : PurchaseRequest
 * Related Mapper    : PurchaseRequestMapper
 * Related DB Table  : purchase_requests
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PurchaseRequestService, PurchaseRequestServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Procurement Module against the 'purchase_requests' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.procurement.repository;

import com.plus33.erp.procurement.entity.PurchaseRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Procurement Module</b>
 *
 * <p><b>Class  :</b> {@code PurchaseRequestRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.procurement.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'purchase_requests' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code purchase_requests}</p>
 * <p><b>Module Deps      :</b> Procurement</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface PurchaseRequestRepository extends JpaRepository<PurchaseRequest, Long>, JpaSpecificationExecutor<PurchaseRequest> {
    Optional<PurchaseRequest> findByRequestNumber(String requestNumber);

    @Query(value = "SELECT nextval('purchase_request_seq')", nativeQuery = true)
    Long getNextSequenceValue();
}
