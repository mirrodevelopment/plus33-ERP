/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.service
 * File              : AttendanceIntegrationServiceImpl.java
 * Purpose           : Business logic service layer for Workforce Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: AttendanceIntegrationController
 * Related Service   : AttendanceIntegrationServiceImpl
 * Related Repository: AttendanceSyncLogRepository
 * Related Entity    : AttendanceIntegration
 * Related DTO       : N/A
 * Related Mapper    : AttendanceIntegrationMapper
 * Related DB Table  : attendance_integrations
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : AttendanceIntegrationController, AttendanceIntegrationServiceImplImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Workforce Module. Implements AttendanceIntegrationService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.workforce.service;

import com.plus33.erp.workforce.entity.AttendanceSource;
import com.plus33.erp.workforce.entity.AttendanceSyncLog;
import com.plus33.erp.workforce.repository.AttendanceSyncLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <b>PLUS33 Coffee ERP -- Workforce Module</b>
 *
 * <p><b>Class  :</b> {@code AttendanceIntegrationServiceImpl}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.workforce.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Workforce Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * AttendanceIntegrationController
 *   --> AttendanceIntegrationServiceImpl (this)
 *   --> Validate business rules
 *   --> AttendanceIntegrationRepository (read/write 'attendance_integrations')
 *   --> AttendanceIntegrationMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code attendance_integrations}</p>
 * <p><b>Module Deps      :</b> Workforce</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class AttendanceIntegrationServiceImpl implements AttendanceIntegrationService {

    private final AttendanceSyncLogRepository attendanceSyncLogRepository;

    public AttendanceIntegrationServiceImpl(AttendanceSyncLogRepository attendanceSyncLogRepository) {
        this.attendanceSyncLogRepository = attendanceSyncLogRepository;
    }

    /**
     * Performs the recordAttendanceSync operation in this module.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param employeeId the employeeId input value
     * @param source the source entity or DTO to convert
     * @param hoursWorked the hoursWorked input value
     * @param overtimeHours the overtimeHours input value
     * @return the AttendanceSyncLog result
     */
    @Override
    @Transactional
    public AttendanceSyncLog recordAttendanceSync(Long companyId, Long employeeId, AttendanceSource source, BigDecimal hoursWorked, BigDecimal overtimeHours) {
        AttendanceSyncLog log = new AttendanceSyncLog();
        log.setCompanyId(companyId);
        log.setEmployeeId(employeeId);
        log.setSyncDate(LocalDate.now());
        log.setAttendanceSource(source);
        log.setHoursWorked(hoursWorked);
        log.setOvertimeHours(overtimeHours);
        return attendanceSyncLogRepository.save(log);
    }
}