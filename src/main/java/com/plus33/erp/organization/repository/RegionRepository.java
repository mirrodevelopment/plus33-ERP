package com.plus33.erp.organization.repository;

import com.plus33.erp.organization.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.Optional;

public interface RegionRepository extends JpaRepository<Region, Long>, JpaSpecificationExecutor<Region> {
    Optional<Region> findByCode(String code);
    boolean existsByCode(String code);
    boolean existsByCompanyIdAndCode(Long companyId, String code);
}
