package com.plus33.erp.sales.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record SalesOrderRequest(
    @NotNull(message = "Company ID is required")
    Long companyId,

    @NotNull(message = "Customer ID is required")
    Long customerId,

    @NotNull(message = "Client reference ID is required")
    UUID clientReferenceId,

    LocalDate requestedDeliveryDate,

    @NotEmpty(message = "Sales order items cannot be empty")
    List<@Valid SalesOrderItemRequest> items
) {}
