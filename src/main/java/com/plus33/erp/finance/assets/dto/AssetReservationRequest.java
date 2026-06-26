package com.plus33.erp.finance.assets.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AssetReservationRequest(
    Long fixedAssetId,
    String reservedForEmployee,
    String reservedForDepartment,
    LocalDate startDate,
    LocalDate endDate,
    String purpose
) {}
