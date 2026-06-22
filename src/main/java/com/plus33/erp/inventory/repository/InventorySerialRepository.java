package com.plus33.erp.inventory.repository;

import com.plus33.erp.inventory.entity.InventorySerial;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

public interface InventorySerialRepository extends JpaRepository<InventorySerial, Long>, JpaSpecificationExecutor<InventorySerial> {

    Optional<InventorySerial> findByCompanyIdAndProductIdAndSerialNumber(Long companyId, Long productId, String serialNumber);

    Optional<InventorySerial> findBySerialNumber(String serialNumber);

    List<InventorySerial> findByLotId(Long lotId);
}
