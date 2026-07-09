/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.dto
 * File              : StockCountUpdateRequest.java
 * Purpose           : Data Transfer Object for request/response in Inventory Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: StockCountUpdateController
 * Related Service   : StockCountUpdateService, StockCountUpdateServiceImpl
 * Related Repository: StockCountUpdateRepository
 * Related Entity    : StockCountUpdate
 * Related DTO       : StockCountUpdateRequest
 * Related Mapper    : StockCountUpdateMapper
 * Related DB Table  : stock_count_updates
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : StockCountUpdateController, StockCountUpdateService, StockCountUpdateServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Inventory Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.inventory.dto;

import com.plus33.erp.inventory.entity.StockCountType;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Inventory Module</b>
 *
 * <p><b>Class  :</b> {@code StockCountUpdateRequest}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.inventory.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Inventory Module.</p>
 *
 * <p><b>Module Deps      :</b> Inventory</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public record StockCountUpdateRequest(
        Long warehouseId,
        Long storeId,

        @NotNull(message = "Count type is required")
        StockCountType countType,

        Boolean blindCount,

        @DecimalMin(value = "0.00", message = "Approval threshold percentage must be at least 0%")
        @DecimalMax(value = "100.00", message = "Approval threshold percentage cannot exceed 100%")
        BigDecimal approvalThresholdPercentage,

        String remarks,

        List<Long> cycleProductIds
) {}
