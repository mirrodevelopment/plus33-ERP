package com.plus33.erp.sales.repository;

import com.plus33.erp.sales.entity.CustomerReturnItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerReturnItemRepository extends JpaRepository<CustomerReturnItem, Long> {
    java.util.List<CustomerReturnItem> findByCustomerInvoiceItemId(Long customerInvoiceItemId);
    boolean existsBySerialIdAndCustomerReturnStatusNot(Long serialId, com.plus33.erp.sales.entity.CustomerReturnStatus status);
}
