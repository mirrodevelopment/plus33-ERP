/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Procurement Module
 * Package           : com.plus33.erp.procurement.dto
 * File              : SupplierRequest.java
 * Purpose           : Data Transfer Object for request/response in Procurement Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SupplierController
 * Related Service   : SupplierService, SupplierServiceImpl
 * Related Repository: SupplierRepository
 * Related Entity    : Supplier
 * Related DTO       : SupplierRequest
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
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

/**
 * <b>PLUS33 Coffee ERP -- Procurement Module</b>
 *
 * <p><b>Class  :</b> {@code SupplierRequest}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.procurement.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Procurement Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Schema(description = "Supplier creation/update request payload")
public record SupplierRequest(
        @Schema(description = "Company ID mapping", example = "1")
        @NotNull(message = "Company ID is required")
        Long companyId,

        @Schema(description = "Unique code of the supplier within the company", example = "SUP-DXB-001")
        @NotBlank(message = "Supplier code is required")
        @Size(max = 50, message = "Supplier code cannot exceed 50 characters")
        String code,

        @Schema(description = "Name of the supplier", example = "Dubai Coffee Supply Co")
        @NotBlank(message = "Supplier name is required")
        @Size(max = 150, message = "Supplier name cannot exceed 150 characters")
        String name,

        @Schema(description = "Contact person name", example = "John Doe")
        @Size(max = 150, message = "Contact person name cannot exceed 150 characters")
        String contactPerson,

        @Schema(description = "Contact email of the supplier", example = "contact@dubaisupply.com")
        @Email(message = "Invalid email format")
        @Size(max = 150, message = "Email cannot exceed 150 characters")
        String email,

        @Schema(description = "Contact phone of the supplier", example = "+971-4-1234567")
        @Size(max = 30, message = "Phone cannot exceed 30 characters")
        String phone,

        @Schema(description = "Address of the supplier", example = "Al Quoz Industrial Area 3, Dubai")
        String address,

        @Schema(description = "Tax Registration Number (TRN)", example = "100254874500003")
        @Size(max = 100, message = "Tax registration number cannot exceed 100 characters")
        String taxNumber,

        @Schema(description = "Active status of the supplier", example = "true")
        Boolean active
) {}