package com.plus33.erp.grc.repository;
import com.plus33.erp.grc.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface SodRuleRepository extends JpaRepository<SodRule, Long> {
    List<SodRule> findByCompanyId(Long companyId);
    List<SodRule> findByCompanyIdAndSodType(Long companyId, String sodType);
}
