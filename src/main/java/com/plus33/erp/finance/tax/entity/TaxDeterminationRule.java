package com.plus33.erp.finance.tax.entity;

import com.plus33.erp.organization.entity.Company;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Entity
@Table(name = "tax_determination_rules")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaxDeterminationRule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(name = "rule_name", nullable = false, length = 100)
    private String ruleName;

    @Builder.Default
    @Column(name = "priority", nullable = false)
    private int priority = 100;

    @Column(name = "customer_tax_profile", length = 50)
    private String customerTaxProfile;

    @Column(name = "supplier_tax_profile", length = 50)
    private String supplierTaxProfile;

    @Column(name = "product_tax_category", length = 50)
    private String productTaxCategory;

    @Column(name = "origin_country", length = 3)
    private String originCountry;

    @Column(name = "origin_state", length = 50)
    private String originState;

    @Column(name = "dest_country", length = 3)
    private String destCountry;

    @Column(name = "dest_state", length = 50)
    private String destState;

    @Column(name = "incoterms", length = 3)
    private String incoterms;

    @Column(name = "document_type", nullable = false, length = 50)
    private String documentType; // Maps to tax document taxonomy

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tax_group_id", nullable = false)
    private TaxGroup taxGroup;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "config_version_id")
    private TaxConfigurationVersion configVersion;

    @Column(name = "effective_from", nullable = false)
    private LocalDate effectiveFrom;

    @Column(name = "effective_to")
    private LocalDate effectiveTo;

    @Builder.Default
    @Column(name = "active", nullable = false)
    private boolean active = true;
}
