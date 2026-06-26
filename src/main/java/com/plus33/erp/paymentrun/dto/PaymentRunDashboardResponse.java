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
