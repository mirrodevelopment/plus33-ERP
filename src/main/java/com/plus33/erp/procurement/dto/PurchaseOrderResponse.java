package com.plus33.erp.procurement.dto;

import com.plus33.erp.procurement.entity.PurchaseOrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Schema(description = "Purchase Order response details")
public record PurchaseOrderResponse(
        @Schema(description = "Database ID of the purchase order", example = "1")
        Long id,

        @Schema(description = "Business-readable unique identifier", example = "PO-2026-000001")
        String orderNumber,

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

        @Schema(description = "Associated Purchase Request ID", example = "1")
        Long purchaseRequestId,

        @Schema(description = "Associated Purchase Request Number", example = "PR-2026-000001")
        String purchaseRequestNumber,

        @Schema(description = "User ID who created this order", example = "1")
        Long orderedByUserId,

        @Schema(description = "Name of the creator", example = "John Doe")
        String orderedByUserName,

        @Schema(description = "User ID who issued this order", example = "1")
        Long issuedByUserId,

        @Schema(description = "Name of the issuer", example = "John Doe")
        String issuedByUserName,

        @Schema(description = "User ID who cancelled this order", example = "2")
        Long cancelledByUserId,

        @Schema(description = "Name of the canceller", example = "Jane Manager")
        String cancelledByUserName,

        @Schema(description = "Expected delivery date", example = "2026-07-01")
        LocalDate expectedDeliveryDate,

        @Schema(description = "Workflow state status", example = "DRAFT")
        PurchaseOrderStatus status,

        @Schema(description = "Optional notes or instructions", example = "Standard monthly order")
        String notes,

        @Schema(description = "SLA Timestamps")
        LocalDateTime issuedAt,
        LocalDateTime cancelledAt,
        LocalDateTime receivedAt,
        LocalDateTime closedAt,

        @Schema(description = "Cancellation reason if cancelled", example = "Order cancelled by manager")
        String cancellationReason,

        @Schema(description = "Received percentage tracking", example = "0.00")
        BigDecimal receivedPercentage,

        @Schema(description = "Financial Tracking details")
        BigDecimal subtotalAmount,
        BigDecimal taxAmount,
        BigDecimal discountAmount,
        BigDecimal totalAmount,
        String currencyCode,

        @Schema(description = "Audit timestamps")
        LocalDateTime createdAt,
        LocalDateTime updatedAt,

        @Schema(description = "Line items")
        List<PurchaseOrderItemResponse> items
) {}
