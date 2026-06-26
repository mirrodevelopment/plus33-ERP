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
@Table(name = "fiscal_years", uniqueConstraints = {
    @UniqueConstraint(name = "uk_fiscal_year_company", columnNames = {"company_id", "fiscal_year"})
})
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FiscalYear {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(name = "fiscal_year", nullable = false)
    private Integer fiscalYear;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    @Builder.Default
    private FiscalYearStatus status = FiscalYearStatus.OPEN;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "opening_journal_id")
    private JournalEntry openingJournal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "closing_journal_id")
    private JournalEntry closingJournal;

    @Column(name = "closed_by", length = 100)
    private String closedBy;

    @Column(name = "closed_at")
    private LocalDateTime closedAt;
}
