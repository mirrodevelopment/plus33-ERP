package com.plus33.erp.grc.repository;
import com.plus33.erp.grc.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface AuditFindingRepository extends JpaRepository<AuditFinding, Long> {
    Optional<AuditFinding> findByFindingNumber(String num);
    List<AuditFinding> findByEngagementId(Long engagementId);
    List<AuditFinding> findByStatus(String status);
    long countByStatus(String status);
}
