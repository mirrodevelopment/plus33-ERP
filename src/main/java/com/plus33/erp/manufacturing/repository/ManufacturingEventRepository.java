package com.plus33.erp.manufacturing.repository;

import com.plus33.erp.manufacturing.entity.ManufacturingEvent;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ManufacturingEventRepository extends JpaRepository<ManufacturingEvent, Long> {
    List<ManufacturingEvent> findByCompanyId(Long companyId);
    List<ManufacturingEvent> findByCompanyIdAndEventType(Long companyId, String eventType);
    List<ManufacturingEvent> findByReferenceTypeAndReferenceId(String referenceType, Long referenceId);
}
