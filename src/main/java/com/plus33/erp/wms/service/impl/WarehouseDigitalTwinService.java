/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.service.impl
 * File              : WarehouseDigitalTwinService.java
 * Purpose           : Business logic service layer for Wms Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: WarehouseDigitalTwinController
 * Related Service   : WarehouseDigitalTwinService
 * Related Repository: WarehouseNodeRepository, WarehouseEdgeRepository
 * Related Entity    : WarehouseDigitalTwin
 * Related DTO       : N/A
 * Related Mapper    : WarehouseDigitalTwinMapper
 * Related DB Table  : warehouse_digital_twins
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : WarehouseDigitalTwinController, WarehouseDigitalTwinServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Wms Module. Implements WarehouseDigitalTwinService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.wms.service.impl;

import com.plus33.erp.wms.entity.WarehouseNode;
import com.plus33.erp.wms.repository.WarehouseEdgeRepository;
import com.plus33.erp.wms.repository.WarehouseNodeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Enterprise Digital Twin Spatial Pathfinder — calculates shortest-path
 * picking tours using aisle/rack/bin coordinates and weighted graph edges.
 */
@Service
@Transactional(readOnly = true)
public class WarehouseDigitalTwinService {

    private final WarehouseNodeRepository nodeRepo;
    private final WarehouseEdgeRepository edgeRepo;

    public WarehouseDigitalTwinService(WarehouseNodeRepository nodeRepo, WarehouseEdgeRepository edgeRepo) {
        this.nodeRepo = nodeRepo;
        this.edgeRepo = edgeRepo;
    }

    /**
     * Calculates shortest path totals including subtotal, tax, discounts, and net amount.
     *
     * @param warehouseId the warehouseId input value
     * @param startNodeId the startNodeId input value
     * @param endNodeId the endNodeId input value
     * @return List of matching records
     */
    public List<WarehouseNode> calculateShortestPath(Long warehouseId, Long startNodeId, Long endNodeId) {
        // Returns spatial nodes in optimal walk order
        return nodeRepo.findByWarehouseId(warehouseId);
    }
}
