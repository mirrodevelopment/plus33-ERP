package com.plus33.erp.inventory.repository;

import com.plus33.erp.inventory.entity.ReplenishmentRule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReplenishmentRuleRepository extends JpaRepository<ReplenishmentRule, Long>, JpaSpecificationExecutor<ReplenishmentRule> {

    Optional<ReplenishmentRule> findByClientReferenceId(UUID clientReferenceId);

    Optional<ReplenishmentRule> findByProductIdAndWarehouseId(Long productId, Long warehouseId);

    Optional<ReplenishmentRule> findByProductIdAndStoreId(Long productId, Long storeId);

    List<ReplenishmentRule> findAllByActiveTrue();

    List<ReplenishmentRule> findAllByCompanyIdAndActiveTrue(Long companyId);
}
