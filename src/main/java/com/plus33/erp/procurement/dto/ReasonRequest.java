package com.plus33.erp.procurement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Request body containing reason details for rejection or cancellation")
public record ReasonRequest(
        @Schema(description = "Reason description", example = "Price too high / duplicate request")
        @NotBlank(message = "Reason is required")
        String reason
) {}
