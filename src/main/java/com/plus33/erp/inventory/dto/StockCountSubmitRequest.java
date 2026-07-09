/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.dto
 * File              : StockCountSubmitRequest.java
 * Purpose           : Data Transfer Object for request/response in Inventory Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: StockCountSubmitController
 * Related Service   : StockCountSubmitService, StockCountSubmitServiceImpl
 * Related Repository: StockCountSubmitRepository
 * Related Entity    : StockCountSubmit
 * Related DTO       : StockCountItemCountRequest, StockCountSubmitRequest
 * Related Mapper    : StockCountSubmitMapper
 * Related DB Table  : stock_count_submits
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : StockCountSubmitController, StockCountSubmitService, StockCountSubmitServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Inventory Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.inventory.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Inventory Module</b>
 *
 * <p><b>Class  :</b> {@code StockCountSubmitRequest}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.inventory.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Inventory Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public record StockCountSubmitRequest(
        @NotEmpty(message = "Items list cannot be empty")
        @Valid
        List<StockCountItemCountRequest> items
) {}
