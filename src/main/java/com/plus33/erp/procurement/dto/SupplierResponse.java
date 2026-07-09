/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Procurement Module
 * Package           : com.plus33.erp.procurement.dto
 * File              : SupplierResponse.java
 * Purpose           : Data Transfer Object for request/response in Procurement Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SupplierController
 * Related Service   : SupplierService, SupplierServiceImpl
 * Related Repository: SupplierRepository
 * Related Entity    : Supplier
 * Related DTO       : SupplierResponse
 * Related Mapper    : SupplierMapper
 * Related DB Table  : suppliers
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : SupplierController, SupplierService, SupplierServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Procurement Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.procurement.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Procurement Module</b>
 *
 * <p><b>Class  :</b> {@code SupplierResponse}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.procurement.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Procurement Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Schema(description = "Supplier response details payload")
public record SupplierResponse(
        @Schema(description = "Database ID of the supplier", example = "1")
        Long id,

        @Schema(description = "Company ID of the supplier", example = "1")
        Long companyId,

        @Schema(description = "Company Code of the supplier", example = "PLUS33_GLOBAL")
        String companyCode,

        @Schema(description = "Unique code of the supplier", example = "SUP-DXB-001")
        String code,

        @Schema(description = "Name of the supplier", example = "Dubai Coffee Supply Co")
        String name,

        @Schema(description = "Contact person name", example = "John Doe")
        String contactPerson,

        @Schema(description = "Contact email of the supplier", example = "contact@dubaisupply.com")
        String email,

        @Schema(description = "Contact phone of the supplier", example = "+971-4-1234567")
        String phone,

        @Schema(description = "Address of the supplier", example = "Al Quoz Industrial Area 3, Dubai")
        String address,

        @Schema(description = "Tax Registration Number (TRN)", example = "100254874500003")
        String taxNumber,

        @Schema(description = "Active status of the supplier", example = "true")
        Boolean active,

        @Schema(description = "Creation date time", example = "2026-06-20T12:00:00")
        LocalDateTime createdAt,

        @Schema(description = "Last modification date time", example = "2026-06-20T12:30:00")
        LocalDateTime updatedAt
) {}