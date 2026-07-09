/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.dto
 * File              : PickListRequest.java
 * Purpose           : Data Transfer Object for request/response in Sales Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PickListController
 * Related Service   : PickListService, PickListServiceImpl
 * Related Repository: PickListRepository
 * Related Entity    : PickList
 * Related DTO       : PickListRequest
 * Related Mapper    : PickListMapper
 * Related DB Table  : pick_lists
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PickListController, PickListService, PickListServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Sales Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.sales.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record PickListRequest(
    @NotNull(message = "Company ID is required")
    Long companyId,

    @NotNull(message = "Sales Order ID is required")
    Long salesOrderId,

    Long warehouseId,
    Long storeId,

    @NotNull(message = "Client reference ID is required")
    UUID clientReferenceId
) {}
