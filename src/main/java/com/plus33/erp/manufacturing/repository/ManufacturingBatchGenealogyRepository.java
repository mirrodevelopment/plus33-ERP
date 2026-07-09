/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Manufacturing Module
 * Package           : com.plus33.erp.manufacturing.repository
 * File              : ManufacturingBatchGenealogyRepository.java
 * Purpose           : JPA Repository providing database CRUD for Manufacturing Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ManufacturingBatchGenealogyController
 * Related Service   : ManufacturingBatchGenealogyService, ManufacturingBatchGenealogyServiceImpl
 * Related Repository: ManufacturingBatchGenealogyRepository
 * Related Entity    : ManufacturingBatchGenealogy
 * Related DTO       : N/A
 * Related Mapper    : ManufacturingBatchGenealogyMapper
 * Related DB Table  : manufacturing_batch_genealogys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ManufacturingBatchGenealogyService, ManufacturingBatchGenealogyServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Manufacturing Module against the 'manufacturing_batch_genealogys' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.manufacturing.repository;

import com.plus33.erp.manufacturing.entity.ManufacturingBatchGenealogy;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Manufacturing Module</b>
 *
 * <p><b>Class  :</b> {@code ManufacturingBatchGenealogyRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.manufacturing.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'manufacturing_batch_genealogys' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code manufacturing_batch_genealogys}</p>
 * <p><b>Module Deps      :</b> Manufacturing</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface ManufacturingBatchGenealogyRepository extends JpaRepository<ManufacturingBatchGenealogy, Long> {
    List<ManufacturingBatchGenealogy> findByProductionOrderId(Long productionOrderId);
    List<ManufacturingBatchGenealogy> findByBatchNumber(String batchNumber);
    List<ManufacturingBatchGenealogy> findByParentBatchNumber(String parentBatchNumber);
}
