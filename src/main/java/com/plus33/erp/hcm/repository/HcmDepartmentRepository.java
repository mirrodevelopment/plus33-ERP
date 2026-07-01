package com.plus33.erp.hcm.repository;

import com.plus33.erp.hcm.entity.HcmDepartment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HcmDepartmentRepository extends JpaRepository<HcmDepartment, Long> {
    List<HcmDepartment> findByOrganizationIdAndIsCurrent(Long organizationId, Boolean isCurrent);
}
