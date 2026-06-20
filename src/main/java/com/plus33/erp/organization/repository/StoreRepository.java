package com.plus33.erp.organization.repository;

import com.plus33.erp.organization.entity.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.Optional;

public interface StoreRepository extends JpaRepository<Store, Long>, JpaSpecificationExecutor<Store> {
    Optional<Store> findByCode(String code);
    boolean existsByCode(String code);
    boolean existsByRegionCompanyIdAndCode(Long companyId, String code);
    boolean existsByRegionIdAndActiveTrue(Long regionId);
    boolean existsByWarehouseIdAndActiveTrue(Long warehouseId);
}
