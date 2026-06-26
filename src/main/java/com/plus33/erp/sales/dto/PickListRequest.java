package com.plus33.erp.sales.dto;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

public record PickListRequest(
    @NotNull(message = "Company ID is required")
    Long companyId,

    @NotNull(message = "Sales Order ID is required")
    Long salesOrderId,

    Long warehouseId,
    Long storeId,

    @NotNull(message = "Client reference ID is required")
    UUID clientReferenceId
) {}
