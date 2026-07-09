/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.service
 * File              : AttendanceIntegrationService.java
 * Purpose           : Service interface contract defining the API for Workforce Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: AttendanceIntegrationController
 * Related Service   : AttendanceIntegrationService, AttendanceIntegrationServiceImpl
 * Related Repository: AttendanceIntegrationRepository
 * Related Entity    : AttendanceIntegration
 * Related DTO       : N/A
 * Related Mapper    : AttendanceIntegrationMapper
 * Related DB Table  : attendance_integrations
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Workforce Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Workforce Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.workforce.service;

import com.plus33.erp.workforce.entity.AttendanceSource;
import com.plus33.erp.workforce.entity.AttendanceSyncLog;
import java.math.BigDecimal;

/**
 * <b>PLUS33 Coffee ERP -- Workforce Module</b>
 *
 * <p><b>Class  :</b> {@code AttendanceIntegrationService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.workforce.service}</p>
 * <p><b>Layer  :</b> Component of Workforce Module in the PLUS33 Coffee ERP platform.</p>
 *
 * <p><b>Module Deps      :</b> Workforce</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
public interface AttendanceIntegrationService {
    AttendanceSyncLog recordAttendanceSync(Long companyId, Long employeeId, AttendanceSource source, BigDecimal hoursWorked, BigDecimal overtimeHours);
}
