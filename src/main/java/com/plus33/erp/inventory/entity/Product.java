/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.entity
 * File              : Product.java
 * Purpose           : JPA Entity representing a persistent database record in Inventory Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProductController
 * Related Service   : ProductService, ProductServiceImpl
 * Related Repository: ProductRepository
 * Related Entity    : Product
 * Related DTO       : N/A
 * Related Mapper    : ProductMapper
 * Related DB Table  : products
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ProductRepository, ProductMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'products'. Defines persistent domain object for Inventory Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.inventory.entity;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * <b>PLUS33 Coffee ERP -- Inventory Module</b>
 *
 * <p><b>Class  :</b> {@code Product}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.inventory.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'products'.</p>
 *
 * <p><b>Database Table   :</b> {@code products}</p>
 * <p><b>Module Deps      :</b> None</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Getter
@Setter
@Entity
@Table(name = "products")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "sku", nullable = false, unique = true, length = 100)
    private String code;

    @Column(nullable = false, length = 150)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private ProductCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_id", nullable = false)
    private UnitOfMeasure unit;

    @Column(name = "product_type", nullable = false, length = 50)
    private String productType;

    @Column(name = "reorder_level", precision = 12, scale = 2)
    @Builder.Default
    private BigDecimal reorderLevel = BigDecimal.ZERO;

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