/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.tax.entity
 * File              : TaxFiling.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TaxFilingController
 * Related Service   : TaxFilingService, TaxFilingServiceImpl
 * Related Repository: TaxFilingRepository
 * Related Entity    : TaxFiling
 * Related DTO       : N/A
 * Related Mapper    : TaxFilingMapper
 * Related DB Table  : tax_filings
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : TaxFilingRepository, TaxFilingMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'tax_filings'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.tax.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code TaxFiling}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.tax.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'tax_filings'.</p>
 *
 * <p><b>Database Table   :</b> {@code tax_filings}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
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