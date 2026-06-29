package com.plus33.erp.workforce.service;

import com.plus33.erp.workforce.entity.AttendanceSource;
import com.plus33.erp.workforce.entity.AttendanceSyncLog;
import com.plus33.erp.workforce.repository.AttendanceSyncLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class AttendanceIntegrationServiceImpl implements AttendanceIntegrationService {

    private final AttendanceSyncLogRepository attendanceSyncLogRepository;

    public AttendanceIntegrationServiceImpl(AttendanceSyncLogRepository attendanceSyncLogRepository) {
        this.attendanceSyncLogRepository = attendanceSyncLogRepository;
    }

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
