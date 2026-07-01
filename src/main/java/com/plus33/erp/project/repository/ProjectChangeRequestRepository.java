package com.plus33.erp.project.repository;

import com.plus33.erp.project.entity.ProjectChangeRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProjectChangeRequestRepository extends JpaRepository<ProjectChangeRequest, Long> {
    Optional<ProjectChangeRequest> findByRequestNumber(String requestNumber);
}
