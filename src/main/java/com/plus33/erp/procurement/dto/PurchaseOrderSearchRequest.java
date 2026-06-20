package com.plus33.erp.procurement.dto;

import com.plus33.erp.procurement.entity.PurchaseOrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(description = "Purchase Order search query filters")
public record PurchaseOrderSearchRequest(
        @Schema(description = "Fuzzy order number or part of it", example = "PO-2026")
        String orderNumber,

        @Schema(description = "Filter by company ID", example = "1")
        Long companyId,

        @Schema(description = "Filter by supplier ID", example = "1")
        Long supplierId,

        @Schema(description = "Filter by status", example = "DRAFT")
        PurchaseOrderStatus status,

        @Schema(description = "Filter by expected delivery date range start", example = "2026-06-01")
        LocalDate expectedDeliveryDateFrom,

        @Schema(description = "Filter by expected delivery date range end", example = "2026-06-30")
        LocalDate expectedDeliveryDateTo,

        @Schema(description = "Filter by associated Purchase Request ID", example = "1")
        Long purchaseRequestId,

        @Schema(description = "Filter by creator User ID", example = "1")
        Long orderedBy
) {}
