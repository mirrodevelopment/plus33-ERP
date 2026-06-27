package com.plus33.erp.finance.tax.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "tax_registrations")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaxRegistration {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "entity_type", nullable = false, length = 20)
    private String entityType; // COMPANY, CUSTOMER, SUPPLIER

    @Column(name = "entity_id", nullable = false)
    private Long entityId;

    @Column(name = "tax_scheme", nullable = false, length = 50)
    private String taxScheme; // TRN, VAT, GST, TIN, PAN, ABN, EIN

    @Column(name = "registration_number", nullable = false, length = 100)
    private String registrationNumber;

    @Column(name = "tax_office", length = 150)
    private String taxOffice;

    @Builder.Default
    @Column(name = "filing_frequency", nullable = false, length = 30)
    private String filingFrequency = "MONTHLY"; // MONTHLY, QUARTERLY, ANNUALLY

    @Builder.Default
    @Column(name = "filing_currency", nullable = false, length = 3)
    private String filingCurrency = "AED";

    @Builder.Default
    @Column(name = "is_default", nullable = false)
    private boolean isDefault = false;

    @Builder.Default
    @Column(name = "status", nullable = false, length = 30)
    private String status = "ACTIVE"; // ACTIVE, SUSPENDED, CANCELLED

    @Column(name = "effective_from", nullable = false)
    private LocalDate effectiveFrom;

    @Column(name = "effective_to")
    private LocalDate effectiveTo;

    @Builder.Default
    @Column(name = "active", nullable = false)
    private boolean active = true;
}
