package com.plus33.erp.organization.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

@Schema(description = "Store document metadata response payload")
public record StoreDocumentResponse(
        @Schema(description = "Database ID of the document", example = "1")
        Long id,

        @Schema(description = "Type of document", example = "RENTAL_AGREEMENT")
        String documentType,

        @Schema(description = "Name of the document file", example = "lease_agreement.pdf")
        String documentName,

        @Schema(description = "File path relative to storage root", example = "storage/documents/xyz.pdf")
        String filePath,

        @Schema(description = "Uploaded date time", example = "2026-06-20T12:00:00")
        LocalDateTime uploadedAt
) {}
