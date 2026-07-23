package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.LeavePolicyGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LeavePolicyGroupRepository extends JpaRepository<LeavePolicyGroup, Long> {

    /** Find a policy group by its unique code (case-insensitive). */
    Optional<LeavePolicyGroup> findByCode(String code);

    /**
     * Resolve the effective policy group for a company by traversing:
     * Store → Region → Company → System Default.
     * This query uses the entity_policy_group_mappings table.
     */
    @Query(value = """
            SELECT lpg.*
            FROM leave_policy_groups lpg
            JOIN entity_policy_group_mappings m ON m.policy_group_id = lpg.id
            WHERE m.entity_type = :entityType
              AND m.entity_id   = :entityId
              AND m.effective_from <= CURRENT_DATE
              AND (m.effective_to IS NULL OR m.effective_to >= CURRENT_DATE)
            ORDER BY m.effective_from DESC
            LIMIT 1
            """,
           nativeQuery = true)
    Optional<LeavePolicyGroup> findEffectiveForEntity(
            @Param("entityType") String entityType,
            @Param("entityId") Long entityId);
}
