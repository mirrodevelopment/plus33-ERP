/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.dto
 * File              : StockCountRequest.java
 * Purpose           : Data Transfer Object for request/response in Inventory Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: StockCountController
 * Related Service   : StockCountService, StockCountServiceImpl
 * Related Repository: StockCountRepository
 * Related Entity    : StockCount
 * Related DTO       : StockCountRequest
 * Related Mapper    : StockCountMapper
 * Related DB Table  : stock_counts
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : StockCountController, StockCountService, StockCountServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Inventory Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.inventory.dto;

import com.plus33.erp.inventory.entity.StockCountType;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

/**
 * <b>PLUS33 Coffee ERP -- Inventory Module</b>
 *
 * <p><b>Class  :</b> {@code StockCountRequest}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.inventory.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Inventory Module.</p>
 *
 * <p><b>Module Deps      :</b> Inventory</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public record StockCountRequest(
        @NotNull(message = "Company ID is required")
        Long companyId,

        Long warehouseId,
        Long storeId,

        @NotNull(message = "Count type is required")
        StockCountType countType,

        Boolean blindCount,

        @DecimalMin(value = "0.00", message = "Approval threshold percentage must be at least 0%")
        @DecimalMax(value = "100.00", message = "Approval threshold percentage cannot exceed 100%")
        BigDecimal approvalThresholdPercentage,

        @NotNull(message = "Client reference ID is required")
        UUID clientReferenceId,

        String remarks,

        List<Long> cycleProductIds
) {}
