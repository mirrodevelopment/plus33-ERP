/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Esm Module
 * Package           : com.plus33.erp.esm.repository
 * File              : ServiceBillingRecordRepository.java
 * Purpose           : JPA Repository providing database CRUD for Esm Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ServiceBillingRecordController
 * Related Service   : ServiceBillingRecordService, ServiceBillingRecordServiceImpl
 * Related Repository: ServiceBillingRecordRepository
 * Related Entity    : ServiceBillingRecord
 * Related DTO       : N/A
 * Related Mapper    : ServiceBillingRecordMapper
 * Related DB Table  : service_billing_records
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ServiceBillingRecordService, ServiceBillingRecordServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Esm Module against the 'service_billing_records' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.esm.repository;

import com.plus33.erp.esm.entity.ServiceBillingRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Esm Module</b>
 *
 * <p><b>Class  :</b> {@code ServiceBillingRecordRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.esm.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'service_billing_records' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code service_billing_records}</p>
 * <p><b>Module Deps      :</b> Esm</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface ServiceBillingRecordRepository extends JpaRepository<ServiceBillingRecord, Long> {
    List<ServiceBillingRecord> findByWorkOrderId(Long workOrderId);
}
