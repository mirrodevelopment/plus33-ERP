package com.plus33.erp.sales.repository;

import com.plus33.erp.sales.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {
    Optional<Customer> findByCompanyIdAndCode(Long companyId, String code);
    boolean existsByCompanyIdAndCode(Long companyId, String code);
    boolean existsByCompanyIdAndEmail(Long companyId, String email);

    @Query(value = "SELECT nextval('customer_seq')", nativeQuery = true)
    Long getNextSequenceValue();
}
