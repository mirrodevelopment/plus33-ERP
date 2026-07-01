package com.plus33.erp.procurement.repository;

import com.plus33.erp.procurement.entity.ProcurementRfq;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ProcurementRfqRepository extends JpaRepository<ProcurementRfq, Long> {
    Optional<ProcurementRfq> findByRfqNumber(String rfqNumber);
}
