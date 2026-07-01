package com.plus33.erp.hcm.repository;

import com.plus33.erp.hcm.entity.LearningEnrollment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LearningEnrollmentRepository extends JpaRepository<LearningEnrollment, Long> {
    List<LearningEnrollment> findByEmployeeId(Long employeeId);
}
