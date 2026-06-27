package com.plus33.erp.finance.treasury.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@Table(name = "bank_virtual_accounts")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BankVirtualAccount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_account_id", nullable = false)
    private BankAccount parentAccount;

    @Column(name = "virtual_account_number", nullable = false, unique = true, length = 50)
    private String virtualAccountNumber;

    @Column(name = "owner_reference_type", nullable = false, length = 30)
    private String ownerReferenceType; // CUSTOMER, SUPPLIER

    @Column(name = "owner_reference_id", nullable = false)
    private Long ownerReferenceId;

    @Column(nullable = false)
    @Builder.Default
    private Boolean active = true;
}
