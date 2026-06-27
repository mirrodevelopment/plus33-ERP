package com.plus33.erp.finance.budget.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record BudgetResponse(
    Long id,
    Long companyId,
    Long fiscalYearId,
    Long budgetPolicyId,
    Long workflowTemplateId,
    String code,
    String name,
    String budgetType,
    String periodType,
    String scenario,
    String status,
    Integer versionNumber,
    Boolean isForecast,
    String forecastType,
    String forecastCycleCode,
    Boolean isFrozen,
    Boolean isActive,
    String rateLockType,
    BigDecimal budgetExchangeRate,
    String createdBy,
    String approvedBy,
    LocalDateTime approvedAt,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    List<BudgetLineResponse> lines
) {}
