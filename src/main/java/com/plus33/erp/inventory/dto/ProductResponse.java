/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.dto
 * File              : ProductResponse.java
 * Purpose           : Data Transfer Object for request/response in Inventory Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProductController
 * Related Service   : ProductService, ProductServiceImpl
 * Related Repository: ProductRepository
 * Related Entity    : Product
 * Related DTO       : ProductResponse
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
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Inventory Module</b>
 *
 * <p><b>Class  :</b> {@code ProductResponse}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.inventory.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Inventory Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Schema(description = "Product details response payload")
public record ProductResponse(
        @Schema(description = "Database ID of the product", example = "1")
        Long id,

        @Schema(description = "SKU / Code of the product", example = "RAW-ARA-001")
        String code,

        @Schema(description = "Name of the product", example = "Arabica Beans")
        String name,

        @Schema(description = "Category ID of the product", example = "1")
        Long categoryId,

        @Schema(description = "Category Name of the product", example = "Coffee Beans")
        String categoryName,

        @Schema(description = "Unit of measure ID of the product", example = "1")
        Long unitId,

        @Schema(description = "Unit of measure Code of the product", example = "KG")
        String unitCode,

        @Schema(description = "Type of product", example = "RAW_MATERIAL")
        String productType,

        @Schema(description = "Reorder limit level", example = "10.00")
        BigDecimal reorderLevel,

        @Schema(description = "Status of the product", example = "true")
        Boolean active,

        @Schema(description = "Creation date time", example = "2026-06-20T12:00:00")
        LocalDateTime createdAt,

        @Schema(description = "Last modification date time", example = "2026-06-20T12:30:00")
        LocalDateTime updatedAt,

        @Schema(description = "Online image URL of the product", example = "https://...")
        String imageUrl
) {}