/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.assets.dto
 * File              : AssetTransferResponse.java
 * Purpose           : Data Transfer Object for request/response in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: AssetTransferController
 * Related Service   : AssetTransferService, AssetTransferServiceImpl
 * Related Repository: AssetTransferRepository
 * Related Entity    : AssetTransfer
 * Related DTO       : AssetTransferResponse
 * Related Mapper    : AssetTransferMapper
 * Related DB Table  : asset_transfers
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : AssetTransferController, AssetTransferService, AssetTransferServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Finance Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.finance.assets.dto;

import java.time.LocalDate;

public record AssetTransferResponse(
    Long id,
    Long fixedAssetId,
    String assetCode,
    String assetName,
    LocalDate transferDate,
    Long fromWarehouseId,
    String fromWarehouseName,
    Long fromStoreId,
    String fromStoreName,
    Long toWarehouseId,
    String toWarehouseName,
    Long toStoreId,
    String toStoreName,
    Long toCompanyId,
    String toCompanyName,
    String status,
    String reason,
    String performedBy
) {}
