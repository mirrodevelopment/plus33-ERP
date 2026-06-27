package com.plus33.erp.finance.tax.entity;

import com.plus33.erp.organization.entity.Company;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

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
