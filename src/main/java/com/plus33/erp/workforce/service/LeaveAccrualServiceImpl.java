package com.plus33.erp.workforce.service;

import com.plus33.erp.workforce.entity.LeaveAccrualLog;
import com.plus33.erp.workforce.repository.LeaveAccrualLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

@Service
public class LeaveAccrualServiceImpl implements LeaveAccrualService {

    private final LeaveAccrualLogRepository leaveAccrualLogRepository;

    public LeaveAccrualServiceImpl(LeaveAccrualLogRepository leaveAccrualLogRepository) {
        this.leaveAccrualLogRepository = leaveAccrualLogRepository;
    }

    @Override
    @Transactional
    public LeaveAccrualLog processMonthlyAccrual(Long companyId, Long employeeId, String leaveType, BigDecimal hours, BigDecimal monetaryValue) {
        LeaveAccrualLog log = new LeaveAccrualLog();
        log.setCompanyId(companyId);
        log.setEmployeeId(employeeId);
        log.setAccrualDate(LocalDate.now());
        log.setLeaveType(leaveType);
        log.setAccruedHours(hours);
        log.setMonetaryValue(monetaryValue);
        return leaveAccrualLogRepository.save(log);
    }
}
