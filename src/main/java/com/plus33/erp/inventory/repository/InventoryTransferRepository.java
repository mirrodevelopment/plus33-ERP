package com.plus33.erp.inventory.repository;

import com.plus33.erp.inventory.entity.InventoryTransfer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface InventoryTransferRepository extends JpaRepository<InventoryTransfer, Long>, JpaSpecificationExecutor<InventoryTransfer> {

    Optional<InventoryTransfer> findByClientReferenceId(UUID clientReferenceId);

    @Query(value = "SELECT nextval('inventory_transfer_seq')", nativeQuery = true)
    Long getNextSequenceValue();
}
