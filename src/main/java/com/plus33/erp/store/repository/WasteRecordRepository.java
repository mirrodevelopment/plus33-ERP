/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Store Module
 * Package           : com.plus33.erp.store.repository
 * File              : WasteRecordRepository.java
 * Purpose           : JPA Repository providing database CRUD for Store Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: WasteRecordController
 * Related Service   : WasteRecordService, WasteRecordServiceImpl
 * Related Repository: WasteRecordRepository
 * Related Entity    : WasteRecord
 * Related DTO       : N/A
 * Related Mapper    : WasteRecordMapper
 * Related DB Table  : waste_records
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : WasteRecordService, WasteRecordServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Store Module against the 'waste_records' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.store.repository;

import com.plus33.erp.store.entity.WasteRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Store Module</b>
 *
 * <p><b>Class  :</b> {@code WasteRecordRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.store.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'waste_records' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code waste_records}</p>
 * <p><b>Module Deps      :</b> Store</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface WasteRecordRepository extends JpaRepository<WasteRecord, Long> {
    List<WasteRecord> findByStoreId(Long storeId);
}
