package com.plus33.erp.grc.repository;
import com.plus33.erp.grc.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
public interface ComplianceFrameworkRepository extends JpaRepository<ComplianceFramework, Long> {
    Optional<ComplianceFramework> findByCode(String code);
    List<ComplianceFramework> findByCompanyId(Long companyId);
}
