/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Paymentrun Module
 * Package           : com.plus33.erp.paymentrun.entity
 * File              : PaymentRunSupplierResult.java
 * Purpose           : JPA Entity representing a persistent database record in Paymentrun Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PaymentRunSupplierResultController
 * Related Service   : PaymentRunSupplierResultService, PaymentRunSupplierResultServiceImpl
 * Related Repository: PaymentRunSupplierResultRepository
 * Related Entity    : PaymentRunSupplierResult
 * Related DTO       : N/A
 * Related Mapper    : PaymentRunSupplierResultMapper
 * Related DB Table  : payment_run_supplier_results
 * Related REST APIs : N/A
 * Depends On        : Finance Module, Procurement Module
 * Used By           : PaymentRunSupplierResultRepository, PaymentRunSupplierResultMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'payment_run_supplier_results'. Defines persistent domain object for Paymentrun Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.paymentrun.entity;

import com.plus33.erp.finance.entity.Payment;
import com.plus33.erp.procurement.entity.Supplier;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Paymentrun Module</b>
 *
 * <p><b>Class  :</b> {@code PaymentRunSupplierResult}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.paymentrun.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'payment_run_supplier_results'.</p>
 *
 * <p><b>Database Table   :</b> {@code payment_run_supplier_results}</p>
 * <p><b>Module Deps      :</b> Finance, Procurement</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "payment_run_supplier_results")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentRunSupplierResult {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_run_id", nullable = false)
    private PaymentRun paymentRun;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id", nullable = false)
    private Supplier supplier;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id")
    private Payment payment;

    @Column(nullable = false, length = 30)
    private String status; // SUCCESS, FAILED

    @Column(nullable = false, precision = 14, scale = 2)
    private BigDecimal amount;

    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    @Column(name = "started_at", nullable = false)
    private LocalDateTime startedAt;

    @Column(name = "completed_at", nullable = false)
    private LocalDateTime completedAt;
}