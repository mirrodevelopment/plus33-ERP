/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.repository
 * File              : ReplenishmentSuggestionRepository.java
 * Purpose           : JPA Repository providing database CRUD for Inventory Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ReplenishmentSuggestionController
 * Related Service   : ReplenishmentSuggestionService, ReplenishmentSuggestionServiceImpl
 * Related Repository: ReplenishmentSuggestionRepository
 * Related Entity    : ReplenishmentSuggestion
 * Related DTO       : N/A
 * Related Mapper    : ReplenishmentSuggestionMapper
 * Related DB Table  : replenishment_suggestions
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ReplenishmentSuggestionService, ReplenishmentSuggestionServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Inventory Module against the 'replenishment_suggestions' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.inventory.repository;

import com.plus33.erp.inventory.entity.ReplenishmentSuggestion;
import com.plus33.erp.inventory.entity.ReplenishmentSuggestionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * <b>PLUS33 Coffee ERP -- Inventory Module</b>
 *
 * <p><b>Class  :</b> {@code ReplenishmentSuggestionRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.inventory.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'replenishment_suggestions' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code replenishment_suggestions}</p>
 * <p><b>Module Deps      :</b> Inventory</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface ReplenishmentSuggestionRepository extends JpaRepository<ReplenishmentSuggestion, Long>, JpaSpecificationExecutor<ReplenishmentSuggestion> {

    Optional<ReplenishmentSuggestion> findByClientReferenceId(UUID clientReferenceId);

    @Query("SELECT rs FROM ReplenishmentSuggestion rs WHERE rs.rule.id = :ruleId AND rs.status IN :statuses")
    List<ReplenishmentSuggestion> findByRuleIdAndStatusIn(
            @Param("ruleId") Long ruleId,
            @Param("statuses") Collection<ReplenishmentSuggestionStatus> statuses
    );

    @Query("SELECT rs FROM ReplenishmentSuggestion rs WHERE rs.rule.id = :ruleId AND rs.status = 'OPEN'")
    Optional<ReplenishmentSuggestion> findOpenSuggestionByRuleId(@Param("ruleId") Long ruleId);
}
