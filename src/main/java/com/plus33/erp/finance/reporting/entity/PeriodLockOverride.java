package com.plus33.erp.finance.reporting.entity;

import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.finance.entity.JournalEntry;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "period_lock_overrides")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PeriodLockOverride {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(name = "user_email", nullable = false, length = 100)
    private String userEmail;

    @Column(name = "override_at", nullable = false)
    @Builder.Default
    private LocalDateTime overrideAt = LocalDateTime.now();

    @Column(name = "original_lock_date", nullable = false)
    private LocalDate originalLockDate;

    @Column(name = "transaction_date", nullable = false)
    private LocalDate transactionDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "journal_entry_id")
    private JournalEntry journalEntry;

    @Column(length = 255)
    private String reason;
}
