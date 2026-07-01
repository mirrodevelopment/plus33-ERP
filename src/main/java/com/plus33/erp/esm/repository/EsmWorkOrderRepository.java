package com.plus33.erp.esm.repository;

import com.plus33.erp.esm.entity.EsmWorkOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EsmWorkOrderRepository extends JpaRepository<EsmWorkOrder, Long> {
    Optional<EsmWorkOrder> findByWorkOrderNumber(String workOrderNumber);
}
