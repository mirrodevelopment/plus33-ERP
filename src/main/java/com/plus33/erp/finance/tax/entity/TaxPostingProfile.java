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
