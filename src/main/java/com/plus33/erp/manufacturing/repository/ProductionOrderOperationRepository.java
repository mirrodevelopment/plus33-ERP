/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.repository
 * File              : ProductionOrderOperationRepository.java
 * Purpose           : JPA Repository providing database CRUD for Manufacturing Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProductionOrderOperationController
 * Related Service   : ProductionOrderOperationService, ProductionOrderOperationServiceImpl
 * Related Repository: ProductionOrderOperationRepository
 * Related Entity    : ProductionOrderOperation
 * Related DTO       : N/A
 * Related Mapper    : ProductionOrderOperationMapper
 * Related DB Table  : production_order_operations
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ProductionOrderOperationService, ProductionOrderOperationServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Manufacturing Module against the 'production_order_operations' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.manufacturing.repository;

import com.plus33.erp.manufacturing.entity.ProductionOrderOperation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code ProductionOrderOperationRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'production_order_operations' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code production_order_operations}</p>
 * <p><b>Module Deps      :</b> Manufacturing</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface ProductionOrderOperationRepository extends JpaRepository<ProductionOrderOperation, Long> {
    List<ProductionOrderOperation> findByProductionOrderId(Long productionOrderId);
}
