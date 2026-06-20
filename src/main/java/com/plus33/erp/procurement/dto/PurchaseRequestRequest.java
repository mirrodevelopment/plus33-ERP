package com.plus33.erp.procurement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.List;

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
