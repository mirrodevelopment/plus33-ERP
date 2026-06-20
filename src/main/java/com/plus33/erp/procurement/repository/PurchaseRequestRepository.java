package com.plus33.erp.procurement.repository;

import com.plus33.erp.procurement.entity.PurchaseRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface PurchaseRequestRepository extends JpaRepository<PurchaseRequest, Long>, JpaSpecificationExecutor<PurchaseRequest> {
    Optional<PurchaseRequest> findByRequestNumber(String requestNumber);

    @Query(value = "SELECT nextval('purchase_request_seq')", nativeQuery = true)
    Long getNextSequenceValue();
}
