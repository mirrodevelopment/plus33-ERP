/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.repository
 * File              : InventoryAllocationRepository.java
 * Purpose           : JPA Repository providing database CRUD for Sales Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: InventoryAllocationController
 * Related Service   : InventoryAllocationService, InventoryAllocationServiceImpl
 * Related Repository: InventoryAllocationRepository
 * Related Entity    : InventoryAllocation
 * Related DTO       : N/A
 * Related Mapper    : InventoryAllocationMapper
 * Related DB Table  : inventory_allocations
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : InventoryAllocationService, InventoryAllocationServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Sales Module against the 'inventory_allocations' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.sales.repository;

import com.plus33.erp.sales.entity.AllocationStatus;
import com.plus33.erp.sales.entity.InventoryAllocation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Sales Module</b>
 *
 * <p><b>Class  :</b> {@code InventoryAllocationRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.sales.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'inventory_allocations' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code inventory_allocations}</p>
 * <p><b>Module Deps      :</b> Sales</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface InventoryAllocationRepository extends JpaRepository<InventoryAllocation, Long>, JpaSpecificationExecutor<InventoryAllocation> {

    List<InventoryAllocation> findByPickListId(Long pickListId);

    List<InventoryAllocation> findByPickListIdAndAllocationStatus(Long pickListId, AllocationStatus status);
}