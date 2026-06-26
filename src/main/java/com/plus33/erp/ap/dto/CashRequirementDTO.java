package com.plus33.erp.ap.dto;

import java.math.BigDecimal;

public record CashRequirementDTO(
        BigDecimal next30Days,
        BigDecimal next60Days,
        BigDecimal next90Days
) {}
