package com.plus33.erp.finance.budget.repository;

import com.plus33.erp.finance.budget.entity.CostCenter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CostCenterRepository extends JpaRepository<CostCenter, Long>, JpaSpecificationExecutor<CostCenter> {
    Optional<CostCenter> findByCompanyIdAndCode(Long companyId, String code);
}
