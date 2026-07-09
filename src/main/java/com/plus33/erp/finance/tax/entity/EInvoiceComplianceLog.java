/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.tax.entity
 * File              : EInvoiceComplianceLog.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: EInvoiceComplianceLogController
 * Related Service   : EInvoiceComplianceLogService, EInvoiceComplianceLogServiceImpl
 * Related Repository: EInvoiceComplianceLogRepository
 * Related Entity    : EInvoiceComplianceLog
 * Related DTO       : N/A
 * Related Mapper    : EInvoiceComplianceLogMapper
 * Related DB Table  : einvoice_compliance_logs
 * Related REST APIs : N/A
 * Depends On        : Organization Module
 * Used By           : EInvoiceComplianceLogRepository, EInvoiceComplianceLogMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'einvoice_compliance_logs'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.tax.entity;

import com.plus33.erp.organization.entity.Company;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code EInvoiceComplianceLog}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.tax.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'einvoice_compliance_logs'.</p>
 *
 * <p><b>Database Table   :</b> {@code einvoice_compliance_logs}</p>
 * <p><b>Module Deps      :</b> Organization</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "einvoice_compliance_logs")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EInvoiceComplianceLog {

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

    @Column(name = "provider_type", nullable = false, length = 50)
    private String providerType; // ZATCA, IRP, FTA, PEPPOL

    @Column(name = "xml_payload", nullable = false, columnDefinition = "TEXT")
    private String xmlPayload;

    @Column(name = "signature_hash", nullable = false, length = 256)
    private String signatureHash;

    @Column(name = "status", nullable = false, length = 30)
    private String status; // SUBMITTED, ACCEPTED, REJECTED, WARNINGS

    @Column(name = "error_details", columnDefinition = "TEXT")
    private String errorDetails;

    @Column(name = "government_uuid", length = 100)
    private String governmentUuid;

    @Builder.Default
    @Column(name = "occurred_at", nullable = false)
    private LocalDateTime occurredAt = LocalDateTime.now();
}