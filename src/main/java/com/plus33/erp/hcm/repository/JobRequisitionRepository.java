package com.plus33.erp.hcm.repository;

import com.plus33.erp.hcm.entity.JobRequisition;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface JobRequisitionRepository extends JpaRepository<JobRequisition, Long> {
    Optional<JobRequisition> findByRequisitionNumber(String requisitionNumber);
}
