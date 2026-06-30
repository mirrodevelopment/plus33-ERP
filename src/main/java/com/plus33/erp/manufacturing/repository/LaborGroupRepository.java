package com.plus33.erp.manufacturing.repository;

import com.plus33.erp.manufacturing.entity.LaborGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LaborGroupRepository extends JpaRepository<LaborGroup, Long> {
    List<LaborGroup> findByCompanyId(Long companyId);
    boolean existsByCompanyIdAndCode(Long companyId, String code);
}
