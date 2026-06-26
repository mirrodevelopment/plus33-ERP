package com.plus33.erp.sales.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;
import java.util.List;

public record CustomerInvoiceUpdateRequest(
        @NotNull(message = "Invoice date is required")
        LocalDate invoiceDate,

        LocalDate dueDate,

        @Size(max = 3, message = "Currency code must be 3 characters")
        String currencyCode,

        @NotEmpty(message = "Invoice must contain at least one item")
        @Valid
        List<CustomerInvoiceItemRequest> items
) {}
