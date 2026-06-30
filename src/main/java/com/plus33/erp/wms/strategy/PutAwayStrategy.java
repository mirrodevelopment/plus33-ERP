package com.plus33.erp.wms.strategy;

import com.plus33.erp.wms.entity.AsnLine;
import com.plus33.erp.wms.entity.WarehouseLocation;

import java.util.Optional;

/**
 * Strategy interface for directed put-away.
 * Implementations resolve which warehouse location a given receipt line
 * should be stored in, based on the strategy's algorithm.
 */
public interface PutAwayStrategy {

    /**
     * Returns the unique key identifying this strategy,
     * used for lookup in {@link WarehouseStrategyRegistry}.
     * Examples: "NEAREST_EMPTY_BIN", "FIXED_BIN", "CAPACITY_BASED"
     */
    String strategyKey();

    /**
     * Resolves the optimal put-away location for the given ASN line.
     *
     * @param asnLine       the incoming receipt line (product, lot, qty)
     * @param warehouseId   the target warehouse
     * @param companyId     the owning company
     * @return              the resolved target location, or empty if no suitable location found
     */
    Optional<WarehouseLocation> resolve(AsnLine asnLine, Long warehouseId, Long companyId);
}
