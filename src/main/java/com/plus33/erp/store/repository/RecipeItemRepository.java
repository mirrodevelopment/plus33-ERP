package com.plus33.erp.store.repository;

import com.plus33.erp.store.entity.RecipeItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RecipeItemRepository extends JpaRepository<RecipeItem, Long> {
    List<RecipeItem> findByRecipeId(Long recipeId);
}
