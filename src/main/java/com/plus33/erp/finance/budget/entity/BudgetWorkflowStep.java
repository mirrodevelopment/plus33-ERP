/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.entity
 * File              : BudgetWorkflowStep.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BudgetWorkflowStepController
 * Related Service   : BudgetWorkflowStepService, BudgetWorkflowStepServiceImpl
 * Related Repository: BudgetWorkflowStepRepository
 * Related Entity    : BudgetWorkflowStep
 * Related DTO       : N/A
 * Related Mapper    : BudgetWorkflowStepMapper
 * Related DB Table  : budget_workflow_steps
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BudgetWorkflowStepRepository, BudgetWorkflowStepMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'budget_workflow_steps'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.budget.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "budget_workflow_steps", uniqueConstraints = {
    @UniqueConstraint(name = "uk_wf_steps_seq", columnNames = {"template_id", "step_sequence"})
})
/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code BudgetWorkflowStep}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.budget.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'budget_workflow_steps'.</p>
 *
 * <p><b>Database Table   :</b> {@code budget_workflow_steps}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BudgetWorkflowStep {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", nullable = false)
    private BudgetWorkflowTemplate template;

    @Column(name = "step_sequence", nullable = false)
    private Integer stepSequence;

    @Column(name = "role_code", nullable = false, length = 50)
    private String roleCode;

    @Builder.Default
    @Column(name = "min_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal minAmount = BigDecimal.ZERO;

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;
}