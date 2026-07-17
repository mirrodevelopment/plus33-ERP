/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.dto
 * File              : EmployeeResponse.java
 * Purpose           : Data Transfer Object for request/response in Workforce Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: EmployeeController
 * Related Service   : EmployeeService, EmployeeServiceImpl
 * Related Repository: EmployeeRepository
 * Related Entity    : Employee
 * Related DTO       : EmployeeResponse
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
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Workforce Module</b>
 *
 * <p><b>Class  :</b> {@code EmployeeResponse}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.workforce.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Workforce Module.</p>
 *
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Schema(description = "Employee details response payload")
public record EmployeeResponse(
        @Schema(description = "Database ID of the employee", example = "1")
        Long id,

        @Schema(description = "Unique code of the employee", example = "EMP_001")
        String employeeCode,

        @Schema(description = "First name of the employee", example = "John")
        String firstName,

        @Schema(description = "Last name of the employee", example = "Doe")
        String lastName,

        @Schema(description = "Email address of the employee", example = "john.doe@plus33.com")
        String email,

        @Schema(description = "Phone number of the employee", example = "+971500000000")
        String phone,

        @Schema(description = "Company ID mapping", example = "1")
        Long companyId,

        @Schema(description = "Company Name of the employee", example = "PLUS33 Global Corp")
        String companyName,

        @Schema(description = "Company Code of the employee", example = "PLUS33_GLOBAL")
        String companyCode,

        @Schema(description = "Region ID mapping", example = "1")
        Long regionId,

        @Schema(description = "Region Name of the employee", example = "UAE Region")
        String regionName,

        @Schema(description = "Region Code of the employee", example = "UAE_REGION")
        String regionCode,

        @Schema(description = "Store ID mapping", example = "1")
        Long storeId,

        @Schema(description = "Store Name of the employee", example = "Dubai Mall Store")
        String storeName,

        @Schema(description = "Store Code of the employee", example = "DUBAI_MALL_STORE")
        String storeCode,

        @Schema(description = "User ID mapping", example = "1")
        Long userId,

        @Schema(description = "User Email/Username", example = "admin@plus33.com")
        String userEmail,

        @Schema(description = "Designation of the employee", example = "Store Manager")
        String designation,

        @Schema(description = "Department of the employee", example = "Operations")
        String department,

        @Schema(description = "Employment type (e.g. FULL_TIME, PART_TIME)", example = "FULL_TIME")
        String employmentType,

        @Schema(description = "Hire date", example = "2026-01-01")
        LocalDate hireDate,

        @Schema(description = "Status of the employee (e.g. ACTIVE, TERMINATED)", example = "ACTIVE")
        String status,

        @Schema(description = "Active status of the employee", example = "true")
        Boolean active,

        @Schema(description = "Monthly basic salary amount", example = "2400.00")
        BigDecimal salary,

        @Schema(description = "Currently active shift name and timing", example = "Morning Shift (06:00 - 14:00)")
        String activeShift,

        @Schema(description = "Attendance status today (e.g. PRESENT, ABSENT, ON_LEAVE)", example = "PRESENT")
        String todayStatus,

        @Schema(description = "Dynamic profile image URL of the employee", example = "imgs/avatars/EMP_001_profile_img.png")
        String avatarUrl,

        @Schema(description = "Creation date time", example = "2026-06-20T12:00:00")
        LocalDateTime createdAt,

        @Schema(description = "Last modification date time", example = "2026-06-20T12:30:00")
        LocalDateTime updatedAt
) {}