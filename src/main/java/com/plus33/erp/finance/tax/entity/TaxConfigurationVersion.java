/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.tax.entity
 * File              : TaxConfigurationVersion.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TaxConfigurationVersionController
 * Related Service   : TaxConfigurationVersionService, TaxConfigurationVersionServiceImpl
 * Related Repository: TaxConfigurationVersionRepository
 * Related Entity    : TaxConfigurationVersion
 * Related DTO       : N/A
 * Related Mapper    : TaxConfigurationVersionMapper
 * Related DB Table  : tax_configuration_versions
 * Related REST APIs : N/A
 * Depends On        : Organization Module
 * Used By           : TaxConfigurationVersionRepository, TaxConfigurationVersionMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'tax_configuration_versions'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.tax.entity;

import com.plus33.erp.organization.entity.Company;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "tax_configuration_versions", uniqueConstraints = {
    @UniqueConstraint(name = "uk_company_version", columnNames = {"company_id", "version_number"})
})
/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code TaxConfigurationVersion}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.tax.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'tax_configuration_versions'.</p>
 *
 * <p><b>Database Table   :</b> {@code tax_configuration_versions}</p>
 * <p><b>Module Deps      :</b> Organization</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaxConfigurationVersion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(name = "version_number", nullable = false)
    private Integer versionNumber;

    @Column(name = "effective_from", nullable = false)
    private LocalDateTime effectiveFrom;

    @Column(name = "effective_to")
    private LocalDateTime effectiveTo;

    @Column(name = "published_by", nullable = false, length = 100)
    private String publishedBy;

    @Column(name = "description", length = 255)
    private String description;

    @Builder.Default
    @Column(name = "active", nullable = false)
    private boolean active = true;
}