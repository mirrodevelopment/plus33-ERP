/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.strategy
 * File              : PickingStrategy.java
 * Purpose           : Service interface contract defining the API for Wms Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PickingStrategyController
 * Related Service   : PickingStrategyService, PickingStrategyServiceImpl
 * Related Repository: PickingStrategyRepository
 * Related Entity    : PickingStrategy
 * Related DTO       : N/A
 * Related Mapper    : PickingStrategyMapper
 * Related DB Table  : picking_strategys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Wms Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Wms Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.wms.strategy;

import com.plus33.erp.wms.entity.LocationStock;
import com.plus33.erp.wms.entity.PickingWork;

import java.util.List;

/**
 * Strategy interface for directed picking allocation.
 * Implementations determine which bins (and in what order) stock should be
 * picked from when fulfilling a picking work line.
 */
public interface PickingStrategy {

    /**
     * Returns the unique key identifying this strategy,
     * used for lookup in {@link WarehouseStrategyRegistry}.
     * Examples: "FEFO", "FIFO", "LIFO", "BATCH", "CLUSTER", "ZONE"
     */
    String strategyKey();

    /**
     * Allocates bin-level stock records for the given picking work line.
     * Returns an ordered list of {@link LocationStock} records representing
     * the bins from which stock should be picked, in priority order.
     *
     * @param pickingWork   the picking work task to allocate stock for
     * @param warehouseId   the warehouse to search within
     * @param companyId     the owning company
     * @return              ordered list of bin-level stock candidates
     */
    List<LocationStock> allocate(PickingWork pickingWork, Long warehouseId, Long companyId);
}
