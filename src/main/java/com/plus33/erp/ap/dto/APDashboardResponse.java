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
