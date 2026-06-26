package com.plus33.erp.finance.reporting.entity;

import com.plus33.erp.organization.entity.Company;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "period_locks")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PeriodLock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false, unique = true)
    private Company company;

    @Column(name = "lock_date", nullable = false)
    private LocalDate lockDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "lock_type", nullable = false, length = 30)
    @Builder.Default
    private PeriodLockType lockType = PeriodLockType.HARD;

    @Column(name = "locked_by", nullable = false, length = 100)
    private String lockedBy;

    @Column(name = "locked_at", nullable = false)
    @Builder.Default
    private LocalDateTime lockedAt = LocalDateTime.now();

    @Column(length = 255)
    private String reason;
}
