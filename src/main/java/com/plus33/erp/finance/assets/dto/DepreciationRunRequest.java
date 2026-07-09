/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.assets.dto
 * File              : DepreciationRunRequest.java
 * Purpose           : Data Transfer Object for request/response in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: DepreciationRunController
 * Related Service   : DepreciationRunService, DepreciationRunServiceImpl
 * Related Repository: DepreciationRunRepository
 * Related Entity    : DepreciationRun
 * Related DTO       : DepreciationRunRequest
 * Related Mapper    : DepreciationRunMapper
 * Related DB Table  : depreciation_runs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : DepreciationRunController, DepreciationRunService, DepreciationRunServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Finance Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.finance.assets.dto;

import java.time.LocalDate;

public record DepreciationRunRequest(
    LocalDate depreciationDate,
    Boolean dryRun
) {}
