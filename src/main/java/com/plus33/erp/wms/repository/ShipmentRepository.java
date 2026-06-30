package com.plus33.erp.wms.repository;

import com.plus33.erp.wms.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
    Optional<Shipment> findByCompanyIdAndShipmentNumber(Long companyId, String shipmentNumber);
    List<Shipment> findByCompanyIdAndWarehouseIdAndStatus(Long companyId, Long warehouseId, String status);
    List<Shipment> findByWaveId(Long waveId);
    List<Shipment> findByCarrierId(Long carrierId);
}
