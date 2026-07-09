/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.repository
 * File              : WarehouseRetentionPolicyRepository.java
 * Purpose           : JPA Repository providing database CRUD for Wms Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: WarehouseRetentionPolicyController
 * Related Service   : WarehouseRetentionPolicyService, WarehouseRetentionPolicyServiceImpl
 * Related Repository: WarehouseRetentionPolicyRepository
 * Related Entity    : WarehouseRetentionPolicy
 * Related DTO       : N/A
 * Related Mapper    : WarehouseRetentionPolicyMapper
 * Related DB Table  : warehouse_retention_policys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : WarehouseRetentionPolicyService, WarehouseRetentionPolicyServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Wms Module against the 'warehouse_retention_policys' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.wms.repository;

import com.plus33.erp.wms.entity.WarehouseRetentionPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code WarehouseRetentionPolicyRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'warehouse_retention_policys' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code warehouse_retention_policys}</p>
 * <p><b>Module Deps      :</b> Wms</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface WarehouseRetentionPolicyRepository extends JpaRepository<WarehouseRetentionPolicy, Long> {
    Optional<WarehouseRetentionPolicy> findByCompanyIdAndEntityName(Long companyId, String entityName);
}
