/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Ap Module
 * Package           : com.plus33.erp.ap.dto
 * File              : CashRequirementDTO.java
 * Purpose           : Data Transfer Object for request/response in Ap Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CashRequirementDTOController
 * Related Service   : CashRequirementDTOService, CashRequirementDTOServiceImpl
 * Related Repository: CashRequirementDTORepository
 * Related Entity    : CashRequirementDTO
 * Related DTO       : CashRequirementDTO
 * Related Mapper    : CashRequirementDTOMapper
 * Related DB Table  : cash_requirement_d_t_os
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CashRequirementDTOController, CashRequirementDTOService, CashRequirementDTOServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Ap Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.ap.dto;

import java.math.BigDecimal;

public record CashRequirementDTO(
        BigDecimal next30Days,
        BigDecimal next60Days,
        BigDecimal next90Days
) {}
