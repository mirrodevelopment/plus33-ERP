package com.plus33.erp.sales.dto;

import com.plus33.erp.sales.entity.CustomerStatus;
import com.plus33.erp.sales.entity.CustomerType;

public record CustomerSearchRequest(
    String query,
    Long companyId,
    CustomerType customerType,
    String pricingTier,
    CustomerStatus status,
    Integer paymentTermsDays,
    Boolean activeOnly
) {}
