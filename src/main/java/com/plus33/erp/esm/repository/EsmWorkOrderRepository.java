/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Esm Module
 * Package           : com.plus33.erp.esm.repository
 * File              : EsmWorkOrderRepository.java
 * Purpose           : JPA Repository providing database CRUD for Esm Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: EsmWorkOrderController
 * Related Service   : EsmWorkOrderService, EsmWorkOrderServiceImpl
 * Related Repository: EsmWorkOrderRepository
 * Related Entity    : EsmWorkOrder
 * Related DTO       : N/A
 * Related Mapper    : EsmWorkOrderMapper
 * Related DB Table  : esm_work_orders
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : EsmWorkOrderService, EsmWorkOrderServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Esm Module against the 'esm_work_orders' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.esm.repository;

import com.plus33.erp.esm.entity.EsmWorkOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Esm Module</b>
 *
 * <p><b>Class  :</b> {@code EsmWorkOrderRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.esm.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'esm_work_orders' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code esm_work_orders}</p>
 * <p><b>Module Deps      :</b> Esm</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface EsmWorkOrderRepository extends JpaRepository<EsmWorkOrder, Long> {
    Optional<EsmWorkOrder> findByWorkOrderNumber(String workOrderNumber);
}
