package com.plus33.erp.finance.budget.entity;

import com.plus33.erp.organization.entity.Company;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "budget_policies", uniqueConstraints = {
    @UniqueConstraint(name = "uk_policies_company_code", columnNames = {"company_id", "code"})
})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BudgetPolicy {

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
    @Column(name = "control_type", nullable = false, length = 30)
    private String controlType = "HARD"; // HARD, SOFT, NONE

    @Builder.Default
    @Column(name = "allow_negative", nullable = false)
    private Boolean allowNegative = false;

    @Builder.Default
    @Column(name = "allow_transfers", nullable = false)
    private Boolean allowTransfers = true;

    @Builder.Default
    @Column(name = "allow_revisions", nullable = false)
    private Boolean allowRevisions = true;

    @Builder.Default
    @Column(name = "auto_reserve", nullable = false)
    private Boolean autoReserve = true;

    @Builder.Default
    @Column(name = "auto_consume", nullable = false)
    private Boolean autoConsume = true;

    @Builder.Default
    @Column(name = "approval_required", nullable = false)
    private Boolean approvalRequired = true;

    @Builder.Default
    @Column(name = "multi_currency_enabled", nullable = false)
    private Boolean multiCurrencyEnabled = false;

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;
}
