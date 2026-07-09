/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Wms Module
 * Package           : com.plus33.erp.wms.repository
 * File              : ShipmentRepository.java
 * Purpose           : JPA Repository providing database CRUD for Wms Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ShipmentController
 * Related Service   : ShipmentService, ShipmentServiceImpl
 * Related Repository: ShipmentRepository
 * Related Entity    : Shipment
 * Related DTO       : N/A
 * Related Mapper    : ShipmentMapper
 * Related DB Table  : shipments
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ShipmentService, ShipmentServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Wms Module against the 'shipments' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.wms.repository;

import com.plus33.erp.wms.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Wms Module</b>
 *
 * <p><b>Class  :</b> {@code ShipmentRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.wms.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'shipments' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code shipments}</p>
 * <p><b>Module Deps      :</b> Wms</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
    Optional<Shipment> findByCompanyIdAndShipmentNumber(Long companyId, String shipmentNumber);
    List<Shipment> findByCompanyIdAndWarehouseIdAndStatus(Long companyId, Long warehouseId, String status);
    List<Shipment> findByWaveId(Long waveId);
    List<Shipment> findByCarrierId(Long carrierId);
}
