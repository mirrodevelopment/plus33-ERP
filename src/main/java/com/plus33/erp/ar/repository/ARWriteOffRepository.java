package com.plus33.erp.ar.repository;

import com.plus33.erp.ar.entity.ARWriteOff;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ARWriteOffRepository extends JpaRepository<ARWriteOff, Long>, JpaSpecificationExecutor<ARWriteOff> {

    Optional<ARWriteOff> findByCompanyIdAndWriteOffNumber(Long companyId, String writeOffNumber);

    List<ARWriteOff> findByCustomerInvoiceId(Long customerInvoiceId);

    List<ARWriteOff> findByCustomerIdAndCompanyId(Long customerId, Long companyId);

    @Query(value = "SELECT nextval('ar_write_off_seq')", nativeQuery = true)
    Long getNextSequenceValue();
}
