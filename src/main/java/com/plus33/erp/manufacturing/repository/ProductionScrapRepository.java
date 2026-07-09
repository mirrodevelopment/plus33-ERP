/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.repository
 * File              : ProductionScrapRepository.java
 * Purpose           : JPA Repository providing database CRUD for Manufacturing Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProductionScrapController
 * Related Service   : ProductionScrapService, ProductionScrapServiceImpl
 * Related Repository: ProductionScrapRepository
 * Related Entity    : ProductionScrap
 * Related DTO       : N/A
 * Related Mapper    : ProductionScrapMapper
 * Related DB Table  : production_scraps
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ProductionScrapService, ProductionScrapServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Manufacturing Module against the 'production_scraps' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.manufacturing.repository;

import com.plus33.erp.manufacturing.entity.ProductionScrap;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code ProductionScrapRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'production_scraps' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code production_scraps}</p>
 * <p><b>Module Deps      :</b> Manufacturing</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface ProductionScrapRepository extends JpaRepository<ProductionScrap, Long> {
    List<ProductionScrap> findByProductionOrderId(Long productionOrderId);
    List<ProductionScrap> findByProductionOrderOperationId(Long productionOrderOperationId);
}
