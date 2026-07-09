/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.repository
 * File              : ProductionCostRepository.java
 * Purpose           : JPA Repository providing database CRUD for Manufacturing Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProductionCostController
 * Related Service   : ProductionCostService, ProductionCostServiceImpl
 * Related Repository: ProductionCostRepository
 * Related Entity    : ProductionCost
 * Related DTO       : N/A
 * Related Mapper    : ProductionCostMapper
 * Related DB Table  : production_costs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ProductionCostService, ProductionCostServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Manufacturing Module against the 'production_costs' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.manufacturing.repository;

import com.plus33.erp.manufacturing.entity.ProductionCost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code ProductionCostRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'production_costs' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code production_costs}</p>
 * <p><b>Module Deps      :</b> Manufacturing</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface ProductionCostRepository extends JpaRepository<ProductionCost, Long> {

    Optional<ProductionCost> findByProductionOrderId(Long productionOrderId);

    @Query("""
        SELECT pc FROM ProductionCost pc
        WHERE pc.productionOrder.companyId = :companyId
          AND pc.status = 'IN_PROGRESS'
    """)
    java.util.List<ProductionCost> findOpenCostsByCompany(@Param("companyId") Long companyId);
}