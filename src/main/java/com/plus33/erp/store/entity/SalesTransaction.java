/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Store Module
 * Package           : com.plus33.erp.store.entity
 * File              : SalesTransaction.java
 * Purpose           : JPA Entity representing a persistent database record in Store Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: SalesTransactionController
 * Related Service   : SalesTransactionService, SalesTransactionServiceImpl
 * Related Repository: SalesTransactionRepository
 * Related Entity    : SalesTransaction
 * Related DTO       : N/A
 * Related Mapper    : SalesTransactionMapper
 * Related DB Table  : sales_transactions
 * Related REST APIs : N/A
 * Depends On        : Organization Module, Security Module
 * Used By           : SalesTransactionRepository, SalesTransactionMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'sales_transactions'. Defines persistent domain object for Store Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.store.entity;

import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.entity.Store;
import com.plus33.erp.security.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Store Module</b>
 *
 * <p><b>Class  :</b> {@code SalesTransaction}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.store.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'sales_transactions'.</p>
 *
 * <p><b>Database Table   :</b> {@code sales_transactions}</p>
 * <p><b>Module Deps      :</b> Organization, Security</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "sales_transactions")
@NoArgsConstructor
@AllArgsConstructor
public class SalesTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "transaction_number", nullable = false, unique = true, length = 50)
    private String transactionNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id", nullable = false)
    private Store store;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cashier_user_id", nullable = false)
    private User cashier;

    @Column(name = "total_amount", nullable = false, precision = 12, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "payment_method", nullable = false, length = 30)
    private String paymentMethod;

    @Column(name = "payment_status", nullable = false, length = 30)
    private String paymentStatus = "PAID";

    @Column(nullable = false, length = 30)
    private String status = "COMPLETED";

    @Column(name = "transaction_time", nullable = false, updatable = false)
    private LocalDateTime transactionTime;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    /**
     * Handles the create event or exception in the business workflow.
     *
     */
    @PrePersist
    protected void onCreate() {
        transactionTime = LocalDateTime.now();
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