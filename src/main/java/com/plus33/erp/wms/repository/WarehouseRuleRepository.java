package com.plus33.erp.wms.repository;

import com.plus33.erp.wms.entity.WarehouseRule;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface WarehouseRuleRepository extends JpaRepository<WarehouseRule, Long> {
    List<WarehouseRule> findByCompanyIdAndActiveTrueOrderByPriorityAsc(Long companyId);
}
