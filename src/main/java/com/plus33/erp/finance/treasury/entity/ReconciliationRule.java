package com.plus33.erp.finance.treasury.entity;

import com.plus33.erp.organization.entity.Company;
import jakarta.persistence.*;
import lombok.*;

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
