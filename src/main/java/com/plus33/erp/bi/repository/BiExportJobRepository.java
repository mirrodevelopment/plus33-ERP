package com.plus33.erp.bi.repository;

import com.plus33.erp.bi.entity.BiExportJob;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BiExportJobRepository extends JpaRepository<BiExportJob, Long> {
    java.util.List<BiExportJob> findByCompanyIdAndStatus(Long companyId, String status);
    java.util.Optional<BiExportJob> findByJobReference(String jobReference);
}
