package com.plus33.erp.wms.repository;

import com.plus33.erp.wms.entity.WarehouseWorkflow;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface WarehouseWorkflowRepository extends JpaRepository<WarehouseWorkflow, Long> {
    Optional<WarehouseWorkflow> findByCompanyIdAndWorkflowCode(Long companyId, String workflowCode);
}
