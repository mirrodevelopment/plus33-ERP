package com.plus33.erp.finance.tax.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "tax_filings")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaxFiling {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "calendar_id", nullable = false)
    private TaxCalendar calendar;

    @Column(name = "total_sales_amount", nullable = false, precision = 20, scale = 2)
    private BigDecimal totalSalesAmount;

    @Column(name = "total_input_tax", nullable = false, precision = 20, scale = 2)
    private BigDecimal totalInputTax;

    @Column(name = "total_output_tax", nullable = false, precision = 20, scale = 2)
    private BigDecimal totalOutputTax;

    @Column(name = "net_tax_liability", nullable = false, precision = 20, scale = 2)
    private BigDecimal netTaxLiability;

    @Column(name = "submission_payload", columnDefinition = "TEXT")
    private String submissionPayload;

    @Column(name = "government_receipt_ref", length = 100)
    private String governmentReceiptRef;

    @Column(name = "filed_by", nullable = false, length = 100)
    private String filedBy;

    @Builder.Default
    @Column(name = "filed_at", nullable = false)
    private LocalDateTime filedAt = LocalDateTime.now();
}
