/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.repository
 * File              : WarehouseNodeRepository.java
 * Purpose           : JPA Repository providing database CRUD for Wms Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: WarehouseNodeController
 * Related Service   : WarehouseNodeService, WarehouseNodeServiceImpl
 * Related Repository: WarehouseNodeRepository
 * Related Entity    : WarehouseNode
 * Related DTO       : N/A
 * Related Mapper    : WarehouseNodeMapper
 * Related DB Table  : warehouse_nodes
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : WarehouseNodeService, WarehouseNodeServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Wms Module against the 'warehouse_nodes' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.wms.repository;

import com.plus33.erp.wms.entity.WarehouseNode;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code WarehouseNodeRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'warehouse_nodes' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code warehouse_nodes}</p>
 * <p><b>Module Deps      :</b> Wms</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface WarehouseNodeRepository extends JpaRepository<WarehouseNode, Long> {
    List<WarehouseNode> findByWarehouseId(Long warehouseId);
    Optional<WarehouseNode> findByLocationId(Long locationId);
}
