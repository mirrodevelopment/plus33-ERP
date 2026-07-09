/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.budget.entity
 * File              : BudgetTemplateLine.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BudgetTemplateLineController
 * Related Service   : BudgetTemplateLineService, BudgetTemplateLineServiceImpl
 * Related Repository: BudgetTemplateLineRepository
 * Related Entity    : BudgetTemplateLine
 * Related DTO       : N/A
 * Related Mapper    : BudgetTemplateLineMapper
 * Related DB Table  : budget_template_lines
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BudgetTemplateLineRepository, BudgetTemplateLineMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'budget_template_lines'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.budget.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code BudgetTemplateLine}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.budget.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'budget_template_lines'.</p>
 *
 * <p><b>Database Table   :</b> {@code budget_template_lines}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "budget_template_lines")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BudgetTemplateLine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "template_id", nullable = false)
    private BudgetTemplate template;

    @Column(name = "account_code", nullable = false, length = 50)
    private String accountCode;

    @Column(name = "dimension_type", length = 50)
    private String dimensionType; // DEPARTMENT, COST_CENTER, PROJECT, WAREHOUSE, ASSET_CATEGORY

    @Builder.Default
    @Column(name = "distribution_percent", nullable = false, precision = 5, scale = 2)
    private BigDecimal distributionPercent = BigDecimal.ZERO;
}