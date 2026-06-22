package com.plus33.erp.sales.dto;

import com.plus33.erp.sales.entity.CustomerStatus;
import com.plus33.erp.sales.entity.CustomerType;
import com.plus33.erp.sales.entity.TaxProfile;
import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record CustomerRequest(
    @NotNull(message = "Company ID is required")
    Long companyId,

    String code,

    @NotBlank(message = "Customer name is required")
    @Size(max = 150, message = "Customer name cannot exceed 150 characters")
    String name,

    @NotNull(message = "Customer type is required")
    CustomerType customerType,

    CustomerStatus status,

    @Size(max = 150, message = "Contact person cannot exceed 150 characters")
    String contactPerson,

    @Email(message = "Invalid email format")
    @Size(max = 150, message = "Email cannot exceed 150 characters")
    String email,

    @Size(max = 30, message = "Phone cannot exceed 30 characters")
    String phone,

    String billingAddress,
    String shippingAddress,

    @Size(max = 100, message = "Tax number cannot exceed 100 characters")
    String taxNumber,

    TaxProfile taxProfile,

    @Min(value = 0, message = "Credit limit cannot be negative")
    BigDecimal creditLimit,

    @Min(value = 0, message = "Outstanding balance cannot be negative")
    BigDecimal outstandingBalance,

    @Size(max = 50, message = "Pricing tier cannot exceed 50 characters")
    String pricingTier,

    @DecimalMin(value = "0.0", message = "Discount rate cannot be negative")
    @DecimalMax(value = "100.0", message = "Discount rate cannot exceed 100%")
    BigDecimal discountRate,

    @Min(value = 0, message = "Payment terms days cannot be negative")
    Integer paymentTermsDays,

    @Size(max = 3, message = "Currency code must be 3 characters")
    String currencyCode
) {}
