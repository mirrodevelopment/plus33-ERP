/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Store Module
 * Package           : com.plus33.erp.store.entity
 * File              : Recipe.java
 * Purpose           : JPA Entity representing a persistent database record in Store Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: RecipeController
 * Related Service   : RecipeService, RecipeServiceImpl
 * Related Repository: RecipeRepository
 * Related Entity    : Recipe
 * Related DTO       : N/A
 * Related Mapper    : RecipeMapper
 * Related DB Table  : recipes
 * Related REST APIs : N/A
 * Depends On        : Organization Module, Inventory Module
 * Used By           : RecipeRepository, RecipeMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'recipes'. Defines persistent domain object for Store Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.store.entity;

import com.plus33.erp.organization.entity.Company;
import com.plus33.erp.inventory.entity.Product;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "recipes", uniqueConstraints = {
    @UniqueConstraint(name = "uk_recipe_product_version", columnNames = {"product_id", "version"})
})
/**
 * <b>PLUS33 Coffee ERP -- Store Module</b>
 *
 * <p><b>Class  :</b> {@code Recipe}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.store.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'recipes'.</p>
 *
 * <p><b>Database Table   :</b> {@code recipes}</p>
 * <p><b>Module Deps      :</b> Organization, Inventory</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@NoArgsConstructor
@AllArgsConstructor
public class Recipe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer version = 1;

    @Column(nullable = false)
    private Boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @OneToMany(mappedBy = "recipe", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<RecipeItem> items = new ArrayList<>();

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