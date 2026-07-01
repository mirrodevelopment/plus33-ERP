package com.plus33.erp.project.repository;

import com.plus33.erp.project.entity.ProjectRisk;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProjectRiskRepository extends JpaRepository<ProjectRisk, Long> {
    List<ProjectRisk> findByProjectId(Long projectId);
}
