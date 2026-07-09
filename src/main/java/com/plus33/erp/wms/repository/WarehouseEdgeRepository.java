/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.repository
 * File              : WarehouseEdgeRepository.java
 * Purpose           : JPA Repository providing database CRUD for Wms Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: WarehouseEdgeController
 * Related Service   : WarehouseEdgeService, WarehouseEdgeServiceImpl
 * Related Repository: WarehouseEdgeRepository
 * Related Entity    : WarehouseEdge
 * Related DTO       : N/A
 * Related Mapper    : WarehouseEdgeMapper
 * Related DB Table  : warehouse_edges
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : WarehouseEdgeService, WarehouseEdgeServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Wms Module against the 'warehouse_edges' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.wms.repository;

import com.plus33.erp.wms.entity.WarehouseEdge;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code WarehouseEdgeRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'warehouse_edges' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code warehouse_edges}</p>
 * <p><b>Module Deps      :</b> Wms</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface WarehouseEdgeRepository extends JpaRepository<WarehouseEdge, Long> {
    List<WarehouseEdge> findByWarehouseId(Long warehouseId);
}
