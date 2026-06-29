package com.plus33.erp.workforce.service;

import com.plus33.erp.workforce.entity.AttendanceSource;
import com.plus33.erp.workforce.entity.AttendanceSyncLog;
import java.math.BigDecimal;

public interface AttendanceIntegrationService {
    AttendanceSyncLog recordAttendanceSync(Long companyId, Long employeeId, AttendanceSource source, BigDecimal hoursWorked, BigDecimal overtimeHours);
}
