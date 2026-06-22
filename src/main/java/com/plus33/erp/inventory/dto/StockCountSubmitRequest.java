package com.plus33.erp.inventory.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public record StockCountSubmitRequest(
        @NotEmpty(message = "Items list cannot be empty")
        @Valid
        List<StockCountItemCountRequest> items
) {}
