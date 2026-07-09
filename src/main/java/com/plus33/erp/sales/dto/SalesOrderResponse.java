/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.dto
 * File              : SalesOrderResponse.java
 * Purpose           : Data Transfer Object for request/response in Sales Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SalesOrderController
 * Related Service   : SalesOrderService, SalesOrderServiceImpl
 * Related Repository: SalesOrderRepository
 * Related Entity    : SalesOrder
 * Related DTO       : SalesOrderItemResponse, SalesOrderResponse
 * Related Mapper    : SalesOrderMapper
 * Related DB Table  : sales_orders
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : SalesOrderController, SalesOrderService, SalesOrderServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Sales Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.sales.dto;

import com.plus33.erp.sales.entity.SalesOrderStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * <b>PLUS33 Coffee ERP -- Sales Module</b>
 *
 * <p><b>Class  :</b> {@code SalesOrderResponse}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.sales.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Sales Module.</p>
 *
 * <p><b>Module Deps      :</b> Sales</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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
