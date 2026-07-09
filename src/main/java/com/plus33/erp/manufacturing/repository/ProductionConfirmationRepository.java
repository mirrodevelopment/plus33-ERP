/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.repository
 * File              : ProductionConfirmationRepository.java
 * Purpose           : JPA Repository providing database CRUD for Manufacturing Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProductionConfirmationController
 * Related Service   : ProductionConfirmationService, ProductionConfirmationServiceImpl
 * Related Repository: ProductionConfirmationRepository
 * Related Entity    : ProductionConfirmation
 * Related DTO       : N/A
 * Related Mapper    : ProductionConfirmationMapper
 * Related DB Table  : production_confirmations
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ProductionConfirmationService, ProductionConfirmationServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Manufacturing Module against the 'production_confirmations' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.manufacturing.repository;

import com.plus33.erp.manufacturing.entity.ProductionConfirmation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code ProductionConfirmationRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'production_confirmations' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code production_confirmations}</p>
 * <p><b>Module Deps      :</b> Manufacturing</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface ProductionConfirmationRepository extends JpaRepository<ProductionConfirmation, Long> {
    List<ProductionConfirmation> findByProductionOrderId(Long productionOrderId);
    List<ProductionConfirmation> findByProductionOrderOperationId(Long productionOrderOperationId);
}
