/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.assets.repository
 * File              : FixedAssetWorkOrderRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: FixedAssetWorkOrderController
 * Related Service   : FixedAssetWorkOrderService, FixedAssetWorkOrderServiceImpl
 * Related Repository: FixedAssetWorkOrderRepository
 * Related Entity    : FixedAssetWorkOrder
 * Related DTO       : N/A
 * Related Mapper    : FixedAssetWorkOrderMapper
 * Related DB Table  : fixed_asset_work_orders
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : FixedAssetWorkOrderService, FixedAssetWorkOrderServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'fixed_asset_work_orders' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.assets.repository;

import com.plus33.erp.finance.assets.entity.FixedAssetWorkOrder;
import com.plus33.erp.finance.assets.entity.WorkOrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code FixedAssetWorkOrderRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.assets.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'fixed_asset_work_orders' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code fixed_asset_work_orders}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface FixedAssetWorkOrderRepository extends JpaRepository<FixedAssetWorkOrder, Long> {
    List<FixedAssetWorkOrder> findAllByFixedAssetId(Long fixedAssetId);
    List<FixedAssetWorkOrder> findAllByFixedAssetIdOrderByCreatedAtDesc(Long fixedAssetId);
    List<FixedAssetWorkOrder> findAllByStatus(WorkOrderStatus status);
}