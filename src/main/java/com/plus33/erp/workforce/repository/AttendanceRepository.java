/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.repository
 * File              : AttendanceRepository.java
 * Purpose           : JPA Repository providing database CRUD for Workforce Module entities
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: AttendanceController
 * Related Service   : AttendanceService, AttendanceServiceImpl
 * Related Repository: AttendanceRepository
 * Related Entity    : Attendance
 * Related DTO       : N/A
 * Related Mapper    : AttendanceMapper
 * Related DB Table  : attendances
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : AttendanceService, AttendanceServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * JPA Repository for Workforce Module against the 'attendances' table. Provides CRUD, Specification-based queries, and paginated results.
 ******************************************************************************/
package com.plus33.erp.workforce.repository;

import com.plus33.erp.workforce.entity.Attendance;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * <b>PLUS33 Coffee ERP -- Workforce Module</b>
 *
 * <p><b>Class  :</b> {@code AttendanceRepository}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.workforce.repository}</p>
 * <p><b>Layer  :</b> JPA Repository: database access for table 'attendances' via Spring Data JPA.</p>
 *
 * <p><b>Database Table   :</b> {@code attendances}</p>
 * <p><b>Module Deps      :</b> Workforce</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    Optional<Attendance> findByEmployeeIdAndAttendanceDate(Long employeeId, LocalDate attendanceDate);

    List<Attendance> findByEmployeeId(Long employeeId);

    List<Attendance> findByAttendanceDate(LocalDate attendanceDate);

    List<Attendance> findByEmployeeIdAndAttendanceDateBetween(Long employeeId, LocalDate startDate, LocalDate endDate);
}
