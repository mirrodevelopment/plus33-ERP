/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.assets.dto
 * File              : AssetSplitRequest.java
 * Purpose           : Data Transfer Object for request/response in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: AssetSplitController
 * Related Service   : AssetSplitService, AssetSplitServiceImpl
 * Related Repository: AssetSplitRepository
 * Related Entity    : AssetSplit
 * Related DTO       : AssetSplitRequest
 * Related Mapper    : AssetSplitMapper
 * Related DB Table  : asset_splits
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : AssetSplitController, AssetSplitService, AssetSplitServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Finance Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.finance.assets.dto;

import java.math.BigDecimal;
import java.util.List;

public record AssetSplitRequest(
    List<SplitTarget> targets
) {
    public record SplitTarget(
        String name,
        String description,
        BigDecimal allocationRatio  // e.g. 0.80 for 80%
    ) {}
}
