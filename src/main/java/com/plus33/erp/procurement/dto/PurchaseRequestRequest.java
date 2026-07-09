/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Procurement Module
 * Package           : com.plus33.erp.procurement.dto
 * File              : PurchaseRequestRequest.java
 * Purpose           : Data Transfer Object for request/response in Procurement Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PurchaseRequestController
 * Related Service   : PurchaseRequestService, PurchaseRequestServiceImpl
 * Related Repository: PurchaseRequestRepository
 * Related Entity    : PurchaseRequest
 * Related DTO       : PurchaseRequestItemRequest, PurchaseRequestRequest
 * Related Mapper    : PurchaseRequestMapper
 * Related DB Table  : purchase_requests
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PurchaseRequestController, PurchaseRequestService, PurchaseRequestServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Procurement Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.procurement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Procurement Module</b>
 *
 * <p><b>Class  :</b> {@code PurchaseRequestRequest}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.procurement.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Procurement Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Schema(description = "Purchase Request creation/update parameters")
public record PurchaseRequestRequest(
        @Schema(description = "Company ID mapping", example = "1")
        @NotNull(message = "Company ID is required")
        Long companyId,

        @Schema(description = "Supplier ID mapping", example = "1")
        @NotNull(message = "Supplier ID is required")
        Long supplierId,

        @Schema(description = "Destination warehouse ID mapping", example = "1")
        Long warehouseId,

        @Schema(description = "Destination store ID mapping", example = "1")
        Long storeId,

        @Schema(description = "Required delivery date", example = "2026-07-01")
        @NotNull(message = "Required date is required")
        @FutureOrPresent(message = "Required date cannot be in the past")
        LocalDate requiredDate,

        @Schema(description = "Header level notes", example = "Standard monthly order")
        String notes,

        @Schema(description = "List of line items")
        @NotEmpty(message = "Purchase request must contain at least one line item")
        @Valid
        List<PurchaseRequestItemRequest> items
) {}