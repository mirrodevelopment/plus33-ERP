/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.dto
 * File              : DepartmentRequest.java
 * Purpose           : Data Transfer Object for request/response in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: DepartmentController
 * Related Service   : DepartmentService, DepartmentServiceImpl
 * Related Repository: DepartmentRepository
 * Related Entity    : Department
 * Related DTO       : DepartmentRequest
 * Related Mapper    : DepartmentMapper
 * Related DB Table  : departments
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : DepartmentController, DepartmentService, DepartmentServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Finance Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.finance.budget.dto;

public record DepartmentRequest(
    String code,
    String name,
    Boolean active
) {}
