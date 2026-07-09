/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Analytics Module
 * Package           : com.plus33.erp.analytics.dto
 * File              : InventoryAgingExpiryResponse.java
 * Purpose           : Data Transfer Object for request/response in Analytics Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InventoryAgingExpiryController
 * Related Service   : InventoryAgingExpiryService, InventoryAgingExpiryServiceImpl
 * Related Repository: InventoryAgingExpiryRepository
 * Related Entity    : InventoryAgingExpiry
 * Related DTO       : InventoryAgingExpiryResponse
 * Related Mapper    : InventoryAgingExpiryMapper
 * Related DB Table  : inventory_aging_expirys
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : InventoryAgingExpiryController, InventoryAgingExpiryService, InventoryAgingExpiryServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Analytics Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.analytics.dto;

import java.math.BigDecimal;

public record InventoryAgingExpiryResponse(
        Long companyId,
        Long warehouseId,
        Long storeId,
        BigDecimal aging0To30,
        BigDecimal aging31To90,
        BigDecimal aging91To180,
        BigDecimal aging180Plus,
        Long expiredLotsCount,
        Long expiring0To30Count,
        Long expiring31To90Count,
        Long expiring91To180Count,
        Long safeLotsCount
) {}
