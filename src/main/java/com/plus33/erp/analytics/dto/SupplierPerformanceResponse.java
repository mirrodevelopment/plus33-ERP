/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Analytics Module
 * Package           : com.plus33.erp.analytics.dto
 * File              : SupplierPerformanceResponse.java
 * Purpose           : Data Transfer Object for request/response in Analytics Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SupplierPerformanceController
 * Related Service   : SupplierPerformanceService, SupplierPerformanceServiceImpl
 * Related Repository: SupplierPerformanceRepository
 * Related Entity    : SupplierPerformance
 * Related DTO       : SupplierPerformanceResponse
 * Related Mapper    : SupplierPerformanceMapper
 * Related DB Table  : supplier_performances
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : SupplierPerformanceController, SupplierPerformanceService, SupplierPerformanceServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Analytics Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.analytics.dto;

import java.math.BigDecimal;

public record SupplierPerformanceResponse(
        Long companyId,
        Long supplierId,
        String supplierName,
        Long totalOrders,
        BigDecimal totalSpend,
        BigDecimal onTimeDeliveryRate,
        BigDecimal avgLeadTimeDays
) {}
