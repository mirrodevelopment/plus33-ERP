package com.plus33.erp.project.repository;

import com.plus33.erp.project.entity.ProjectTask;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProjectTaskRepository extends JpaRepository<ProjectTask, Long> {
    List<ProjectTask> findByWbsVersionId(Long wbsVersionId);
}
