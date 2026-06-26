package com.plus33.erp.finance.assets.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AssetLeaseRequest(
    String leaseType,
    LocalDate leaseStartDate,
    LocalDate leaseEndDate,
    BigDecimal monthlyLeasePayment,
    String lessorName,
    Long leaseLiabilityAccountId
) {}
