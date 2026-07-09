/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Organization Module
 * Package           : com.plus33.erp.organization.repository
 * File              : StoreRepository.java
 * Purpose           : JPA Repository providing database CRUD for Organization Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: StoreController
 * Related Service   : StoreService, StoreServiceImpl
 * Related Repository: StoreRepository
 * Related Entity    : Store
 * Related DTO       : N/A
 * Related Mapper    : StoreMapper
 * Related DB Table  : stores
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : StoreService, StoreServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Organization Module against the 'stores' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.organization.repository;

import com.plus33.erp.organization.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Organization Module</b>
 *
 * <p><b>Class  :</b> {@code StoreRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.organization.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'stores' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code stores}</p>
 * <p><b>Module Deps      :</b> Organization</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface StoreRepository extends JpaRepository<Store, Long>, JpaSpecificationExecutor<Store> {
    Optional<Store> findByCode(String code);
    boolean existsByCode(String code);
    boolean existsByRegionCompanyIdAndCode(Long companyId, String code);
    boolean existsByRegionIdAndActiveTrue(Long regionId);
    boolean existsByWarehouseIdAndActiveTrue(Long warehouseId);
}
