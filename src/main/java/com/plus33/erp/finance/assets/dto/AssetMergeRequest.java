/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.assets.dto
 * File              : AssetMergeRequest.java
 * Purpose           : Data Transfer Object for request/response in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: AssetMergeController
 * Related Service   : AssetMergeService, AssetMergeServiceImpl
 * Related Repository: AssetMergeRepository
 * Related Entity    : AssetMerge
 * Related DTO       : AssetMergeRequest
 * Related Mapper    : AssetMergeMapper
 * Related DB Table  : asset_merges
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : AssetMergeController, AssetMergeService, AssetMergeServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Finance Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.finance.assets.dto;

import java.util.List;

public record AssetMergeRequest(
    List<Long> sourceAssetIds,
    String targetName,
    String targetDescription
) {}
