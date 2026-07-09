/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Sales Module
 * Package           : com.plus33.erp.sales.repository
 * File              : CustomerRepository.java
 * Purpose           : JPA Repository providing database CRUD for Sales Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: CustomerController
 * Related Service   : CustomerService, CustomerServiceImpl
 * Related Repository: CustomerRepository
 * Related Entity    : Customer
 * Related DTO       : N/A
 * Related Mapper    : CustomerMapper
 * Related DB Table  : customers
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : CustomerService, CustomerServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Sales Module against the 'customers' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.sales.repository;

import com.plus33.erp.sales.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Sales Module</b>
 *
 * <p><b>Class  :</b> {@code CustomerRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.sales.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'customers' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code customers}</p>
 * <p><b>Module Deps      :</b> Sales</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long>, JpaSpecificationExecutor<Customer> {
    Optional<Customer> findByCompanyIdAndCode(Long companyId, String code);
    boolean existsByCompanyIdAndCode(Long companyId, String code);
    boolean existsByCompanyIdAndEmail(Long companyId, String email);

    @Query(value = "SELECT nextval('customer_seq')", nativeQuery = true)
    Long getNextSequenceValue();
}