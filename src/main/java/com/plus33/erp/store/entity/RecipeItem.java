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
