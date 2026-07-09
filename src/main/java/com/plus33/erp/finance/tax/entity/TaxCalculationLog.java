/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.tax.entity
 * File              : TaxCalculationLog.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TaxCalculationLogController
 * Related Service   : TaxCalculationLogService, TaxCalculationLogServiceImpl
 * Related Repository: TaxCalculationLogRepository
 * Related Entity    : TaxCalculationLog
 * Related DTO       : N/A
 * Related Mapper    : TaxCalculationLogMapper
 * Related DB Table  : tax_calculation_logs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : TaxCalculationLogRepository, TaxCalculationLogMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'tax_calculation_logs'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.tax.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code TaxCalculationLog}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.tax.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'tax_calculation_logs'.</p>
 *
 * <p><b>Database Table   :</b> {@code tax_calculation_logs}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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