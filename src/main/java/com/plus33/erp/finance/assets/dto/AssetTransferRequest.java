/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.assets.dto
 * File              : AssetTransferRequest.java
 * Purpose           : Data Transfer Object for request/response in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: AssetTransferController
 * Related Service   : AssetTransferService, AssetTransferServiceImpl
 * Related Repository: AssetTransferRepository
 * Related Entity    : AssetTransfer
 * Related DTO       : AssetTransferRequest
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

public record AssetTransferRequest(
    LocalDate transferDate,
    Long toWarehouseId,
    Long toStoreId,
    Long toCompanyId,
    String reason
) {}
