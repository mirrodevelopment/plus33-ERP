/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.repository
 * File              : AttendanceSyncLogRepository.java
 * Purpose           : JPA Repository providing database CRUD for Workforce Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: AttendanceSyncLogController
 * Related Service   : AttendanceSyncLogService, AttendanceSyncLogServiceImpl
 * Related Repository: AttendanceSyncLogRepository
 * Related Entity    : AttendanceSyncLog
 * Related DTO       : N/A
 * Related Mapper    : AttendanceSyncLogMapper
 * Related DB Table  : attendance_sync_logs
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : AttendanceSyncLogService, AttendanceSyncLogServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Workforce Module against the 'attendance_sync_logs' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.AttendanceSyncLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Workforce Module</b>
 *
 * <p><b>Class  :</b> {@code AttendanceSyncLogRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.workforce.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'attendance_sync_logs' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code attendance_sync_logs}</p>
 * <p><b>Module Deps      :</b> Workforce</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Repository
public interface AttendanceSyncLogRepository extends JpaRepository<AttendanceSyncLog, Long> {
    List<AttendanceSyncLog> findByCompanyIdAndEmployeeId(Long companyId, Long employeeId);
}