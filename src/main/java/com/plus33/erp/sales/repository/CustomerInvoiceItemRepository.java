package com.plus33.erp.sales.repository;

import com.plus33.erp.sales.entity.CustomerInvoiceItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerInvoiceItemRepository extends JpaRepository<CustomerInvoiceItem, Long> {
}
