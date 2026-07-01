package com.plus33.erp.grc.repository;
import com.plus33.erp.grc.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface ControlTestResultRepository extends JpaRepository<ControlTestResult, Long> {
    List<ControlTestResult> findByTestPlanId(Long testPlanId);
    long countByResult(String result);
}
