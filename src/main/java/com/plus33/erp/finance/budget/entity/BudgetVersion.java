package com.plus33.erp.finance.budget.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "budget_versions", uniqueConstraints = {
    @UniqueConstraint(name = "uk_versions_budget_code", columnNames = {"budget_id", "version_code"})
})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BudgetVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "budget_id", nullable = false)
    private Budget budget;

    @Column(name = "version_code", nullable = false, length = 30)
    private String versionCode;

    @Column(length = 255)
    private String description;

    @Builder.Default
    @Column(nullable = false, length = 30)
    private String status = "DRAFT";

    @Column(name = "created_by", nullable = false, length = 100)
    private String createdBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
