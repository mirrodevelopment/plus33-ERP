/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.entity
 * File              : Account.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: AccountController
 * Related Service   : AccountService, AccountServiceImpl
 * Related Repository: AccountRepository
 * Related Entity    : Account
 * Related DTO       : N/A
 * Related Mapper    : AccountMapper
 * Related DB Table  : chart_of_accounts
 * Related REST APIs : N/A
 * Depends On        : Organization Module
 * Used By           : AccountRepository, AccountMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'chart_of_accounts'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.entity;

import com.plus33.erp.organization.entity.Company;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "chart_of_accounts", uniqueConstraints = {
    @UniqueConstraint(name = "uk_coa_company_code", columnNames = {"company_id", "account_code"})
})
/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code Account}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'chart_of_accounts'.</p>
 *
 * <p><b>Database Table   :</b> {@code chart_of_accounts}</p>
 * <p><b>Module Deps      :</b> Organization</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Account {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @Column(name = "account_code", nullable = false, length = 50)
    private String accountCode;

    @Column(name = "account_name", nullable = false, length = 150)
    private String accountName;

    @Column(name = "account_type", nullable = false, length = 30)
    private String accountType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_account_id")
    private Account parentAccount;

    @Builder.Default
    @Column(nullable = false)
    private Boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Handles the create event or exception in the business workflow.
     *
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    /**
     * Handles the update event or exception in the business workflow.
     *
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}