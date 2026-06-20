package com.plus33.erp.procurement.dto;

import com.plus33.erp.procurement.entity.PurchaseRequestStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(description = "Purchase Request search query filters")
public record PurchaseRequestSearchRequest(
        @Schema(description = "Fuzzy request number or part of it", example = "PR-2026")
        String requestNumber,

        @Schema(description = "Filter by company ID", example = "1")
        Long companyId,

        @Schema(description = "Filter by supplier ID", example = "1")
        Long supplierId,

        @Schema(description = "Filter by warehouse ID", example = "1")
        Long warehouseId,

        @Schema(description = "Filter by store ID", example = "1")
        Long storeId,

        @Schema(description = "Filter by status", example = "DRAFT")
        PurchaseRequestStatus status,

        @Schema(description = "Filter by requester User ID", example = "1")
        Long requestedBy,

        @Schema(description = "Filter by request date range start", example = "2026-06-01")
        LocalDate requestDateFrom,

        @Schema(description = "Filter by request date range end", example = "2026-06-30")
        LocalDate requestDateTo,

        @Schema(description = "Filter by required date range start", example = "2026-07-01")
        LocalDate requiredDateFrom,

        @Schema(description = "Filter by required date range end", example = "2026-07-31")
        LocalDate requiredDateTo
) {}
