/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.repository
 * File              : BiDataContractRepository.java
 * Purpose           : JPA Repository providing database CRUD for Bi Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: BiDataContractController
 * Related Service   : BiDataContractService, BiDataContractServiceImpl
 * Related Repository: BiDataContractRepository
 * Related Entity    : BiDataContract
 * Related DTO       : N/A
 * Related Mapper    : BiDataContractMapper
 * Related DB Table  : bi_data_contracts
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : BiDataContractService, BiDataContractServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Bi Module against the 'bi_data_contracts' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.bi.repository;

import com.plus33.erp.bi.entity.BiDataContract;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <b>PLUS33 Coffee ERP -- Bi Module</b>
 *
 * <p><b>Class  :</b> {@code BiDataContractRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.bi.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'bi_data_contracts' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code bi_data_contracts}</p>
 * <p><b>Module Deps      :</b> Bi</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface BiDataContractRepository extends JpaRepository<BiDataContract, Long> {
}