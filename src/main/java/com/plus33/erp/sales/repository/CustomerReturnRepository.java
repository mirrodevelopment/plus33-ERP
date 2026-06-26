package com.plus33.erp.sales.repository;

import com.plus33.erp.sales.entity.CustomerReturn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerReturnRepository extends JpaRepository<CustomerReturn, Long>, JpaSpecificationExecutor<CustomerReturn> {
    Optional<CustomerReturn> findByClientReferenceId(UUID clientReferenceId);
    Optional<CustomerReturn> findByCompanyIdAndReturnNumber(Long companyId, String returnNumber);

    @Query(value = "SELECT nextval('customer_return_seq')", nativeQuery = true)
    Long getNextSequenceValue();
}
