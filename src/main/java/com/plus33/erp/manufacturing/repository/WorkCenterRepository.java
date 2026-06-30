package com.plus33.erp.manufacturing.repository;

import com.plus33.erp.manufacturing.entity.WorkCenter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WorkCenterRepository extends JpaRepository<WorkCenter, Long> {

    List<WorkCenter> findByCompanyIdAndActiveTrue(Long companyId);

    Optional<WorkCenter> findByCompanyIdAndCode(Long companyId, String code);

    boolean existsByCompanyIdAndCode(Long companyId, String code);

    default List<WorkCenter> findByCompanyId(Long companyId) {
        return findByCompanyIdAndActiveTrue(companyId);
    }

    List<WorkCenter> findByCompanyIdAndWorkCenterType(Long companyId, String workCenterType);
}
