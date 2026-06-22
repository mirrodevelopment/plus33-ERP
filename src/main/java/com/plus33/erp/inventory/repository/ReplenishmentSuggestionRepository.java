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
