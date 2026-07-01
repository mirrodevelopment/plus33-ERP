package com.plus33.erp.hcm.repository;

import com.plus33.erp.hcm.entity.HcmOrganization;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface HcmOrganizationRepository extends JpaRepository<HcmOrganization, Long> {
    List<HcmOrganization> findByCompanyIdAndIsCurrent(Long companyId, Boolean isCurrent);
}
