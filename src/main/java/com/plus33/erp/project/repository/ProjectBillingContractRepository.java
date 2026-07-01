package com.plus33.erp.project.repository;

import com.plus33.erp.project.entity.ProjectBillingContract;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProjectBillingContractRepository extends JpaRepository<ProjectBillingContract, Long> {
    Optional<ProjectBillingContract> findByProjectId(Long projectId);
}
