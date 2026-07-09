/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Store Module
 * Package           : com.plus33.erp.store.repository
 * File              : RecipeItemRepository.java
 * Purpose           : JPA Repository providing database CRUD for Store Module entities
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
 * Depends On        : None
 * Used By           : RecipeItemService, RecipeItemServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Store Module against the 'recipe_items' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.store.repository;

import com.plus33.erp.store.entity.RecipeItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Store Module</b>
 *
 * <p><b>Class  :</b> {@code RecipeItemRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.store.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'recipe_items' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code recipe_items}</p>
 * <p><b>Module Deps      :</b> Store</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface RecipeItemRepository extends JpaRepository<RecipeItem, Long> {
    List<RecipeItem> findByRecipeId(Long recipeId);
}
