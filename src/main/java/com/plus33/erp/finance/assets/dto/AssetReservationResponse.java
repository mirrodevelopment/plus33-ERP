package com.plus33.erp.finance.assets.dto;

import java.time.LocalDate;

public record AssetReservationResponse(
    Long id,
    Long fixedAssetId,
    String assetCode,
    String assetName,
    String reservedForEmployee,
    String reservedForDepartment,
    LocalDate startDate,
    LocalDate endDate,
    String purpose,
    String status
) {}
