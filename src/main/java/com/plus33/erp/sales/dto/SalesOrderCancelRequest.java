package com.plus33.erp.sales.dto;

import jakarta.validation.constraints.NotBlank;

public record SalesOrderCancelRequest(
    @NotBlank(message = "Cancellation reason is required")
    String reason
) {}
