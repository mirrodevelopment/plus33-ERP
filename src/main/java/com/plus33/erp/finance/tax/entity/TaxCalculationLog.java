package com.plus33.erp.finance.tax.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "tax_calculation_logs")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaxCalculationLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId;

    @Column(name = "document_type", nullable = false, length = 50)
    private String documentType;

    @Column(name = "document_id")
    private Long documentId;

    @Column(name = "request_payload", nullable = false, columnDefinition = "TEXT")
    private String requestPayload;

    @Column(name = "resolved_rule_id")
    private Long resolvedRuleId;

    @Column(name = "applied_rate_percent", precision = 5, scale = 2)
    private BigDecimal appliedRatePercent;

    @Column(name = "provider_name", length = 100)
    private String providerName;

    @Column(name = "calculated_tax_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal calculatedTaxAmount;

    @Builder.Default
    @Column(name = "override_applied", nullable = false)
    private boolean overrideApplied = false;

    @Column(name = "execution_duration_ms", nullable = false)
    private Long executionDurationMs;

    @Builder.Default
    @Column(name = "occurred_at", nullable = false)
    private LocalDateTime occurredAt = LocalDateTime.now();
}
