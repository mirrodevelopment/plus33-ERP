/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.dto
 * File              : InventorySerialResponse.java
 * Purpose           : Data Transfer Object for request/response in Inventory Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InventorySerialController
 * Related Service   : InventorySerialService, InventorySerialServiceImpl
 * Related Repository: InventorySerialRepository
 * Related Entity    : InventorySerial
 * Related DTO       : InventorySerialResponse
 * Related Mapper    : InventorySerialMapper
 * Related DB Table  : inventory_serials
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : InventorySerialController, InventorySerialService, InventorySerialServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Inventory Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.inventory.dto;

import com.plus33.erp.inventory.entity.InventorySerialStatus;
import java.time.LocalDateTime;

public record InventorySerialResponse(
        Long id,
        Long companyId,
        Long productId,
        Long lotId,
        String lotNumber,
        String serialNumber,
        Long warehouseId,
        Long storeId,
        InventorySerialStatus status,
        Long version,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
