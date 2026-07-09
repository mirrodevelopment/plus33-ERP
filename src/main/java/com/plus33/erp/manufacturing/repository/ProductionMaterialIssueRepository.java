/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.repository
 * File              : ProductionMaterialIssueRepository.java
 * Purpose           : JPA Repository providing database CRUD for Manufacturing Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProductionMaterialIssueController
 * Related Service   : ProductionMaterialIssueService, ProductionMaterialIssueServiceImpl
 * Related Repository: ProductionMaterialIssueRepository
 * Related Entity    : ProductionMaterialIssue
 * Related DTO       : N/A
 * Related Mapper    : ProductionMaterialIssueMapper
 * Related DB Table  : production_material_issues
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ProductionMaterialIssueService, ProductionMaterialIssueServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Manufacturing Module against the 'production_material_issues' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.manufacturing.repository;

import com.plus33.erp.manufacturing.entity.ProductionMaterialIssue;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code ProductionMaterialIssueRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'production_material_issues' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code production_material_issues}</p>
 * <p><b>Module Deps      :</b> Manufacturing</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface ProductionMaterialIssueRepository extends JpaRepository<ProductionMaterialIssue, Long> {
    List<ProductionMaterialIssue> findByProductionOrderId(Long productionOrderId);
    List<ProductionMaterialIssue> findByProductionOrderIdAndBomLineId(Long productionOrderId, Long bomLineId);
}
