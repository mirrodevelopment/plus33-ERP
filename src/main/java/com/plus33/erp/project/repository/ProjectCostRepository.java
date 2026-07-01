package com.plus33.erp.project.repository;

import com.plus33.erp.project.entity.ProjectCost;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProjectCostRepository extends JpaRepository<ProjectCost, Long> {
    List<ProjectCost> findByProjectId(Long projectId);
}
