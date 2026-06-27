package com.plus33.erp.finance.tax.entity;

import com.plus33.erp.organization.entity.Company;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

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
