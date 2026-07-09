/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.repository
 * File              : ProductionReworkRepository.java
 * Purpose           : JPA Repository providing database CRUD for Manufacturing Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProductionReworkController
 * Related Service   : ProductionReworkService, ProductionReworkServiceImpl
 * Related Repository: ProductionReworkRepository
 * Related Entity    : ProductionRework
 * Related DTO       : N/A
 * Related Mapper    : ProductionReworkMapper
 * Related DB Table  : production_reworks
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ProductionReworkService, ProductionReworkServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Manufacturing Module against the 'production_reworks' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.manufacturing.repository;

import com.plus33.erp.manufacturing.entity.ProductionRework;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code ProductionReworkRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'production_reworks' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code production_reworks}</p>
 * <p><b>Module Deps      :</b> Manufacturing</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface ProductionReworkRepository extends JpaRepository<ProductionRework, Long> {
    List<ProductionRework> findByOriginalProductionOrderId(Long originalProductionOrderId);
    List<ProductionRework> findByReworkProductionOrderId(Long reworkProductionOrderId);
}
