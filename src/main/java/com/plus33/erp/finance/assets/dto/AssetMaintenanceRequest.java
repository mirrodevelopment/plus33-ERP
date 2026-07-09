/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.assets.dto
 * File              : AssetMaintenanceRequest.java
 * Purpose           : Data Transfer Object for request/response in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: AssetMaintenanceController
 * Related Service   : AssetMaintenanceService, AssetMaintenanceServiceImpl
 * Related Repository: AssetMaintenanceRepository
 * Related Entity    : AssetMaintenance
 * Related DTO       : AssetMaintenanceRequest
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

public record AssetMaintenanceRequest(
    LocalDate maintenanceDate,
    String description,
    BigDecimal cost,
    Boolean capitalize,
    String performedBy
) {}
