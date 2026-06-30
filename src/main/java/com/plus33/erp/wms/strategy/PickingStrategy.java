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
