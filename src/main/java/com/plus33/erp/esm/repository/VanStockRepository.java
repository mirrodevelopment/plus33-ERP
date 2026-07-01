package com.plus33.erp.esm.repository;

import com.plus33.erp.esm.entity.VanStock;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface VanStockRepository extends JpaRepository<VanStock, Long> {
    Optional<VanStock> findByCompanyIdAndVanIdAndProductId(Long companyId, Long vanId, Long productId);
}
