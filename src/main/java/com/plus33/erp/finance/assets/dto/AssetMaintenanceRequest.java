package com.plus33.erp.finance.assets.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record AssetMaintenanceRequest(
    LocalDate maintenanceDate,
    String description,
    BigDecimal cost,
    Boolean capitalize,
    String performedBy
) {}
