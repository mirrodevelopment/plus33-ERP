/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Ap Module
 * Package           : com.plus33.erp.ap.dto
 * File              : APDashboardResponse.java
 * Purpose           : Data Transfer Object for request/response in Ap Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: APDashboardController
 * Related Service   : APDashboardService, APDashboardServiceImpl
 * Related Repository: APDashboardRepository
 * Related Entity    : APDashboard
 * Related DTO       : APDashboardResponse, CashRequirementDTO, TopSupplierDTO
 * Related Mapper    : APDashboardMapper
 * Related DB Table  : a_p_dashboards
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : APDashboardController, APDashboardService, APDashboardServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Ap Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.ap.dto;

import java.math.BigDecimal;
import java.util.List;

public record APDashboardResponse(
        Long companyId,
        BigDecimal totalPayables,
        BigDecimal currentDue,
        BigDecimal overdue,
        BigDecimal dueToday,
        BigDecimal dueThisWeek,
        BigDecimal paidThisMonth,
        Long openBills,
        BigDecimal averagePaymentDays,
        List<TopSupplierDTO> topSuppliers,
        CashRequirementDTO cashRequirement,
        Long billsAwaitingApproval,
        Long billsDueToday
) {}
