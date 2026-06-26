package com.plus33.erp.sales.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

public record CustomerInvoiceRequest(
        @NotNull(message = "Company ID is required")
        Long companyId,

        @NotNull(message = "Customer ID is required")
        Long customerId,

        Long salesOrderId,

        @NotNull(message = "Client reference ID is required")
        UUID clientReferenceId,

        @NotNull(message = "Invoice date is required")
        LocalDate invoiceDate,

        LocalDate dueDate,

        @Size(max = 3, message = "Currency code must be 3 characters")
        String currencyCode,

        @NotEmpty(message = "Invoice must contain at least one item")
        @Valid
        List<CustomerInvoiceItemRequest> items
) {}
