/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.dto
 * File              : StockCountResponse.java
 * Purpose           : Data Transfer Object for request/response in Inventory Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: StockCountController
 * Related Service   : StockCountService, StockCountServiceImpl
 * Related Repository: StockCountRepository
 * Related Entity    : StockCount
 * Related DTO       : StockCountItemResponse, StockCountResponse
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

import com.plus33.erp.inventory.entity.StockCountStatus;
import com.plus33.erp.inventory.entity.StockCountType;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * <b>PLUS33 Coffee ERP -- Inventory Module</b>
 *
 * <p><b>Class  :</b> {@code StockCountResponse}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.inventory.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Inventory Module.</p>
 *
 * <p><b>Module Deps      :</b> Inventory</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public record StockCountResponse(
        Long id,
        String countNumber,
        Long companyId,
        Long warehouseId,
        Long storeId,
        StockCountStatus status,
        StockCountType countType,
        boolean blindCount,
        Long assignedToId,
        String assignedToName,
        Long adjustmentId,
        boolean approvalRequired,
        BigDecimal approvalThresholdPercentage,
        UUID clientReferenceId,
        String remarks,
        String rejectionReason,
        int recountNumber,
        Long createdById,
        LocalDateTime createdAt,
        Long assignedById,
        LocalDateTime assignedAt,
        Long startedById,
        LocalDateTime startedAt,
        Long submittedById,
        LocalDateTime submittedAt,
        Long approvedById,
        LocalDateTime approvedAt,
        Long postedById,
        LocalDateTime postedAt,
        Long closedById,
        LocalDateTime closedAt,
        List<StockCountItemResponse> items,
        Long version
) {}
