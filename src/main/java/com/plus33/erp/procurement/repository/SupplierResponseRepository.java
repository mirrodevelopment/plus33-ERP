/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Procurement Module
 * Package           : com.plus33.erp.procurement.repository
 * File              : SupplierResponseRepository.java
 * Purpose           : JPA Repository providing database CRUD for Procurement Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SupplierResponseController
 * Related Service   : SupplierResponseService, SupplierResponseServiceImpl
 * Related Repository: SupplierResponseRepository
 * Related Entity    : SupplierResponse
 * Related DTO       : SupplierResponse
 * Related Mapper    : SupplierResponseMapper
 * Related DB Table  : supplier_responses
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : SupplierResponseService, SupplierResponseServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Procurement Module against the 'supplier_responses' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.procurement.repository;

import com.plus33.erp.procurement.entity.SupplierResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Procurement Module</b>
 *
 * <p><b>Class  :</b> {@code SupplierResponseRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.procurement.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'supplier_responses' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code supplier_responses}</p>
 * <p><b>Module Deps      :</b> Procurement</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface SupplierResponseRepository extends JpaRepository<SupplierResponse, Long> {
    List<SupplierResponse> findByRfqVersionId(Long rfqVersionId);
}
