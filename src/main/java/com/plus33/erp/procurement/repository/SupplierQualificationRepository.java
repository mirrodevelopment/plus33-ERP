package com.plus33.erp.procurement.repository;

import com.plus33.erp.procurement.entity.SupplierQualification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SupplierQualificationRepository extends JpaRepository<SupplierQualification, Long> {
    Optional<SupplierQualification> findBySupplierId(Long supplierId);
}
