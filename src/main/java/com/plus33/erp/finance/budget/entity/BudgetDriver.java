package com.plus33.erp.finance.budget.entity;

import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.finance.reporting.entity.FiscalYear;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "budget_drivers", uniqueConstraints = {
    @UniqueConstraint(name = "uk_drivers_company_fy_code", columnNames = {"company_id", "fiscal_year_id", "code"})
})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BudgetDriver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fiscal_year_id", nullable = false)
    private FiscalYear fiscalYear;

    @Column(nullable = false, length = 50)
    private String code;

    @Column(nullable = false, length = 150)
    private String name;

    @Builder.Default
    @Column(name = "driver_value", nullable = false, precision = 15, scale = 4)
    private BigDecimal driverValue = BigDecimal.ZERO;

    @Column(nullable = false, length = 30)
    private String unit;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
