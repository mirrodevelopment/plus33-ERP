package com.plus33.erp.finance.tax.entity;

import com.plus33.erp.finance.entity.Account;
import com.plus33.erp.organization.entity.Company;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "tax_adjustment_entries")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaxAdjustmentEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(name = "adjustment_date", nullable = false)
    private LocalDate adjustmentDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tax_category_id", nullable = false)
    private TaxCategory taxCategory;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "gl_account_id", nullable = false)
    private Account glAccount;

    @Builder.Default
    @Column(name = "debit_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal debitAmount = BigDecimal.ZERO;

    @Builder.Default
    @Column(name = "credit_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal creditAmount = BigDecimal.ZERO;

    @Column(name = "description", nullable = false, length = 255)
    private String description;

    @Column(name = "reason_code", nullable = false, length = 50)
    private String reasonCode; // AUDIT_CORRECTION, MANUAL_ADJUSTMENT, GOVERNMENT_REASSESSMENT

    @Builder.Default
    @Column(name = "status", nullable = false, length = 30)
    private String status = "DRAFT"; // DRAFT, APPROVED, POSTED

    @Column(name = "approved_by", length = 100)
    private String approvedBy;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;
}
