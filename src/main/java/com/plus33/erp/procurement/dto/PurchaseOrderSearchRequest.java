/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Procurement Module
 * Package           : com.plus33.erp.procurement.dto
 * File              : PurchaseOrderSearchRequest.java
 * Purpose           : Data Transfer Object for request/response in Procurement Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PurchaseOrderSearchController
 * Related Service   : PurchaseOrderSearchService, PurchaseOrderSearchServiceImpl
 * Related Repository: PurchaseOrderSearchRepository
 * Related Entity    : PurchaseOrderSearch
 * Related DTO       : PurchaseOrderSearchRequest
 * Related Mapper    : PurchaseOrderSearchMapper
 * Related DB Table  : purchase_order_searchs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PurchaseOrderSearchController, PurchaseOrderSearchService, PurchaseOrderSearchServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Procurement Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.procurement.dto;

import com.plus33.erp.procurement.entity.PurchaseOrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

/**
 * <b>PLUS33 Coffee ERP -- Procurement Module</b>
 *
 * <p><b>Class  :</b> {@code PurchaseOrderSearchRequest}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.procurement.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Procurement Module.</p>
 *
 * <p><b>Module Deps      :</b> Procurement</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Schema(description = "Purchase Order search query filters")
public record PurchaseOrderSearchRequest(
        @Schema(description = "Fuzzy order number or part of it", example = "PO-2026")
        String orderNumber,

        @Schema(description = "Filter by company ID", example = "1")
        Long companyId,

        @Schema(description = "Filter by supplier ID", example = "1")
        Long supplierId,

        @Schema(description = "Filter by status", example = "DRAFT")
        PurchaseOrderStatus status,

        @Schema(description = "Filter by expected delivery date range start", example = "2026-06-01")
        LocalDate expectedDeliveryDateFrom,

        @Schema(description = "Filter by expected delivery date range end", example = "2026-06-30")
        LocalDate expectedDeliveryDateTo,

        @Schema(description = "Filter by associated Purchase Request ID", example = "1")
        Long purchaseRequestId,

        @Schema(description = "Filter by creator User ID", example = "1")
        Long orderedBy
) {}