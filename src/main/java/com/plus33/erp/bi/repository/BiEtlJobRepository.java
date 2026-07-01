package com.plus33.erp.bi.repository;

import com.plus33.erp.bi.entity.BiEtlJob;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BiEtlJobRepository extends JpaRepository<BiEtlJob, Long> {
    java.util.List<BiEtlJob> findByStatusAndEnabledTrue(String status);
    java.util.List<BiEtlJob> findBySourceModuleAndEnabledTrue(String sourceModule);
}
