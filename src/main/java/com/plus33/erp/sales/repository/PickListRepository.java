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
