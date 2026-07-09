/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.entity
 * File              : PaymentAllocation.java
 * Purpose           : JPA Entity representing a persistent database record in Finance Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PaymentAllocationController
 * Related Service   : PaymentAllocationService, PaymentAllocationServiceImpl
 * Related Repository: PaymentAllocationRepository
 * Related Entity    : PaymentAllocation
 * Related DTO       : N/A
 * Related Mapper    : PaymentAllocationMapper
 * Related DB Table  : payment_allocations
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PaymentAllocationRepository, PaymentAllocationMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'payment_allocations'. Defines persistent domain object for Finance Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.finance.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code PaymentAllocation}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'payment_allocations'.</p>
 *
 * <p><b>Database Table   :</b> {@code payment_allocations}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "payment_allocations")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentAllocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false)
    private Payment payment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_invoice_id")
    private SupplierInvoice supplierInvoice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_invoice_id")
    private com.plus33.erp.sales.entity.CustomerInvoice customerInvoice;

    @Column(name = "allocated_amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal allocatedAmount;
}