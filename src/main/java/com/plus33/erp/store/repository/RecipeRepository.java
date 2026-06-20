package com.plus33.erp.store.repository;

import com.plus33.erp.store.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    Optional<Recipe> findByProductIdAndVersion(Long productId, Integer version);
    Optional<Recipe> findByProductIdAndActiveTrue(Long productId);
}
