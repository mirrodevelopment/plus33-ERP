/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.entity
 * File              : InventoryAllocation.java
 * Purpose           : JPA Entity representing a persistent database record in Sales Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InventoryAllocationController
 * Related Service   : InventoryAllocationService, InventoryAllocationServiceImpl
 * Related Repository: InventoryAllocationRepository
 * Related Entity    : InventoryAllocation
 * Related DTO       : N/A
 * Related Mapper    : InventoryAllocationMapper
 * Related DB Table  : inventory_allocations
 * Related REST APIs : N/A
 * Depends On        : Inventory Module, Organization Module
 * Used By           : InventoryAllocationRepository, InventoryAllocationMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'inventory_allocations'. Defines persistent domain object for Sales Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.sales.entity;

import com.plus33.erp.inventory.entity.Product;
import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.organization.entity.Store;
import com.plus33.erp.organization.entity.Warehouse;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Sales Module</b>
 *
 * <p><b>Class  :</b> {@code InventoryAllocation}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.sales.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'inventory_allocations'.</p>
 *
 * <p><b>Database Table   :</b> {@code inventory_allocations}</p>
 * <p><b>Module Deps      :</b> Inventory, Organization</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "inventory_allocations")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryAllocation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sales_order_id", nullable = false)
    private SalesOrder salesOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sales_order_item_id", nullable = false)
    private SalesOrderItem salesOrderItem;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pick_list_id", nullable = false)
    private PickList pickList;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "warehouse_id")
    private Warehouse warehouse;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "store_id")
    private Store store;

    @Column(name = "allocated_quantity", nullable = false, precision = 12, scale = 2)
    private BigDecimal allocatedQuantity;

    @Enumerated(EnumType.STRING)
    @Column(name = "allocation_status", nullable = false, length = 30)
    private AllocationStatus allocationStatus;

    @Column(name = "allocation_reference", nullable = false, length = 50)
    private String allocationReference;

    @Version
    private Long version;
}