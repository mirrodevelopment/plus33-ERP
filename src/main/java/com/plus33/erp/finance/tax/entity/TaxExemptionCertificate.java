/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.tax.entity
 * File              : TaxExemptionCertificate.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TaxExemptionCertificateController
 * Related Service   : TaxExemptionCertificateService, TaxExemptionCertificateServiceImpl
 * Related Repository: TaxExemptionCertificateRepository
 * Related Entity    : TaxExemptionCertificate
 * Related DTO       : N/A
 * Related Mapper    : TaxExemptionCertificateMapper
 * Related DB Table  : tax_exemption_certificates
 * Related REST APIs : N/A
 * Depends On        : Organization Module
 * Used By           : TaxExemptionCertificateRepository, TaxExemptionCertificateMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'tax_exemption_certificates'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.tax.entity;

import com.plus33.erp.organization.entity.Company;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code TaxExemptionCertificate}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.tax.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'tax_exemption_certificates'.</p>
 *
 * <p><b>Database Table   :</b> {@code tax_exemption_certificates}</p>
 * <p><b>Module Deps      :</b> Organization</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "tax_exemption_certificates")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaxExemptionCertificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(name = "customer_id", nullable = false)
    private Long customerId;

    @Column(name = "certificate_number", nullable = false, unique = true, length = 100)
    private String certificateNumber;

    @Column(name = "exemption_reason", nullable = false, length = 100)
    private String exemptionReason;

    @Column(name = "effective_from", nullable = false)
    private LocalDate effectiveFrom;

    @Column(name = "effective_to", nullable = false)
    private LocalDate effectiveTo;

    @Builder.Default
    @Column(name = "active", nullable = false)
    private boolean active = true;
}