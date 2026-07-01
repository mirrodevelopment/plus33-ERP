package com.plus33.erp.hcm.repository;

import com.plus33.erp.hcm.entity.PositionAssignment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PositionAssignmentRepository extends JpaRepository<PositionAssignment, Long> {
    List<PositionAssignment> findByPositionIdAndIsCurrent(Long positionId, Boolean isCurrent);
}
