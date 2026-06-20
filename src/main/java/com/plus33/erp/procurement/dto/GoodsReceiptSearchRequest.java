package com.plus33.erp.procurement.dto;

import com.plus33.erp.procurement.entity.GoodsReceiptStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

@Schema(description = "Goods Receipt search query parameters")
public record GoodsReceiptSearchRequest(
        @Schema(description = "Fuzzy receipt number or part of it", example = "GR-2026")
        String receiptNumber,

        @Schema(description = "Filter by company ID", example = "1")
        Long companyId,

        @Schema(description = "Filter by purchase order ID", example = "1")
        Long purchaseOrderId,

        @Schema(description = "Filter by destination warehouse ID", example = "1")
        Long warehouseId,

        @Schema(description = "Filter by destination store ID", example = "1")
        Long storeId,

        @Schema(description = "Filter by goods receipt status", example = "COMPLETED")
        GoodsReceiptStatus status,

        @Schema(description = "Filter by receipt date range start", example = "2026-06-01")
        LocalDate receivedAtFrom,

        @Schema(description = "Filter by receipt date range end", example = "2026-06-30")
        LocalDate receivedAtTo
) {}
