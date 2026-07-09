/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.dto
 * File              : EmployeeRequest.java
 * Purpose           : Data Transfer Object for request/response in Workforce Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: EmployeeController
 * Related Service   : EmployeeService, EmployeeServiceImpl
 * Related Repository: EmployeeRepository
 * Related Entity    : Employee
 * Related DTO       : EmployeeRequest
 * Related Mapper    : EmployeeMapper
 * Related DB Table  : employees
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : EmployeeController, EmployeeService, EmployeeServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Workforce Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.workforce.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;

/**
 * <b>PLUS33 Coffee ERP -- Workforce Module</b>
 *
 * <p><b>Class  :</b> {@code EmployeeRequest}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.workforce.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Workforce Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Schema(description = "Employee creation/update request details")
public record EmployeeRequest(
        @Schema(description = "Unique code of the employee", example = "EMP_001")
        @NotBlank(message = "Employee code is required")
        @Size(max = 50, message = "Employee code cannot exceed 50 characters")
        String employeeCode,

        @Schema(description = "First name of the employee", example = "John")
        @NotBlank(message = "First name is required")
        @Size(max = 100, message = "First name cannot exceed 100 characters")
        String firstName,

        @Schema(description = "Last name of the employee", example = "Doe")
        @Size(max = 100, message = "Last name cannot exceed 100 characters")
        String lastName,

        @Schema(description = "Email address of the employee", example = "john.doe@plus33.com")
        @Email(message = "Invalid email format")
        @Size(max = 150, message = "Email cannot exceed 150 characters")
        String email,

        @Schema(description = "Phone number of the employee", example = "+971500000000")
        @Size(max = 30, message = "Phone number cannot exceed 30 characters")
        String phone,

        @Schema(description = "Company ID mapping", example = "1")
        @NotNull(message = "Company ID mapping is required")
        Long companyId,

        @Schema(description = "Region ID mapping", example = "1")
        Long regionId,

        @Schema(description = "Store ID mapping", example = "1")
        Long storeId,

        @Schema(description = "User ID mapping", example = "1")
        Long userId,

        @Schema(description = "Designation of the employee", example = "Store Manager")
        @NotBlank(message = "Designation is required")
        @Size(max = 100, message = "Designation cannot exceed 100 characters")
        String designation,

        @Schema(description = "Department of the employee", example = "Operations")
        @Size(max = 100, message = "Department cannot exceed 100 characters")
        String department,

        @Schema(description = "Employment type (e.g. FULL_TIME, PART_TIME)", example = "FULL_TIME")
        @NotBlank(message = "Employment type is required")
        @Size(max = 50, message = "Employment type cannot exceed 50 characters")
        String employmentType,

        @Schema(description = "Hire date", example = "2026-01-01")
        @NotNull(message = "Hire date is required")
        LocalDate hireDate,

        @Schema(description = "Active status of the employee", example = "true")
        Boolean active
) {}