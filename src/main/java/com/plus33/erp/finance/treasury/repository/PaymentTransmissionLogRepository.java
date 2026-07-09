/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Finance Module
 * Package           : com.plus33.erp.finance.treasury.repository
 * File              : PaymentTransmissionLogRepository.java
 * Purpose           : JPA Repository providing database CRUD for Finance Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PaymentTransmissionLogController
 * Related Service   : PaymentTransmissionLogService, PaymentTransmissionLogServiceImpl
 * Related Repository: PaymentTransmissionLogRepository
 * Related Entity    : PaymentTransmissionLog
 * Related DTO       : N/A
 * Related Mapper    : PaymentTransmissionLogMapper
 * Related DB Table  : payment_transmission_logs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PaymentTransmissionLogService, PaymentTransmissionLogServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Finance Module against the 'payment_transmission_logs' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.finance.treasury.repository;

import com.plus33.erp.finance.treasury.entity.PaymentTransmissionLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Finance Module</b>
 *
 * <p><b>Class  :</b> {@code PaymentTransmissionLogRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.finance.treasury.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'payment_transmission_logs' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code payment_transmission_logs}</p>
 * <p><b>Module Deps      :</b> Finance</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface PaymentTransmissionLogRepository extends JpaRepository<PaymentTransmissionLog, Long> {
    List<PaymentTransmissionLog> findByFileId(Long fileId);
}