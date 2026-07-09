/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.entity
 * File              : ReconciliationRule.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ReconciliationRuleController
 * Related Service   : ReconciliationRuleService, ReconciliationRuleServiceImpl
 * Related Repository: ReconciliationRuleRepository
 * Related Entity    : ReconciliationRule
 * Related DTO       : N/A
 * Related Mapper    : ReconciliationRuleMapper
 * Related DB Table  : reconciliation_rules
 * Related REST APIs : N/A
 * Depends On        : Organization Module
 * Used By           : ReconciliationRuleRepository, ReconciliationRuleMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'reconciliation_rules'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.entity;

import com.plus33.erp.organization.entity.Company;
import jakarta.persistence.*;
import lombok.*;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code ReconciliationRule}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.treasury.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'reconciliation_rules'.</p>
 *
 * <p><b>Database Table   :</b> {@code reconciliation_rules}</p>
 * <p><b>Module Deps      :</b> Organization</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "reconciliation_rules")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReconciliationRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(name = "rule_name", nullable = false, length = 150)
    private String ruleName;

    @Column(name = "date_tolerance_days")
    @Builder.Default
    private Integer dateToleranceDays = 3;

    @Column(name = "reference_match_type", nullable = false, length = 30)
    @Builder.Default
    private String referenceMatchType = "EXACT"; // EXACT, PARTIAL, FUZZY, NONE

    @Column(name = "allow_many_to_one", nullable = false)
    @Builder.Default
    private Boolean allowManyToOne = false;

    @Column(name = "allow_one_to_many", nullable = false)
    @Builder.Default
    private Boolean allowOneToMany = false;

    @Column(name = "allow_splits", nullable = false)
    @Builder.Default
    private Boolean allowSplits = false;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;
}