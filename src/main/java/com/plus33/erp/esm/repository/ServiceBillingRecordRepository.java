package com.plus33.erp.esm.repository;

import com.plus33.erp.esm.entity.ServiceBillingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ServiceBillingRecordRepository extends JpaRepository<ServiceBillingRecord, Long> {
    List<ServiceBillingRecord> findByWorkOrderId(Long workOrderId);
}
