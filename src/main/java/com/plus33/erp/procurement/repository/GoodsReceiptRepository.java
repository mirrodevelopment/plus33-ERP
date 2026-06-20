package com.plus33.erp.procurement.repository;

import com.plus33.erp.procurement.entity.GoodsReceipt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface GoodsReceiptRepository extends JpaRepository<GoodsReceipt, Long>, JpaSpecificationExecutor<GoodsReceipt> {

    Optional<GoodsReceipt> findByClientReferenceId(UUID clientReferenceId);

    @Query(value = "SELECT nextval('goods_receipt_seq')", nativeQuery = true)
    Long getNextSequenceValue();
}
