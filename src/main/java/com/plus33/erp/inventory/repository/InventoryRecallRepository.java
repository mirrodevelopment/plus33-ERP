package com.plus33.erp.inventory.repository;

import com.plus33.erp.inventory.entity.InventoryRecall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface InventoryRecallRepository extends JpaRepository<InventoryRecall, Long>, JpaSpecificationExecutor<InventoryRecall> {

    Optional<InventoryRecall> findByRecallNumber(String recallNumber);

    boolean existsByLotIdAndStatus(Long lotId, com.plus33.erp.inventory.entity.InventoryRecallStatus status);

    boolean existsBySerialIdAndStatus(Long serialId, com.plus33.erp.inventory.entity.InventoryRecallStatus status);

    @Query(value = "SELECT nextval('inventory_recall_seq')", nativeQuery = true)
    Long getNextSequenceValue();
}
