/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.dto
 * File              : SalesOrderItemRequest.java
 * Purpose           : Data Transfer Object for request/response in Sales Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SalesOrderItemController
 * Related Service   : SalesOrderItemService, SalesOrderItemServiceImpl
 * Related Repository: SalesOrderItemRepository
 * Related Entity    : SalesOrderItem
 * Related DTO       : SalesOrderItemRequest
 * Related Mapper    : SalesOrderItemMapper
 * Related DB Table  : sales_order_items
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : SalesOrderItemController, SalesOrderItemService, SalesOrderItemServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Sales Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.sales.dto;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Sales Module</b>
 *
 * <p><b>Class  :</b> {@code SalesOrderItemRequest}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.sales.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Sales Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public record SalesOrderItemRequest(
    @NotNull(message = "Product ID is required")
    Long productId,

    @NotNull(message = "Ordered quantity is required")
    @DecimalMin(value = "0.01", message = "Ordered quantity must be greater than 0")
    BigDecimal orderedQuantity,

    @NotNull(message = "Unit price is required")
    @DecimalMin(value = "0.0", message = "Unit price cannot be negative")
    BigDecimal unitPrice,

    @DecimalMin(value = "0.0", message = "Discount percentage cannot be negative")
    @DecimalMax(value = "100.0", message = "Discount percentage cannot exceed 100%")
    BigDecimal discountPercentage,

    @DecimalMin(value = "0.0", message = "Tax percentage cannot be negative")
    @DecimalMax(value = "100.0", message = "Tax percentage cannot exceed 100%")
    BigDecimal taxPercentage
) {}
