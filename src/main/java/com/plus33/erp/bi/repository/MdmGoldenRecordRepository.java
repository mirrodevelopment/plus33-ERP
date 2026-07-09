/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Bi Module
 * Package           : com.plus33.erp.bi.repository
 * File              : MdmGoldenRecordRepository.java
 * Purpose           : JPA Repository providing database CRUD for Bi Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: MdmGoldenRecordController
 * Related Service   : MdmGoldenRecordService, MdmGoldenRecordServiceImpl
 * Related Repository: MdmGoldenRecordRepository
 * Related Entity    : MdmGoldenRecord
 * Related DTO       : N/A
 * Related Mapper    : MdmGoldenRecordMapper
 * Related DB Table  : mdm_golden_records
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : MdmGoldenRecordService, MdmGoldenRecordServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Bi Module against the 'mdm_golden_records' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.bi.repository;

import com.plus33.erp.bi.entity.MdmGoldenRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * <b>PLUS33 Coffee ERP -- Bi Module</b>
 *
 * <p><b>Class  :</b> {@code MdmGoldenRecordRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.bi.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'mdm_golden_records' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code mdm_golden_records}</p>
 * <p><b>Module Deps      :</b> Bi</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface MdmGoldenRecordRepository extends JpaRepository<MdmGoldenRecord, Long> {
}