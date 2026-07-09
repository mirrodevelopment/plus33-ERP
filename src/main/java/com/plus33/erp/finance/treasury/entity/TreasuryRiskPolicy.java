/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.entity
 * File              : TreasuryRiskPolicy.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TreasuryRiskPolicyController
 * Related Service   : TreasuryRiskPolicyService, TreasuryRiskPolicyServiceImpl
 * Related Repository: TreasuryRiskPolicyRepository
 * Related Entity    : TreasuryRiskPolicy
 * Related DTO       : N/A
 * Related Mapper    : TreasuryRiskPolicyMapper
 * Related DB Table  : treasury_risk_policies
 * Related REST APIs : N/A
 * Depends On        : Organization Module
 * Used By           : TreasuryRiskPolicyRepository, TreasuryRiskPolicyMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'treasury_risk_policies'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.entity;

import com.plus33.erp.organization.entity.Company;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Configurable treasury risk policy rule.
 * Supports a hierarchy of limit categories: PAYMENT, FX, INVESTMENT,
 * COUNTERPARTY, BANK, LIQUIDITY, CONCENTRATION, REGULATORY.
 *
 * A policy can be scoped to a company, specific currency, bank, counterparty,
 * or investment type. The engine evaluates the most specific matching rule first.
 */
@Getter
@Setter
@Entity
@Table(name = "treasury_risk_policies", indexes = {
    @Index(name = "idx_trp_company_category", columnList = "company_id, policy_category"),
    @Index(name = "idx_trp_active", columnList = "active, effective_from")
})
/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code TreasuryRiskPolicy}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.treasury.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'treasury_risk_policies'.</p>
 *
 * <p><b>Database Table   :</b> {@code treasury_risk_policies}</p>
 * <p><b>Module Deps      :</b> Organization</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TreasuryRiskPolicy {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    /** PAYMENT | FX | INVESTMENT | COUNTERPARTY | BANK | LIQUIDITY | CONCENTRATION | REGULATORY */
    @Column(name = "policy_category", nullable = false, length = 50)
    private String policyCategory;

    /** More specific rule name within category, e.g. DAILY_OUTFLOW, SINGLE_PAYMENT, CURRENCY_EXPOSURE */
    @Column(name = "policy_name", nullable = false, length = 100)
    private String policyName;

    /** Optional currency scope */
    @Column(name = "currency_code", length = 3)
    private String currencyCode;

    /** Optional bank scope */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "bank_id")
    private Bank bank;

    /** Optional counterparty scope (stored as external party reference) */
    @Column(name = "counterparty_ref", length = 100)
    private String counterpartyRef;

    /** Optional investment type scope */
    @Column(name = "investment_type", length = 50)
    private String investmentType;

    @Column(name = "limit_amount", nullable = false, precision = 20, scale = 2)
    private BigDecimal limitAmount;

    /** 0–100 percentage threshold that triggers WARNING alert */
    @Column(name = "warning_threshold_pct", nullable = false, precision = 5, scale = 2)
    @Builder.Default
    private BigDecimal warningThresholdPct = new BigDecimal("80.00");

    /** HARD_BLOCK | SOFT_WARN | NOTIFY */
    @Column(name = "breach_action", nullable = false, length = 20)
    @Builder.Default
    private String breachAction = "HARD_BLOCK";

    @Column(name = "effective_from", nullable = false)
    private LocalDate effectiveFrom;

    @Column(name = "effective_to")
    private LocalDate effectiveTo;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;

    @Column(name = "created_by", nullable = false, length = 100)
    private String createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

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