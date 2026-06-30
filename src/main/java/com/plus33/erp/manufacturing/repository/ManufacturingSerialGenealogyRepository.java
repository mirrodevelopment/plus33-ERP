package com.plus33.erp.manufacturing.repository;

import com.plus33.erp.manufacturing.entity.ManufacturingSerialGenealogy;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ManufacturingSerialGenealogyRepository extends JpaRepository<ManufacturingSerialGenealogy, Long> {
    List<ManufacturingSerialGenealogy> findByProductionOrderId(Long productionOrderId);
    Optional<ManufacturingSerialGenealogy> findBySerialNumber(String serialNumber);
    List<ManufacturingSerialGenealogy> findByParentSerialNumber(String parentSerialNumber);
}
