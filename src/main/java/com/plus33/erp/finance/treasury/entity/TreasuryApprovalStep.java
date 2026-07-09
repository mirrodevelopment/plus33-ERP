/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.entity
 * File              : TreasuryApprovalStep.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TreasuryApprovalStepController
 * Related Service   : TreasuryApprovalStepService, TreasuryApprovalStepServiceImpl
 * Related Repository: TreasuryApprovalStepRepository
 * Related Entity    : TreasuryApprovalStep
 * Related DTO       : N/A
 * Related Mapper    : TreasuryApprovalStepMapper
 * Related DB Table  : treasury_approval_steps
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : TreasuryApprovalStepRepository, TreasuryApprovalStepMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'treasury_approval_steps'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code TreasuryApprovalStep}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.treasury.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'treasury_approval_steps'.</p>
 *
 * <p><b>Database Table   :</b> {@code treasury_approval_steps}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "treasury_approval_steps")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TreasuryApprovalStep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "step_sequence", nullable = false)
    private Integer stepSequence;

    @Column(name = "role_code", nullable = false, length = 50)
    private String roleCode;

    @Column(name = "min_amount", nullable = false, precision = 15, scale = 2)
    @Builder.Default
    private BigDecimal minAmount = BigDecimal.ZERO;

    @Column(name = "max_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal maxAmount;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;
}