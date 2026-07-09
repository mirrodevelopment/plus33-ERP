/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Analytics Module
 * Package           : com.plus33.erp.analytics.dto
 * File              : PoFulfilmentResponse.java
 * Purpose           : Data Transfer Object for request/response in Analytics Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PoFulfilmentController
 * Related Service   : PoFulfilmentService, PoFulfilmentServiceImpl
 * Related Repository: PoFulfilmentRepository
 * Related Entity    : PoFulfilment
 * Related DTO       : PoFulfilmentResponse
 * Related Mapper    : PoFulfilmentMapper
 * Related DB Table  : po_fulfilments
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PoFulfilmentController, PoFulfilmentService, PoFulfilmentServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Analytics Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.analytics.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PoFulfilmentResponse(
        Long companyId,
        Long purchaseOrderId,
        String orderNumber,
        String supplierName,
        String status,
        BigDecimal totalAmount,
        LocalDate expectedDeliveryDate,
        Long totalItemsOrdered,
        BigDecimal totalQuantityOrdered,
        BigDecimal totalQuantityReceived,
        BigDecimal fulfillmentRate
) {}
