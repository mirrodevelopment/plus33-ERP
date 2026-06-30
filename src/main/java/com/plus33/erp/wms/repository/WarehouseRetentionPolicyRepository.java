package com.plus33.erp.wms.repository;

import com.plus33.erp.wms.entity.WarehouseRetentionPolicy;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface WarehouseRetentionPolicyRepository extends JpaRepository<WarehouseRetentionPolicy, Long> {
    Optional<WarehouseRetentionPolicy> findByCompanyIdAndEntityName(Long companyId, String entityName);
}
