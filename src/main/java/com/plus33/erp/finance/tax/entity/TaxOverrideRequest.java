/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.tax.entity
 * File              : TaxOverrideRequest.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TaxOverrideController
 * Related Service   : TaxOverrideService, TaxOverrideServiceImpl
 * Related Repository: TaxOverrideRepository
 * Related Entity    : TaxOverrideRequest
 * Related DTO       : TaxOverrideRequest
 * Related Mapper    : TaxOverrideMapper
 * Related DB Table  : tax_override_requests
 * Related REST APIs : N/A
 * Depends On        : Organization Module
 * Used By           : TaxOverrideRepository, TaxOverrideMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'tax_override_requests'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.tax.entity;

import com.plus33.erp.organization.entity.Company;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code TaxOverrideRequest}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.tax.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'tax_override_requests'.</p>
 *
 * <p><b>Database Table   :</b> {@code tax_override_requests}</p>
 * <p><b>Module Deps      :</b> Organization</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "tax_override_requests")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaxOverrideRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(name = "document_type", nullable = false, length = 50)
    private String documentType;

    @Column(name = "document_id", nullable = false)
    private Long documentId;

    @Column(name = "original_tax_amount", nullable = false, precision = 20, scale = 2)
    private BigDecimal originalTaxAmount;

    @Column(name = "requested_tax_amount", nullable = false, precision = 20, scale = 2)
    private BigDecimal requestedTaxAmount;

    @Column(name = "reason", nullable = false, columnDefinition = "TEXT")
    private String reason;

    @Builder.Default
    @Column(name = "status", nullable = false, length = 30)
    private String status = "PENDING"; // PENDING, APPROVED, REJECTED

    @Column(name = "reviewed_by", length = 100)
    private String reviewedBy;

    @Column(name = "reviewed_at")
    private LocalDateTime reviewedAt;
}