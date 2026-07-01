package com.plus33.erp.grc.repository;
import com.plus33.erp.grc.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface CorrectiveActionPlanRepository extends JpaRepository<CorrectiveActionPlan, Long> {
    List<CorrectiveActionPlan> findByIssueId(Long issueId);
    List<CorrectiveActionPlan> findByStatus(String status);
    long countByStatus(String status);
}
