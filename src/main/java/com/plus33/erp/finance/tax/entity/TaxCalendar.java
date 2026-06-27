package com.plus33.erp.finance.tax.entity;

import com.plus33.erp.organization.entity.Company;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "tax_calendar")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaxCalendar {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(name = "filing_type", nullable = false, length = 50)
    private String filingType; // VAT_RETURN, GST_RETURN, WHT_RETURN

    @Column(name = "period_start", nullable = false)
    private LocalDate periodStart;

    @Column(name = "period_end", nullable = false)
    private LocalDate periodEnd;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Builder.Default
    @Column(name = "status", nullable = false, length = 30)
    private String status = "DRAFT"; // DRAFT, CALCULATED, REVIEWED, APPROVED, SUBMITTED, ACCEPTED, REJECTED, AMENDED
}
