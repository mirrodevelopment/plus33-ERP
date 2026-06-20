package com.plus33.erp.procurement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

@Schema(description = "Goods Receipt update parameters (restricted to remarks and supplier reference updates)")
public record GoodsReceiptUpdateRequest(
        @Schema(description = "Remarks update", example = "Updated note")
        @Size(max = 255, message = "Remarks cannot exceed 255 characters")
        String remarks,

        @Schema(description = "Supplier delivery note reference", example = "DN-90812-REV")
        @Size(max = 100, message = "Delivery note reference cannot exceed 100 characters")
        String supplierDeliveryNote,

        @Schema(description = "Supplier invoice number reference", example = "INV-7731-REV")
        @Size(max = 100, message = "Invoice number cannot exceed 100 characters")
        String supplierInvoiceNumber
) {}
