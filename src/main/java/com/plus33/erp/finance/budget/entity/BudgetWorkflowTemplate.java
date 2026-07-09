/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.entity
 * File              : BudgetWorkflowTemplate.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BudgetWorkflowTemplateController
 * Related Service   : BudgetWorkflowTemplateService, BudgetWorkflowTemplateServiceImpl
 * Related Repository: BudgetWorkflowTemplateRepository
 * Related Entity    : BudgetWorkflowTemplate
 * Related DTO       : N/A
 * Related Mapper    : BudgetWorkflowTemplateMapper
 * Related DB Table  : budget_workflow_templates
 * Related REST APIs : N/A
 * Depends On        : Organization Module
 * Used By           : BudgetWorkflowTemplateRepository, BudgetWorkflowTemplateMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'budget_workflow_templates'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.budget.entity;

import com.plus33.erp.organization.entity.Company;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "budget_workflow_templates", uniqueConstraints = {
    @UniqueConstraint(name = "uk_wf_templates_company_code", columnNames = {"company_id", "code"})
})
/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code BudgetWorkflowTemplate}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.budget.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'budget_workflow_templates'.</p>
 *
 * <p><b>Database Table   :</b> {@code budget_workflow_templates}</p>
 * <p><b>Module Deps      :</b> Organization</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BudgetWorkflowTemplate {

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

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;

    @OneToMany(mappedBy = "template", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    @OrderBy("stepSequence ASC")
    private List<BudgetWorkflowStep> steps = new ArrayList<>();
}