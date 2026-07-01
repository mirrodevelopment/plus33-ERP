package com.plus33.erp.grc.repository;
import com.plus33.erp.grc.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface EnterpriseIssueRepository extends JpaRepository<EnterpriseIssue, Long> {
    Optional<EnterpriseIssue> findByIssueNumber(String num);
    List<EnterpriseIssue> findByCompanyIdAndStatus(Long companyId, String status);
    long countByCompanyIdAndStatus(Long companyId, String status);
}
