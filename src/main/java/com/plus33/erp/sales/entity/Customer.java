/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.entity
 * File              : Customer.java
 * Purpose           : JPA Entity representing a persistent database record in Sales Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CustomerController
 * Related Service   : CustomerService, CustomerServiceImpl
 * Related Repository: CustomerRepository
 * Related Entity    : Customer
 * Related DTO       : N/A
 * Related Mapper    : CustomerMapper
 * Related DB Table  : customers
 * Related REST APIs : N/A
 * Depends On        : Organization Module
 * Used By           : CustomerRepository, CustomerMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'customers'. Defines persistent domain object for Sales Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.sales.entity;

import com.plus33.erp.organization.entity.Company;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "customers", uniqueConstraints = {
    @UniqueConstraint(name = "uk_customer_company_code", columnNames = {"company_id", "code"}),
    @UniqueConstraint(name = "uk_customer_company_email", columnNames = {"company_id", "email"})
})
/**
 * <b>PLUS33 Coffee ERP -- Sales Module</b>
 *
 * <p><b>Class  :</b> {@code Customer}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.sales.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'customers'.</p>
 *
 * <p><b>Database Table   :</b> {@code customers}</p>
 * <p><b>Module Deps      :</b> Organization</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(nullable = false, length = 50)
    private String code;

    @Column(nullable = false, length = 150)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(name = "customer_type", nullable = false, length = 20)
    private CustomerType customerType;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private CustomerStatus status = CustomerStatus.ACTIVE;

    @Column(name = "contact_person", length = 150)
    private String contactPerson;

    @Column(length = 150)
    private String email;

    @Column(length = 30)
    private String phone;

    @Column(name = "billing_address", columnDefinition = "TEXT")
    private String billingAddress;

    @Column(name = "shipping_address", columnDefinition = "TEXT")
    private String shippingAddress;

    @Column(name = "tax_number", length = 100)
    private String taxNumber;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(name = "tax_profile", nullable = false, length = 50)
    private TaxProfile taxProfile = TaxProfile.STANDARD;

    @Builder.Default
    @Column(name = "credit_limit", nullable = false, precision = 12, scale = 2)
    private BigDecimal creditLimit = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "outstanding_balance", nullable = false, precision = 12, scale = 2)
    private BigDecimal outstandingBalance = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "pricing_tier", nullable = false, length = 50)
    private String pricingTier = "RETAIL";

    @Builder.Default
    @Column(name = "discount_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal discountRate = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "payment_terms_days", nullable = false)
    private Integer paymentTermsDays = 0;

    @Builder.Default
    @Column(name = "currency_code", nullable = false, length = 3)
    private String currencyCode = "INR";

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Version
    private Long version;

    /**
     * Handles the create event or exception in the business workflow.
     *
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    /**
     * Handles the update event or exception in the business workflow.
     *
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}