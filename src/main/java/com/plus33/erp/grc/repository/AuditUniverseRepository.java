package com.plus33.erp.grc.repository;
import com.plus33.erp.grc.entity.*;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface AuditUniverseRepository extends JpaRepository<AuditUniverse, Long> {
    List<AuditUniverse> findByCompanyId(Long companyId);
}
