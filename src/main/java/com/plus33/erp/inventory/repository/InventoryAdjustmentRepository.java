package com.plus33.erp.inventory.repository;

import com.plus33.erp.inventory.entity.InventoryAdjustment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface InventoryAdjustmentRepository extends JpaRepository<InventoryAdjustment, Long>, JpaSpecificationExecutor<InventoryAdjustment> {

    Optional<InventoryAdjustment> findByClientReferenceId(UUID clientReferenceId);

    @Query(value = "SELECT nextval('inventory_adjustment_seq')", nativeQuery = true)
    Long getNextSequenceValue();
}
