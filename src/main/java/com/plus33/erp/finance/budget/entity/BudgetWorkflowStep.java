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
