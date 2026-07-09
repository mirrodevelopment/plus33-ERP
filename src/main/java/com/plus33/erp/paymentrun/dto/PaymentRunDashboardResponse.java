/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Paymentrun Module
 * Package           : com.plus33.erp.paymentrun.dto
 * File              : PaymentRunDashboardResponse.java
 * Purpose           : Data Transfer Object for request/response in Paymentrun Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PaymentRunDashboardController
 * Related Service   : PaymentRunDashboardService, PaymentRunDashboardServiceImpl
 * Related Repository: PaymentRunDashboardRepository
 * Related Entity    : PaymentRunDashboard
 * Related DTO       : PaymentRunDashboardResponse
 * Related Mapper    : PaymentRunDashboardMapper
 * Related DB Table  : payment_run_dashboards
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PaymentRunDashboardController, PaymentRunDashboardService, PaymentRunDashboardServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Paymentrun Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.paymentrun.dto;

import java.math.BigDecimal;

public record PaymentRunDashboardResponse(
        Long companyId,
        
        // Basic Financial KPIs
        Long scheduledPaymentsCount,
        BigDecimal scheduledPaymentsAmount,
        BigDecimal paymentsDueTodayAmount,
        BigDecimal paymentsDueThisWeekAmount,
        BigDecimal cashRequiredAmount,
        BigDecimal executedPaymentsAmount,
        Long failedPaymentsCount,
        
        // Rich Operational KPIs
        Long totalPaymentRunsCount,
        Long completedTodayCount,
        Long processingNowCount,
        Long cancelledRunsCount,
        BigDecimal averagePaymentBatchSize,
        BigDecimal largestPaymentRunAmount,
        BigDecimal averageExecutionTimeSeconds
) {}
