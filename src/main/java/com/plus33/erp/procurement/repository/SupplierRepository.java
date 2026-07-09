/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Procurement Module
 * Package           : com.plus33.erp.procurement.repository
 * File              : SupplierRepository.java
 * Purpose           : JPA Repository providing database CRUD for Procurement Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SupplierController
 * Related Service   : SupplierService, SupplierServiceImpl
 * Related Repository: SupplierRepository
 * Related Entity    : Supplier
 * Related DTO       : N/A
 * Related Mapper    : SupplierMapper
 * Related DB Table  : suppliers
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : SupplierService, SupplierServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Procurement Module against the 'suppliers' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.procurement.repository;

import com.plus33.erp.procurement.entity.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Procurement Module</b>
 *
 * <p><b>Class  :</b> {@code SupplierRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.procurement.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'suppliers' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code suppliers}</p>
 * <p><b>Module Deps      :</b> Procurement</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface SupplierRepository extends JpaRepository<Supplier, Long>, JpaSpecificationExecutor<Supplier> {
    Optional<Supplier> findByCode(String code);
    boolean existsByCompanyIdAndCode(Long companyId, String code);
    boolean existsByCompanyIdAndEmail(Long companyId, String email);
    Page<Supplier> findByCompanyIdAndActiveTrue(Long companyId, Pageable pageable);
    Page<Supplier> findByCompanyId(Long companyId, Pageable pageable);
}
