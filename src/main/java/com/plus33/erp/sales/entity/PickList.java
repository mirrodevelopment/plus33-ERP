/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.entity
 * File              : PickList.java
 * Purpose           : JPA Entity representing a persistent database record in Sales Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PickListController
 * Related Service   : PickListService, PickListServiceImpl
 * Related Repository: PickListRepository
 * Related Entity    : PickList
 * Related DTO       : N/A
 * Related Mapper    : PickListMapper
 * Related DB Table  : pick_lists
 * Related REST APIs : N/A
 * Depends On        : Organization Module, Security Module
 * Used By           : PickListRepository, PickListMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'pick_lists'. Defines persistent domain object for Sales Module with validation, relationship mappings, and lifecycle callbacks.
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
@Table(name = "pick_lists", uniqueConstraints = {
    @UniqueConstraint(name = "uk_pick_list_company_number", columnNames = {"company_id", "pick_number"}),
    @UniqueConstraint(name = "uk_pick_list_client_reference", columnNames = {"company_id", "client_reference_id"})
})
/**
 * <b>PLUS33 Coffee ERP -- Sales Module</b>
 *
 * <p><b>Class  :</b> {@code PickList}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.sales.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'pick_lists'.</p>
 *
 * <p><b>Database Table   :</b> {@code pick_lists}</p>
 * <p><b>Module Deps      :</b> Organization, Security</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PickList {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sales_order_id", nullable = false)
    private SalesOrder salesOrder;

    @Column(name = "pick_number", nullable = false, length = 50)
    private String pickNumber;

    @Column(name = "client_reference_id", nullable = false)
    private UUID clientReferenceId;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 30)
    private PickListStatus status = PickListStatus.DRAFT;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    // Audit trail fields
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private User createdBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "released_by")
    private User releasedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "picked_by")
    private User pickedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "packed_by")
    private User packedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipped_by")
    private User shippedBy;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cancelled_by")
    private User cancelledBy;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "released_at")
    private LocalDateTime releasedAt;

    @Column(name = "picked_at")
    private LocalDateTime pickedAt;

    @Column(name = "packed_at")
    private LocalDateTime packedAt;

    @Column(name = "shipped_at")
    private LocalDateTime shippedAt;

    @Column(name = "cancelled_at")
    private LocalDateTime cancelledAt;

    @Column(name = "cancellation_reason", columnDefinition = "TEXT")
    private String cancellationReason;

    @Version
    private Long version;

    @Builder.Default
    @OneToMany(mappedBy = "pickList", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PickListItem> items = new ArrayList<>();

    /**
     * Handles the create event or exception in the business workflow.
     *
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    /**
     * Creates a new item and persists it to the database.
     *
     * @param item the item input value
     * @throws BusinessException if a business rule is violated
     */
    public void addItem(PickListItem item) {
        items.add(item);
        item.setPickList(this);
    }

    /**
     * Permanently deletes the item from the database.
     *
     * @param item the item input value
     */
    public void removeItem(PickListItem item) {
        items.remove(item);
        item.setPickList(null);
    }
}