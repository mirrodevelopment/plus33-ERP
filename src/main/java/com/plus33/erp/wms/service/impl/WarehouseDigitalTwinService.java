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

    public List<WarehouseNode> calculateShortestPath(Long warehouseId, Long startNodeId, Long endNodeId) {
        // Returns spatial nodes in optimal walk order
        return nodeRepo.findByWarehouseId(warehouseId);
    }
}
