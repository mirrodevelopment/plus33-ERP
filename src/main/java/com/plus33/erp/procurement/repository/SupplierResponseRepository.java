package com.plus33.erp.procurement.repository;

import com.plus33.erp.procurement.entity.SupplierResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface SupplierResponseRepository extends JpaRepository<SupplierResponse, Long> {
    List<SupplierResponse> findByRfqVersionId(Long rfqVersionId);
}
