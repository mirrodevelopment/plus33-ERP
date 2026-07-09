/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.tax.entity
 * File              : TaxPostingProfile.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: TaxPostingProfileController
 * Related Service   : TaxPostingProfileService, TaxPostingProfileServiceImpl
 * Related Repository: TaxPostingProfileRepository
 * Related Entity    : TaxPostingProfile
 * Related DTO       : N/A
 * Related Mapper    : TaxPostingProfileMapper
 * Related DB Table  : tax_posting_profiles
 * Related REST APIs : N/A
 * Depends On        : Organization Module
 * Used By           : TaxPostingProfileRepository, TaxPostingProfileMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'tax_posting_profiles'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.tax.entity;

import com.plus33.erp.finance.entity.Account;
import com.plus33.erp.organization.entity.Company;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "tax_posting_profiles", uniqueConstraints = {
    @UniqueConstraint(name = "uk_tpp_company_category", columnNames = {"company_id", "category_id"})
})
/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code TaxPostingProfile}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.tax.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'tax_posting_profiles'.</p>
 *
 * <p><b>Database Table   :</b> {@code tax_posting_profiles}</p>
 * <p><b>Module Deps      :</b> Finance, Organization</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TaxPostingProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private TaxCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "input_tax_account_id")
    private Account inputTaxAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "output_tax_account_id")
    private Account outputTaxAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reverse_charge_account_id")
    private Account reverseChargeAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recoverable_account_id")
    private Account recoverableAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "non_recoverable_account_id")
    private Account nonRecoverableAccount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "suspense_account_id")
    private Account suspenseAccount;

    @Builder.Default
    @Column(name = "active", nullable = false)
    private boolean active = true;
}