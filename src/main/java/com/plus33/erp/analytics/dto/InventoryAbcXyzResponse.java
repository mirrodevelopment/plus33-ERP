/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Analytics Module
 * Package           : com.plus33.erp.analytics.dto
 * File              : InventoryAbcXyzResponse.java
 * Purpose           : Data Transfer Object for request/response in Analytics Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InventoryAbcXyzController
 * Related Service   : InventoryAbcXyzService, InventoryAbcXyzServiceImpl
 * Related Repository: InventoryAbcXyzRepository
 * Related Entity    : InventoryAbcXyz
 * Related DTO       : InventoryAbcXyzResponse
 * Related Mapper    : InventoryAbcXyzMapper
 * Related DB Table  : inventory_abc_xyzs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : InventoryAbcXyzController, InventoryAbcXyzService, InventoryAbcXyzServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Analytics Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.analytics.dto;

public record InventoryAbcXyzResponse(
        Long companyId,
        Long productId,
        Long warehouseId,
        Long storeId,
        String abcClass,
        String xyzClass
) {}
