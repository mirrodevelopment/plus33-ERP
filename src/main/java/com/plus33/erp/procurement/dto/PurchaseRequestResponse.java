package com.plus33.erp.procurement.dto;

import com.plus33.erp.procurement.entity.PurchaseRequestStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Purchase Request response details")
public record PurchaseRequestResponse(
        @Schema(description = "Database ID of the purchase request", example = "1")
        Long id,

        @Schema(description = "Business-readable unique identifier", example = "PR-2026-000001")
        String requestNumber,

        @Schema(description = "Company ID mapping", example = "1")
        Long companyId,

        @Schema(description = "Company Name", example = "PLUS33 Global Corp")
        String companyName,

        @Schema(description = "Company Code", example = "PLUS33_GLOBAL")
        String companyCode,

        @Schema(description = "Supplier ID mapping", example = "1")
        Long supplierId,

        @Schema(description = "Supplier Name", example = "Almarai Dairy")
        String supplierName,

        @Schema(description = "Supplier Code", example = "SUP_ALMARAI")
        String supplierCode,

        @Schema(description = "User ID who created/requested this request", example = "1")
        Long requestedByUserId,

        @Schema(description = "Name of the requester", example = "John Doe")
        String requestedByUserName,

        @Schema(description = "User ID who submitted this request", example = "1")
        Long submittedByUserId,

        @Schema(description = "Name of the submitter", example = "John Doe")
        String submittedByUserName,

        @Schema(description = "User ID who approved this request", example = "2")
        Long approvedByUserId,

        @Schema(description = "Name of the approver", example = "Jane Manager")
        String approvedByUserName,

        @Schema(description = "Destination warehouse ID mapping", example = "1")
        Long warehouseId,

        @Schema(description = "Destination warehouse Name", example = "Dubai Central Warehouse")
        String warehouseName,

        @Schema(description = "Destination warehouse Code", example = "DUBAI_WAREHOUSE")
        String warehouseCode,

        @Schema(description = "Destination store ID mapping", example = "1")
        Long storeId,

        @Schema(description = "Destination store Name", example = "Dubai Mall Store")
        String storeName,

        @Schema(description = "Destination store Code", example = "DUBAI_MALL_STORE")
        String storeCode,

        @Schema(description = "Workflow state status", example = "DRAFT")
        PurchaseRequestStatus status,

        @Schema(description = "Date when the request was initiated", example = "2026-06-20")
        LocalDate requestDate,

        @Schema(description = "Date when the goods are required", example = "2026-07-01")
        LocalDate requiredDate,

        @Schema(description = "Optional notes or instructions", example = "Standard monthly replenishment")
        String notes,

        @Schema(description = "SLA Timestamps")
        LocalDateTime submittedAt,
        LocalDateTime approvedAt,
        LocalDateTime rejectedAt,
        LocalDateTime cancelledAt,
        LocalDateTime convertedToPoAt,

        @Schema(description = "Event reasoning explanations")
        String rejectionReason,
        String cancellationReason,

        @Schema(description = "Associated Purchase Order ID if converted", example = "1")
        Long purchaseOrderId,

        @Schema(description = "Associated Purchase Order Number if converted", example = "PO-2026-000001")
        String purchaseOrderNumber,

        @Schema(description = "Audit timestamps")
        LocalDateTime createdAt,
        LocalDateTime updatedAt,

        @Schema(description = "Line items")
        List<PurchaseRequestItemResponse> items
) {}
