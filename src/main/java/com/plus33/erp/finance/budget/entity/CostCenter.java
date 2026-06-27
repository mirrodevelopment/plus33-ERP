package com.plus33.erp.finance.budget.entity;

import com.plus33.erp.organization.entity.Company;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "cost_centers", uniqueConstraints = {
    @UniqueConstraint(name = "uk_cost_centers_company_code", columnNames = {"company_id", "code"})
})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CostCenter {

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
}
