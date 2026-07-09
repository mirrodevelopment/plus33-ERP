/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Procurement Module
 * Package           : com.plus33.erp.procurement.dto
 * File              : GoodsReceiptUpdateRequest.java
 * Purpose           : Data Transfer Object for request/response in Procurement Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: GoodsReceiptUpdateController
 * Related Service   : GoodsReceiptUpdateService, GoodsReceiptUpdateServiceImpl
 * Related Repository: GoodsReceiptUpdateRepository
 * Related Entity    : GoodsReceiptUpdate
 * Related DTO       : GoodsReceiptUpdateRequest
 * Related Mapper    : GoodsReceiptUpdateMapper
 * Related DB Table  : goods_receipt_updates
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : GoodsReceiptUpdateController, GoodsReceiptUpdateService, GoodsReceiptUpdateServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Procurement Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.procurement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Size;

/**
 * <b>PLUS33 Coffee ERP -- Procurement Module</b>
 *
 * <p><b>Class  :</b> {@code GoodsReceiptUpdateRequest}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.procurement.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Procurement Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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