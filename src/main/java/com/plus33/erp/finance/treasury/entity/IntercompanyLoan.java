/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.entity
 * File              : IntercompanyLoan.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: IntercompanyLoanController
 * Related Service   : IntercompanyLoanService, IntercompanyLoanServiceImpl
 * Related Repository: IntercompanyLoanRepository
 * Related Entity    : IntercompanyLoan
 * Related DTO       : N/A
 * Related Mapper    : IntercompanyLoanMapper
 * Related DB Table  : intercompany_loans
 * Related REST APIs : N/A
 * Depends On        : Organization Module
 * Used By           : IntercompanyLoanRepository, IntercompanyLoanMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'intercompany_loans'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.entity;

import com.plus33.erp.organization.entity.Company;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code IntercompanyLoan}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.treasury.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'intercompany_loans'.</p>
 *
 * <p><b>Database Table   :</b> {@code intercompany_loans}</p>
 * <p><b>Module Deps      :</b> Organization</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "intercompany_loans")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class IntercompanyLoan {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "lender_company_id", nullable = false)
    private Company lenderCompany;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "borrower_company_id", nullable = false)
    private Company borrowerCompany;

    @Column(name = "principal_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal principalAmount;

    @Column(name = "interest_rate", nullable = false, precision = 5, scale = 2)
    private BigDecimal interestRate;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "maturity_date")
    private LocalDate maturityDate;

    @Column(name = "interest_accrued", nullable = false, precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal interestAccrued = BigDecimal.ZERO;

    @Column(nullable = false, length = 30)
    @Builder.Default
    private String status = "ACTIVE"; // ACTIVE, SETTLED, DEFAULTED

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    /**
     * Handles the create event or exception in the business workflow.
     *
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}