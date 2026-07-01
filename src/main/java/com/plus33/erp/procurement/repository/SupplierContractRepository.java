package com.plus33.erp.procurement.repository;

import com.plus33.erp.procurement.entity.SupplierContract;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface SupplierContractRepository extends JpaRepository<SupplierContract, Long> {
    Optional<SupplierContract> findByContractNumber(String contractNumber);
}
