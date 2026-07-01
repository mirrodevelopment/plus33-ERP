package com.plus33.erp.hcm.repository;

import com.plus33.erp.hcm.entity.JobRequisitionVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface JobRequisitionVersionRepository extends JpaRepository<JobRequisitionVersion, Long> {
    List<JobRequisitionVersion> findByRequisitionId(Long requisitionId);
}
