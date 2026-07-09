/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.assets.dto
 * File              : AssetWorkOrderResponse.java
 * Purpose           : Data Transfer Object for request/response in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: AssetWorkOrderController
 * Related Service   : AssetWorkOrderService, AssetWorkOrderServiceImpl
 * Related Repository: AssetWorkOrderRepository
 * Related Entity    : AssetWorkOrder
 * Related DTO       : AssetWorkOrderResponse
 * Related Mapper    : AssetWorkOrderMapper
 * Related DB Table  : asset_work_orders
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : AssetWorkOrderController, AssetWorkOrderService, AssetWorkOrderServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Finance Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.finance.assets.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AssetWorkOrderResponse(
    Long id,
    Long fixedAssetId,
    String assetCode,
    String assetName,
    String title,
    String description,
    String priority,
    String status,
    LocalDate scheduledDate,
    LocalDate completedDate,
    BigDecimal estimatedCost,
    BigDecimal actualCost,
    String createdBy
) {}
