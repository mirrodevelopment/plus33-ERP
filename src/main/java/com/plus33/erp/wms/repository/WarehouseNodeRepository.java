package com.plus33.erp.wms.repository;

import com.plus33.erp.wms.entity.WarehouseNode;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface WarehouseNodeRepository extends JpaRepository<WarehouseNode, Long> {
    List<WarehouseNode> findByWarehouseId(Long warehouseId);
    Optional<WarehouseNode> findByLocationId(Long locationId);
}
