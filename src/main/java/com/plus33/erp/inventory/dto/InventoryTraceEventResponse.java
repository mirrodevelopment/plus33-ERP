/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.dto
 * File              : InventoryTraceEventResponse.java
 * Purpose           : Data Transfer Object for request/response in Inventory Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InventoryTraceEventController
 * Related Service   : InventoryTraceEventService, InventoryTraceEventServiceImpl
 * Related Repository: InventoryTraceEventRepository
 * Related Entity    : InventoryTraceEvent
 * Related DTO       : InventoryTraceEventResponse
 * Related Mapper    : InventoryTraceEventMapper
 * Related DB Table  : inventory_trace_events
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : InventoryTraceEventController, InventoryTraceEventService, InventoryTraceEventServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Inventory Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.inventory.dto;

import com.plus33.erp.inventory.entity.InventoryTraceEventType;
import com.plus33.erp.inventory.entity.InventoryTraceReferenceType;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Inventory Module</b>
 *
 * <p><b>Class  :</b> {@code InventoryTraceEventResponse}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.inventory.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Inventory Module.</p>
 *
 * <p><b>Module Deps      :</b> Inventory</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public record InventoryTraceEventResponse(
        Long id,
        Long companyId,
        Long productId,
        Long lotId,
        String lotNumber,
        Long serialId,
        String serialNumber,
        Long warehouseId,
        Long storeId,
        InventoryTraceEventType eventType,
        BigDecimal quantity,
        InventoryTraceReferenceType referenceType,
        Long referenceId,
        String referenceNumber,
        String notes,
        Long createdById,
        LocalDateTime createdAt
) {}
