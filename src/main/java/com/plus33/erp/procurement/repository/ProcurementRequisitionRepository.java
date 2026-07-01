package com.plus33.erp.procurement.repository;

import com.plus33.erp.procurement.entity.ProcurementRequisition;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProcurementRequisitionRepository extends JpaRepository<ProcurementRequisition, Long> {
    Optional<ProcurementRequisition> findByRequisitionNumber(String requisitionNumber);
}
