package com.plus33.erp.project.repository;

import com.plus33.erp.project.entity.ProjectDependency;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProjectDependencyRepository extends JpaRepository<ProjectDependency, Long> {
    List<ProjectDependency> findByTaskId(Long taskId);
}
