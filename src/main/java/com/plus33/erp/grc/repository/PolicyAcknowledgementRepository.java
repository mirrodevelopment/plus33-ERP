package com.plus33.erp.grc.repository;
import com.plus33.erp.grc.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface PolicyAcknowledgementRepository extends JpaRepository<PolicyAcknowledgement, Long> {
    List<PolicyAcknowledgement> findByPolicyVersionId(Long policyVersionId);
    boolean existsByPolicyVersionIdAndEmployeeId(Long policyVersionId, Long employeeId);
    long countByPolicyVersionId(Long policyVersionId);
}
