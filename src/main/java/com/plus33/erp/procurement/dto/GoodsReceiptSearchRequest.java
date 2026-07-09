/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Procurement Module
 * Package           : com.plus33.erp.procurement.dto
 * File              : GoodsReceiptSearchRequest.java
 * Purpose           : Data Transfer Object for request/response in Procurement Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: GoodsReceiptSearchController
 * Related Service   : GoodsReceiptSearchService, GoodsReceiptSearchServiceImpl
 * Related Repository: GoodsReceiptSearchRepository
 * Related Entity    : GoodsReceiptSearch
 * Related DTO       : GoodsReceiptSearchRequest
 * Related Mapper    : GoodsReceiptSearchMapper
 * Related DB Table  : goods_receipt_searchs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : GoodsReceiptSearchController, GoodsReceiptSearchService, GoodsReceiptSearchServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Procurement Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.procurement.dto;

import com.plus33.erp.procurement.entity.GoodsReceiptStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

/**
 * <b>PLUS33 Coffee ERP -- Procurement Module</b>
 *
 * <p><b>Class  :</b> {@code GoodsReceiptSearchRequest}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.procurement.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Procurement Module.</p>
 *
 * <p><b>Module Deps      :</b> Procurement</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Schema(description = "Goods Receipt search query parameters")
public record GoodsReceiptSearchRequest(
        @Schema(description = "Fuzzy receipt number or part of it", example = "GR-2026")
        String receiptNumber,

        @Schema(description = "Filter by company ID", example = "1")
        Long companyId,

        @Schema(description = "Filter by purchase order ID", example = "1")
        Long purchaseOrderId,

        @Schema(description = "Filter by destination warehouse ID", example = "1")
        Long warehouseId,

        @Schema(description = "Filter by destination store ID", example = "1")
        Long storeId,

        @Schema(description = "Filter by goods receipt status", example = "COMPLETED")
        GoodsReceiptStatus status,

        @Schema(description = "Filter by receipt date range start", example = "2026-06-01")
        LocalDate receivedAtFrom,

        @Schema(description = "Filter by receipt date range end", example = "2026-06-30")
        LocalDate receivedAtTo
) {}