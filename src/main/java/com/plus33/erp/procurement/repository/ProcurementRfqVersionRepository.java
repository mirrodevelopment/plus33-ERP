package com.plus33.erp.procurement.repository;

import com.plus33.erp.procurement.entity.ProcurementRfqVersion;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProcurementRfqVersionRepository extends JpaRepository<ProcurementRfqVersion, Long> {
    List<ProcurementRfqVersion> findByRfqId(Long rfqId);
}
