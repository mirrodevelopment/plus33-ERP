/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.repository
 * File              : WarehouseWorkflowRepository.java
 * Purpose           : JPA Repository providing database CRUD for Wms Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: WarehouseWorkflowController
 * Related Service   : WarehouseWorkflowService, WarehouseWorkflowServiceImpl
 * Related Repository: WarehouseWorkflowRepository
 * Related Entity    : WarehouseWorkflow
 * Related DTO       : N/A
 * Related Mapper    : WarehouseWorkflowMapper
 * Related DB Table  : warehouse_workflows
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : WarehouseWorkflowService, WarehouseWorkflowServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Wms Module against the 'warehouse_workflows' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.wms.repository;

import com.plus33.erp.wms.entity.WarehouseWorkflow;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code WarehouseWorkflowRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'warehouse_workflows' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code warehouse_workflows}</p>
 * <p><b>Module Deps      :</b> Wms</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface WarehouseWorkflowRepository extends JpaRepository<WarehouseWorkflow, Long> {
    Optional<WarehouseWorkflow> findByCompanyIdAndWorkflowCode(Long companyId, String workflowCode);
}
