package com.plus33.erp.project.repository;

import com.plus33.erp.project.entity.ResourceAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ResourceAssignmentRepository extends JpaRepository<ResourceAssignment, Long> {
    List<ResourceAssignment> findByResourceId(Long resourceId);
    List<ResourceAssignment> findByTaskId(Long taskId);
}
