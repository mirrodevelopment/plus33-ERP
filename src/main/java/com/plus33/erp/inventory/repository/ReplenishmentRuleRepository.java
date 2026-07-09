/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Inventory Module
 * Package           : com.plus33.erp.inventory.repository
 * File              : ReplenishmentRuleRepository.java
 * Purpose           : JPA Repository providing database CRUD for Inventory Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ReplenishmentRuleController
 * Related Service   : ReplenishmentRuleService, ReplenishmentRuleServiceImpl
 * Related Repository: ReplenishmentRuleRepository
 * Related Entity    : ReplenishmentRule
 * Related DTO       : N/A
 * Related Mapper    : ReplenishmentRuleMapper
 * Related DB Table  : replenishment_rules
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ReplenishmentRuleService, ReplenishmentRuleServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Inventory Module against the 'replenishment_rules' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.inventory.repository;

import com.plus33.erp.inventory.entity.ReplenishmentRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * <b>PLUS33 Coffee ERP -- Inventory Module</b>
 *
 * <p><b>Class  :</b> {@code ReplenishmentRuleRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.inventory.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'replenishment_rules' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code replenishment_rules}</p>
 * <p><b>Module Deps      :</b> Inventory</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface ReplenishmentRuleRepository extends JpaRepository<ReplenishmentRule, Long>, JpaSpecificationExecutor<ReplenishmentRule> {

    Optional<ReplenishmentRule> findByClientReferenceId(UUID clientReferenceId);

    Optional<ReplenishmentRule> findByProductIdAndWarehouseId(Long productId, Long warehouseId);

    Optional<ReplenishmentRule> findByProductIdAndStoreId(Long productId, Long storeId);

    List<ReplenishmentRule> findAllByActiveTrue();

    List<ReplenishmentRule> findAllByCompanyIdAndActiveTrue(Long companyId);
}
