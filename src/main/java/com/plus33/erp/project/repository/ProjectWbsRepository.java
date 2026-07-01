package com.plus33.erp.project.repository;

import com.plus33.erp.project.entity.ProjectWbs;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProjectWbsRepository extends JpaRepository<ProjectWbs, Long> {
    Optional<ProjectWbs> findByProjectId(Long projectId);
}
