/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.dto
 * File              : CustomerResponse.java
 * Purpose           : Data Transfer Object for request/response in Sales Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CustomerController
 * Related Service   : CustomerService, CustomerServiceImpl
 * Related Repository: CustomerRepository
 * Related Entity    : Customer
 * Related DTO       : CustomerResponse
 * Related Mapper    : CustomerMapper
 * Related DB Table  : customers
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CustomerController, CustomerService, CustomerServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * DTO for Sales Module HTTP serialization. Annotated with Jakarta Bean Validation constraints.
 ******************************************************************************/
package com.plus33.erp.sales.dto;

import com.plus33.erp.sales.entity.CustomerStatus;
import com.plus33.erp.sales.entity.CustomerType;
import com.plus33.erp.sales.entity.TaxProfile;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Sales Module</b>
 *
 * <p><b>Class  :</b> {@code CustomerResponse}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.sales.dto}</p>
 * <p><b>Layer  :</b> Java Record: immutable value object / data carrier for Sales Module.</p>
 *
 * <p><b>Module Deps      :</b> Sales</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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
