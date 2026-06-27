package com.plus33.erp.finance.budget.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "budget_snapshots")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BudgetSnapshot {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "budget_id", nullable = false)
    private Budget budget;

    @Column(name = "version_number", nullable = false)
    private Integer versionNumber;

    @Column(name = "snapshot_date", nullable = false, updatable = false)
    private LocalDateTime snapshotDate;

    @Column(name = "created_by", nullable = false, length = 100)
    private String createdBy;

    @Column(name = "trigger_event", nullable = false, length = 50)
    private String triggerEvent;

    @Column(length = 255)
    private String notes;

    @OneToMany(mappedBy = "snapshot", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<BudgetSnapshotLine> lines = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        snapshotDate = LocalDateTime.now();
    }
}
