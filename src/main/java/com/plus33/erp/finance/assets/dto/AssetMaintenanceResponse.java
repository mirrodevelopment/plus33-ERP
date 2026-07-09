/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.assets.dto
 * File              : AssetMaintenanceResponse.java
 * Purpose           : Data Transfer Object for request/response in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: AssetMaintenanceController
 * Related Service   : AssetMaintenanceService, AssetMaintenanceServiceImpl
 * Related Repository: AssetMaintenanceRepository
 * Related Entity    : AssetMaintenance
 * Related DTO       : AssetMaintenanceResponse
 * Related Mapper    : AssetMaintenanceMapper
 * Related DB Table  : asset_maintenances
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : AssetMaintenanceController, AssetMaintenanceService, AssetMaintenanceServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Finance Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.finance.assets.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code AssetMaintenanceResponse}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.assets.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Finance Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public record AssetMaintenanceResponse(
    Long id,
    Long fixedAssetId,
    LocalDate maintenanceDate,
    String description,
    BigDecimal cost,
    Boolean capitalize,
    Long journalEntryId,
    String performedBy,
    LocalDateTime createdAt
) {}
