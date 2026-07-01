package com.plus33.erp.grc.repository;
import com.plus33.erp.grc.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface EnterprisePolicyRepository extends JpaRepository<EnterprisePolicy, Long> {
    Optional<EnterprisePolicy> findByPolicyCode(String code);
    List<EnterprisePolicy> findByCompanyIdAndStatus(Long companyId, String status);
}
