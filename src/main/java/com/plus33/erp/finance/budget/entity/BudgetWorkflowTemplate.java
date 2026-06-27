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
