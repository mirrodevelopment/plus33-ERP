package com.plus33.erp.sales.repository;

import com.plus33.erp.sales.entity.SalesOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface SalesOrderRepository extends JpaRepository<SalesOrder, Long>, JpaSpecificationExecutor<SalesOrder> {

    Optional<SalesOrder> findByCompanyIdAndOrderNumber(Long companyId, String orderNumber);

    boolean existsByCompanyIdAndOrderNumber(Long companyId, String orderNumber);

    boolean existsByCompanyIdAndClientReferenceId(Long companyId, UUID clientReferenceId);

    Optional<SalesOrder> findByCompanyIdAndClientReferenceId(Long companyId, UUID clientReferenceId);

    @Query(value = "SELECT nextval('sales_order_seq')", nativeQuery = true)
    Long getNextSequenceValue();
}
