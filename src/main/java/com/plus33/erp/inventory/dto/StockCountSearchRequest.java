/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.dto
 * File              : StockCountSearchRequest.java
 * Purpose           : Data Transfer Object for request/response in Inventory Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: StockCountSearchController
 * Related Service   : StockCountSearchService, StockCountSearchServiceImpl
 * Related Repository: StockCountSearchRepository
 * Related Entity    : StockCountSearch
 * Related DTO       : StockCountSearchRequest
 * Related Mapper    : StockCountSearchMapper
 * Related DB Table  : stock_count_searchs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : StockCountSearchController, StockCountSearchService, StockCountSearchServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Inventory Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.inventory.dto;

import com.plus33.erp.inventory.entity.StockCountStatus;
import com.plus33.erp.inventory.entity.StockCountType;
import lombok.Builder;
import java.time.LocalDate;
import java.util.UUID;

/**
 * <b>PLUS33 Coffee ERP -- Inventory Module</b>
 *
 * <p><b>Class  :</b> {@code StockCountSearchRequest}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.inventory.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Inventory Module.</p>
 *
 * <p><b>Module Deps      :</b> Inventory</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Builder
public record StockCountSearchRequest(
        StockCountStatus status,
        Long companyId,
        Long warehouseId,
        Long storeId,
        StockCountType countType,
        String countNumber,
        UUID clientReferenceId,
        LocalDate createdAtFrom,
        LocalDate createdAtTo,
        Long createdBy,
        Long assignedTo,
        Long productId
) {}