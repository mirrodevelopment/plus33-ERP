package com.plus33.erp.finance.budget.dto;

import java.time.LocalDate;

public record ProjectRequest(
    String code,
    String name,
    LocalDate startDate,
    LocalDate endDate,
    String status,
    Boolean active
) {}
