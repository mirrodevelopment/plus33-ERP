package com.plus33.erp.inventory.dto;

import jakarta.validation.constraints.NotBlank;

public record ReplenishmentSuggestionCancelRequest(
        @NotBlank(message = "Notes are required for cancellation")
        String notes
) {}
