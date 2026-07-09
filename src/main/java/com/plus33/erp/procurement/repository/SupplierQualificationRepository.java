/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Procurement Module
 * Package           : com.plus33.erp.procurement.repository
 * File              : SupplierQualificationRepository.java
 * Purpose           : JPA Repository providing database CRUD for Procurement Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SupplierQualificationController
 * Related Service   : SupplierQualificationService, SupplierQualificationServiceImpl
 * Related Repository: SupplierQualificationRepository
 * Related Entity    : SupplierQualification
 * Related DTO       : N/A
 * Related Mapper    : SupplierQualificationMapper
 * Related DB Table  : supplier_qualifications
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : SupplierQualificationService, SupplierQualificationServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Procurement Module against the 'supplier_qualifications' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.procurement.repository;

import com.plus33.erp.procurement.entity.SupplierQualification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Procurement Module</b>
 *
 * <p><b>Class  :</b> {@code SupplierQualificationRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.procurement.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'supplier_qualifications' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code supplier_qualifications}</p>
 * <p><b>Module Deps      :</b> Procurement</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface SupplierQualificationRepository extends JpaRepository<SupplierQualification, Long> {
    Optional<SupplierQualification> findBySupplierId(Long supplierId);
}
