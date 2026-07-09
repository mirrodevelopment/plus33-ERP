/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.dto
 * File              : CostCenterRequest.java
 * Purpose           : Data Transfer Object for request/response in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CostCenterController
 * Related Service   : CostCenterService, CostCenterServiceImpl
 * Related Repository: CostCenterRepository
 * Related Entity    : CostCenter
 * Related DTO       : CostCenterRequest
 * Related Mapper    : CostCenterMapper
 * Related DB Table  : cost_centers
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CostCenterController, CostCenterService, CostCenterServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Finance Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.finance.budget.dto;

public record CostCenterRequest(
    String code,
    String name,
    Boolean active
) {}
