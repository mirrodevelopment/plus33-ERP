package com.plus33.erp.wms.repository;

import com.plus33.erp.wms.entity.WarehouseSagaState;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface WarehouseSagaStateRepository extends JpaRepository<WarehouseSagaState, Long> {
    Optional<WarehouseSagaState> findByCorrelationId(String correlationId);
}
