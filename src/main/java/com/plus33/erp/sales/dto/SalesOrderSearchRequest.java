package com.plus33.erp.sales.dto;

import com.plus33.erp.sales.entity.SalesOrderStatus;

import java.time.LocalDate;

public record SalesOrderSearchRequest(
    String query,
    Long companyId,
    Long customerId,
    SalesOrderStatus status,
    String customerType,
    Boolean creditOverride,
    LocalDate requestedDeliveryDateFrom,
    LocalDate requestedDeliveryDateTo,
    Long createdBy,
    Long approvedBy
) {}
