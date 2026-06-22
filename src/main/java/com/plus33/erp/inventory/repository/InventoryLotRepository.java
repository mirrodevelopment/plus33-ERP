package com.plus33.erp.inventory.repository;

import com.plus33.erp.inventory.entity.InventoryLot;
import com.plus33.erp.inventory.entity.InventoryLotStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface InventoryLotRepository extends JpaRepository<InventoryLot, Long>, JpaSpecificationExecutor<InventoryLot> {

    Optional<InventoryLot> findByCompanyIdAndProductIdAndLotNumber(Long companyId, Long productId, String lotNumber);

    List<InventoryLot> findByProductIdAndStatusOrderByExpiryDateAsc(Long productId, InventoryLotStatus status);

    @Query("SELECT il FROM InventoryLot il WHERE il.status = :status AND il.expiryDate < :date")
    List<InventoryLot> findActiveExpiredLots(@Param("status") InventoryLotStatus status, @Param("date") LocalDate date);
}
