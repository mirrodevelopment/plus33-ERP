package com.plus33.erp.wms.repository;

import com.plus33.erp.wms.entity.WarehouseEdge;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WarehouseEdgeRepository extends JpaRepository<WarehouseEdge, Long> {
    List<WarehouseEdge> findByWarehouseId(Long warehouseId);
}
