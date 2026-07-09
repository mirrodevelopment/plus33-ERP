/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Store Module
 * Package           : com.plus33.erp.store.entity
 * File              : RecipeItem.java
 * Purpose           : JPA Entity representing a persistent database record in Store Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: RecipeItemController
 * Related Service   : RecipeItemService, RecipeItemServiceImpl
 * Related Repository: RecipeItemRepository
 * Related Entity    : RecipeItem
 * Related DTO       : N/A
 * Related Mapper    : RecipeItemMapper
 * Related DB Table  : recipe_items
 * Related REST APIs : N/A
 * Depends On        : Inventory Module
 * Used By           : RecipeItemRepository, RecipeItemMapper
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Entity mapped to 'recipe_items'. Defines persistent domain object for Store Module with validation, relationship mappings, and lifecycle callbacks.
 ******************************************************************************/
package com.plus33.erp.store.entity;

import com.plus33.erp.inventory.entity.Product;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "recipe_items", uniqueConstraints = {
    @UniqueConstraint(name = "uk_recipe_ingredient", columnNames = {"recipe_id", "ingredient_product_id"})
})
/**
 * <b>PLUS33 Coffee ERP -- Store Module</b>
 *
 * <p><b>Class  :</b> {@code RecipeItem}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.store.entity}</p>
 * <p><b>Layer  :</b> JPA Entity: persistent domain object mapped to PostgreSQL table 'recipe_items'.</p>
 *
 * <p><b>Database Table   :</b> {@code recipe_items}</p>
 * <p><b>Module Deps      :</b> Inventory</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@NoArgsConstructor
@AllArgsConstructor
public class RecipeItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recipe_id", nullable = false)
    private Recipe recipe;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ingredient_product_id", nullable = false)
    private Product ingredient;

    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal quantity;
}