/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.repository
 * File              : PickListRepository.java
 * Purpose           : JPA Repository providing database CRUD for Sales Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PickListController
 * Related Service   : PickListService, PickListServiceImpl
 * Related Repository: PickListRepository
 * Related Entity    : PickList
 * Related DTO       : N/A
 * Related Mapper    : PickListMapper
 * Related DB Table  : pick_lists
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PickListService, PickListServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Sales Module against the 'pick_lists' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.sales.repository;

import com.plus33.erp.sales.entity.PickList;
import com.plus33.erp.sales.entity.PickListStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * <b>PLUS33 Coffee ERP -- Sales Module</b>
 *
 * <p><b>Class  :</b> {@code PickListRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.sales.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'pick_lists' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code pick_lists}</p>
 * <p><b>Module Deps      :</b> Sales</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface PickListRepository extends JpaRepository<PickList, Long>, JpaSpecificationExecutor<PickList> {

    Optional<PickList> findByCompanyIdAndPickNumber(Long companyId, String pickNumber);

    boolean existsByCompanyIdAndClientReferenceId(Long companyId, UUID clientReferenceId);

    Optional<PickList> findByCompanyIdAndClientReferenceId(Long companyId, UUID clientReferenceId);

    @Query(value = "SELECT nextval('pick_list_seq')", nativeQuery = true)
    Long getNextSequenceValue();

    boolean existsBySalesOrderIdAndWarehouseIdAndStatusNotIn(Long salesOrderId, Long warehouseId, Collection<PickListStatus> statuses);

    boolean existsBySalesOrderIdAndStoreIdAndStatusNotIn(Long salesOrderId, Long storeId, Collection<PickListStatus> statuses);

    List<PickList> findBySalesOrderId(Long salesOrderId);

    List<PickList> findByWarehouseId(Long warehouseId);

    List<PickList> findByStoreId(Long storeId);

    List<PickList> findByStatus(PickListStatus status);
}