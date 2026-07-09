/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.dto
 * File              : ProductRequest.java
 * Purpose           : Data Transfer Object for request/response in Inventory Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProductController
 * Related Service   : ProductService, ProductServiceImpl
 * Related Repository: ProductRepository
 * Related Entity    : Product
 * Related DTO       : ProductRequest
 * Related Mapper    : ProductMapper
 * Related DB Table  : products
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ProductController, ProductService, ProductServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Inventory Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.inventory.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Inventory Module</b>
 *
 * <p><b>Class  :</b> {@code ProductRequest}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.inventory.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Inventory Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Schema(description = "Product creation/update request details")
public record ProductRequest(
        @Schema(description = "Unique SKU / Code of the product", example = "RAW-ARA-001")
        @NotBlank(message = "Product code is required")
        @Size(max = 100, message = "Product code cannot exceed 100 characters")
        String code,

        @Schema(description = "Name of the product", example = "Arabica Beans")
        @NotBlank(message = "Product name is required")
        @Size(max = 150, message = "Product name cannot exceed 150 characters")
        String name,

        @Schema(description = "Category ID mapping", example = "1")
        @NotNull(message = "Category ID is required")
        Long categoryId,

        @Schema(description = "Unit of measure ID mapping", example = "1")
        @NotNull(message = "Unit of measure ID is required")
        Long unitId,

        @Schema(description = "Type of product (e.g. RAW_MATERIAL, FINISHED_GOOD)", example = "RAW_MATERIAL")
        @NotBlank(message = "Product type is required")
        @Size(max = 50, message = "Product type cannot exceed 50 characters")
        String productType,

        @Schema(description = "Reorder limit level", example = "10.00")
        BigDecimal reorderLevel,

        @Schema(description = "Status of the product", example = "true")
        Boolean active
) {}