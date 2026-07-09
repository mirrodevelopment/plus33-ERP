/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Analytics Module
 * Package           : com.plus33.erp.analytics.dto
 * File              : InventoryKpisResponse.java
 * Purpose           : Data Transfer Object for request/response in Analytics Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InventoryKpisController
 * Related Service   : InventoryKpisService, InventoryKpisServiceImpl
 * Related Repository: InventoryKpisRepository
 * Related Entity    : InventoryKpis
 * Related DTO       : InventoryKpisResponse
 * Related Mapper    : InventoryKpisMapper
 * Related DB Table  : inventory_kpiss
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : InventoryKpisController, InventoryKpisService, InventoryKpisServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Analytics Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.analytics.dto;

import java.math.BigDecimal;

public record InventoryKpisResponse(
        Long companyId,
        Long warehouseId,
        Long storeId,
        BigDecimal totalStockValue,
        Long totalUniqueProducts,
        Long outOfStockProducts,
        Long lowStockProducts,
        Long overstockProducts
) {}
