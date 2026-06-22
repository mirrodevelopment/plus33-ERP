package com.plus33.erp.sales.dto;

import com.plus33.erp.sales.entity.SalesOrderStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record SalesOrderResponse(
    Long id,
    Long companyId,
    String companyName,
    Long customerId,
    String orderNumber,
    UUID clientReferenceId,
    LocalDate orderDate,
    LocalDate requestedDeliveryDate,
    String currencyCode,
    Integer paymentTermsDays,
    String billingAddress,
    String shippingAddress,
    SalesOrderStatus status,

    // Customer Snapshot
    String customerName,
    String customerCode,
    String customerType,
    String pricingTier,
    BigDecimal discountRate,
    String taxProfile,

    // Monetary
    BigDecimal subtotal,
    BigDecimal discountAmount,
    BigDecimal taxAmount,
    BigDecimal totalAmount,
    BigDecimal outstandingAmount,
    Boolean creditOverride,

    // Audit
    Long orderedByUserId,
    String orderedByUserName,
    Long submittedByUserId,
    String submittedByUserName,
    Long approvedByUserId,
    String approvedByUserName,
    Long cancelledByUserId,
    String cancelledByUserName,
    LocalDateTime submittedAt,
    LocalDateTime approvedAt,
    LocalDateTime cancelledAt,
    String cancellationReason,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    Long version,

    List<SalesOrderItemResponse> items
) {}
