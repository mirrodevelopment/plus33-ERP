/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.assets.dto
 * File              : DepreciationRunResponse.java
 * Purpose           : Data Transfer Object for request/response in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: DepreciationRunController
 * Related Service   : DepreciationRunService, DepreciationRunServiceImpl
 * Related Repository: DepreciationRunRepository
 * Related Entity    : DepreciationRun
 * Related DTO       : DepreciationRunResponse
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

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code DepreciationRunResponse}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.assets.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Finance Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public record DepreciationRunResponse(
    LocalDate targetDate,
    Boolean dryRun,
    Integer assetsProcessedCount,
    BigDecimal totalDepreciationAmount,
    List<DepreciationPreviewEntry> previewEntries,
    List<ProjectedJournalEntry> projectedJournalEntries
) {}
