package com.plus33.erp.sales.dto;

import com.plus33.erp.sales.entity.CustomerStatus;
import com.plus33.erp.sales.entity.CustomerType;
import com.plus33.erp.sales.entity.TaxProfile;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CustomerResponse(
    Long id,
    Long companyId,
    String companyName,
    String code,
    String name,
    CustomerType customerType,
    CustomerStatus status,
    String contactPerson,
    String email,
    String phone,
    String billingAddress,
    String shippingAddress,
    String taxNumber,
    TaxProfile taxProfile,
    BigDecimal creditLimit,
    BigDecimal outstandingBalance,
    String pricingTier,
    BigDecimal discountRate,
    Integer paymentTermsDays,
    String currencyCode,
    LocalDateTime createdAt,
    LocalDateTime updatedAt,
    Long version
) {}
