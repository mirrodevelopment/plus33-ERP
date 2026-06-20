package com.plus33.erp.procurement.repository;

import com.plus33.erp.procurement.entity.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface SupplierRepository extends JpaRepository<Supplier, Long>, JpaSpecificationExecutor<Supplier> {
    Optional<Supplier> findByCode(String code);
    boolean existsByCompanyIdAndCode(Long companyId, String code);
    boolean existsByCompanyIdAndEmail(Long companyId, String email);
    Page<Supplier> findByCompanyIdAndActiveTrue(Long companyId, Pageable pageable);
    Page<Supplier> findByCompanyId(Long companyId, Pageable pageable);
}
