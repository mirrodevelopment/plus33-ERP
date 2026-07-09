/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.strategy
 * File              : PutAwayStrategy.java
 * Purpose           : Service interface contract defining the API for Wms Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PutAwayStrategyController
 * Related Service   : PutAwayStrategyService, PutAwayStrategyServiceImpl
 * Related Repository: PutAwayStrategyRepository
 * Related Entity    : PutAwayStrategy
 * Related DTO       : N/A
 * Related Mapper    : PutAwayStrategyMapper
 * Related DB Table  : put_away_strategys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Wms Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Wms Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
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
