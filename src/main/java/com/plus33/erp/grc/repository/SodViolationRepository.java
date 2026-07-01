package com.plus33.erp.grc.repository;
import com.plus33.erp.grc.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface SodViolationRepository extends JpaRepository<SodViolation, Long> {
    List<SodViolation> findBySodRuleId(Long ruleId);
    List<SodViolation> findByUserId(Long userId);
    List<SodViolation> findByStatus(String status);
    long countByStatus(String status);
}
