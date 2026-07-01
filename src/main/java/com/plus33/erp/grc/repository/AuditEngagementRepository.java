package com.plus33.erp.grc.repository;
import com.plus33.erp.grc.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface AuditEngagementRepository extends JpaRepository<AuditEngagement, Long> {
    Optional<AuditEngagement> findByEngagementNumber(String engagementNumber);
    List<AuditEngagement> findByProgramId(Long programId);
    List<AuditEngagement> findByStatus(String status);
}
