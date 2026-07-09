/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.dto
 * File              : ProjectRequest.java
 * Purpose           : Data Transfer Object for request/response in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProjectController
 * Related Service   : ProjectService, ProjectServiceImpl
 * Related Repository: ProjectRepository
 * Related Entity    : Project
 * Related DTO       : ProjectRequest
 * Related Mapper    : ProjectMapper
 * Related DB Table  : projects
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ProjectController, ProjectService, ProjectServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Finance Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.finance.budget.dto;

import java.time.LocalDate;

public record ProjectRequest(
    String code,
    String name,
    LocalDate startDate,
    LocalDate endDate,
    String status,
    Boolean active
) {}
