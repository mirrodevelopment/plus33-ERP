/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.entity
 * File              : CustomerReturn.java
 * Purpose           : JPA Entity representing a persistent database record in Sales Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CustomerReturnController
 * Related Service   : CustomerReturnService, CustomerReturnServiceImpl
 * Related Repository: CustomerReturnRepository
 * Related Entity    : CustomerReturn
 * Related DTO       : N/A
 * Related Mapper    : CustomerReturnMapper
 * Related DB Table  : customer_returns
 * Related REST APIs : N/A
 * Depends On        : Organization Module, Security Module
 * Used By           : CustomerReturnRepository, CustomerReturnMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'customer_returns'. Defines persistent domain object for Sales Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.sales.entity;

import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.entity.Store;
import com.plus33.erp.organization.entity.Warehouse;
import com.plus33.erp.security.entity.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
@Setter
@Entity
@Table(name = "customer_returns", uniqueConstraints = {
    @UniqueConstraint(name = "uk_customer_returns_number", columnNames = {"company_id", "return_number"}),
    @UniqueConstraint(name = "uk_customer_returns_client_ref", columnNames = {"company_id", "client_reference_id"})
})
/**
 * <b>PLUS33 Coffee ERP -- Sales Module</b>
 *
 * <p><b>Class  :</b> {@code CustomerReturn}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.sales.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'customer_returns'.</p>
 *
 * <p><b>Database Table   :</b> {@code customer_returns}</p>
 * <p><b>Module Deps      :</b> Organization, Security</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerReturn {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sales_order_id")
    private SalesOrder salesOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_invoice_id")
    private CustomerInvoice customerInvoice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Column(name = "return_number", nullable = false, length = 50)
    private String returnNumber;

    @Column(name = "client_reference_id", nullable = false)
    private UUID clientReferenceId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private CustomerReturnStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ReturnReason reason;

    @Column(columnDefinition = "TEXT")
    private String remarks;

    // Audit fields
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "approved_by")
    private User approvedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "received_by")
    private User receivedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "inspected_by")
    private User inspectedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "closed_by")
    private User closedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cancelled_by")
    private User cancelledBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "approved_at")
    private LocalDateTime approvedAt;

    @Column(name = "received_at")
    private LocalDateTime receivedAt;

    @Column(name = "inspected_at")
    private LocalDateTime inspectedAt;

    @Column(name = "closed_at")
    private LocalDateTime closedAt;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @Column(name = "cancellation_reason", columnDefinition = "TEXT")
    private String cancellationReason;

    @Version
    private Long version;

    @Builder.Default
    @OneToMany(mappedBy = "customerReturn", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CustomerReturnItem> items = new ArrayList<>();

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

    /**
     * Creates a new item and persists it to the database.
     *
     * @param item the item input value
     * @throws BusinessException if a business rule is violated
     */
    public void addItem(CustomerReturnItem item) {
        items.add(item);
        item.setCustomerReturn(this);
    }

    /**
     * Permanently deletes the item from the database.
     *
     * @param item the item input value
     */
    public void removeItem(CustomerReturnItem item) {
        items.remove(item);
        item.setCustomerReturn(null);
    }
}