package com.plus33.erp.wms.repository;

import com.plus33.erp.wms.entity.Carrier;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface CarrierRepository extends JpaRepository<Carrier, Long> {
    Optional<Carrier> findByCompanyIdAndCode(Long companyId, String code);
    List<Carrier> findByCompanyIdAndActiveTrue(Long companyId);
    Optional<Carrier> findByCompanyIdAndProviderKey(Long companyId, String providerKey);
}
