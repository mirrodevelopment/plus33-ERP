package com.plus33.erp.finance.budget.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

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
