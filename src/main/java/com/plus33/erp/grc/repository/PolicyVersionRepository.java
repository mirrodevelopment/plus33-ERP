package com.plus33.erp.grc.repository;
import com.plus33.erp.grc.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface PolicyVersionRepository extends JpaRepository<PolicyVersion, Long> {
    List<PolicyVersion> findByPolicyId(Long policyId);
    Optional<PolicyVersion> findByPolicyIdAndVersionNumber(Long policyId, Integer versionNumber);
}
