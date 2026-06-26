package com.plus33.erp.sales.repository;

import com.plus33.erp.sales.entity.CustomerInvoice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerInvoiceRepository extends JpaRepository<CustomerInvoice, Long>, JpaSpecificationExecutor<CustomerInvoice> {

    Optional<CustomerInvoice> findByCompanyIdAndInvoiceNumber(Long companyId, String invoiceNumber);

    Optional<CustomerInvoice> findByCompanyIdAndClientReferenceId(Long companyId, UUID clientReferenceId);

    @Query(value = "SELECT nextval('customer_invoice_seq')", nativeQuery = true)
    Long getNextSequenceValue();
}
