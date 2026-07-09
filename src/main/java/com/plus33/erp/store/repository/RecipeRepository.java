/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Store Module
 * Package           : com.plus33.erp.store.repository
 * File              : RecipeRepository.java
 * Purpose           : JPA Repository providing database CRUD for Store Module entities
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
 * Depends On        : None
 * Used By           : RecipeService, RecipeServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Store Module against the 'recipes' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.store.repository;

import com.plus33.erp.store.entity.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Store Module</b>
 *
 * <p><b>Class  :</b> {@code RecipeRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.store.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'recipes' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code recipes}</p>
 * <p><b>Module Deps      :</b> Store</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface RecipeRepository extends JpaRepository<Recipe, Long> {
    Optional<Recipe> findByProductIdAndVersion(Long productId, Integer version);
    Optional<Recipe> findByProductIdAndActiveTrue(Long productId);
}
