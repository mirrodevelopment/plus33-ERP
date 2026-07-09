/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.entity
 * File              : BudgetTemplate.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BudgetTemplateController
 * Related Service   : BudgetTemplateService, BudgetTemplateServiceImpl
 * Related Repository: BudgetTemplateRepository
 * Related Entity    : BudgetTemplate
 * Related DTO       : N/A
 * Related Mapper    : BudgetTemplateMapper
 * Related DB Table  : budget_templates
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BudgetTemplateRepository, BudgetTemplateMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'budget_templates'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.budget.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code BudgetTemplate}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.budget.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'budget_templates'.</p>
 *
 * <p><b>Database Table   :</b> {@code budget_templates}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "budget_templates")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BudgetTemplate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String code;

    @Column(nullable = false, length = 150)
    private String name;

    @Column(length = 255)
    private String description;

    @Column(name = "industry_type", nullable = false, length = 50)
    private String industryType; // RETAIL, SERVICES, WAREHOUSE, MANUFACTURING, HEAD_OFFICE

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;

    @OneToMany(mappedBy = "template", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<BudgetTemplateLine> lines = new ArrayList<>();
}