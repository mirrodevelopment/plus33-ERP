/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Procurement Module
 * Package           : com.plus33.erp.procurement.dto
 * File              : PurchaseRequestSearchRequest.java
 * Purpose           : Data Transfer Object for request/response in Procurement Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PurchaseRequestSearchController
 * Related Service   : PurchaseRequestSearchService, PurchaseRequestSearchServiceImpl
 * Related Repository: PurchaseRequestSearchRepository
 * Related Entity    : PurchaseRequestSearch
 * Related DTO       : PurchaseRequestSearchRequest
 * Related Mapper    : PurchaseRequestSearchMapper
 * Related DB Table  : purchase_request_searchs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PurchaseRequestSearchController, PurchaseRequestSearchService, PurchaseRequestSearchServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Procurement Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.procurement.dto;

import com.plus33.erp.procurement.entity.PurchaseRequestStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDate;

/**
 * <b>PLUS33 Coffee ERP -- Procurement Module</b>
 *
 * <p><b>Class  :</b> {@code PurchaseRequestSearchRequest}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.procurement.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Procurement Module.</p>
 *
 * <p><b>Module Deps      :</b> Procurement</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Schema(description = "Purchase Request search query filters")
public record PurchaseRequestSearchRequest(
        @Schema(description = "Fuzzy request number or part of it", example = "PR-2026")
        String requestNumber,

        @Schema(description = "Filter by company ID", example = "1")
        Long companyId,

        @Schema(description = "Filter by supplier ID", example = "1")
        Long supplierId,

        @Schema(description = "Filter by warehouse ID", example = "1")
        Long warehouseId,

        @Schema(description = "Filter by store ID", example = "1")
        Long storeId,

        @Schema(description = "Filter by status", example = "DRAFT")
        PurchaseRequestStatus status,

        @Schema(description = "Filter by requester User ID", example = "1")
        Long requestedBy,

        @Schema(description = "Filter by request date range start", example = "2026-06-01")
        LocalDate requestDateFrom,

        @Schema(description = "Filter by request date range end", example = "2026-06-30")
        LocalDate requestDateTo,

        @Schema(description = "Filter by required date range start", example = "2026-07-01")
        LocalDate requiredDateFrom,

        @Schema(description = "Filter by required date range end", example = "2026-07-31")
        LocalDate requiredDateTo
) {}