package com.plus33.erp.inventory.repository;

import com.plus33.erp.inventory.entity.InventoryStock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface InventoryStockRepository extends JpaRepository<InventoryStock, Long> {

    List<InventoryStock> findByProductId(Long productId);

    List<InventoryStock> findByWarehouseId(Long warehouseId);

    List<InventoryStock> findByStoreId(Long storeId);

    Optional<InventoryStock> findByProductIdAndWarehouseId(Long productId, Long warehouseId);

    Optional<InventoryStock> findByProductIdAndStoreId(Long productId, Long storeId);

    @org.springframework.data.jpa.repository.Lock(jakarta.persistence.LockModeType.PESSIMISTIC_WRITE)
    @org.springframework.data.jpa.repository.Query("SELECT i FROM InventoryStock i WHERE i.product.id = :productId AND i.warehouse.id = :warehouseId")
    Optional<InventoryStock> findWithPessimisticWriteByProductIdAndWarehouseId(Long productId, Long warehouseId);

    @org.springframework.data.jpa.repository.Lock(jakarta.persistence.LockModeType.PESSIMISTIC_WRITE)
    @org.springframework.data.jpa.repository.Query("SELECT i FROM InventoryStock i WHERE i.product.id = :productId AND i.store.id = :storeId")
    Optional<InventoryStock> findWithPessimisticWriteByProductIdAndStoreId(Long productId, Long storeId);
}
