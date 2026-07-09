/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.dto
 * File              : SalesOrderRequest.java
 * Purpose           : Data Transfer Object for request/response in Sales Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SalesOrderController
 * Related Service   : SalesOrderService, SalesOrderServiceImpl
 * Related Repository: SalesOrderRepository
 * Related Entity    : SalesOrder
 * Related DTO       : SalesOrderItemRequest, SalesOrderRequest
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

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

/**
 * <b>PLUS33 Coffee ERP -- Sales Module</b>
 *
 * <p><b>Class  :</b> {@code SalesOrderRequest}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.sales.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Sales Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public record SalesOrderRequest(
    @NotNull(message = "Company ID is required")
    Long companyId,

    @NotNull(message = "Customer ID is required")
    Long customerId,

    @NotNull(message = "Client reference ID is required")
    UUID clientReferenceId,

    LocalDate requestedDeliveryDate,

    @NotEmpty(message = "Sales order items cannot be empty")
    List<@Valid SalesOrderItemRequest> items
) {}
