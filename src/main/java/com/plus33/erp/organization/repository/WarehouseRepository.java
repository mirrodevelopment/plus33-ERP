package com.plus33.erp.organization.repository;

import com.plus33.erp.organization.entity.Warehouse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.Optional;

public interface WarehouseRepository extends JpaRepository<Warehouse, Long>, JpaSpecificationExecutor<Warehouse> {
    Optional<Warehouse> findByCode(String code);
    boolean existsByCode(String code);
    boolean existsByRegionIdAndCode(Long regionId, String code);
    boolean existsByRegionIdAndActiveTrue(Long regionId);
}
