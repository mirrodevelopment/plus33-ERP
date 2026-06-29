package com.plus33.erp.workforce.service;

import com.plus33.erp.workforce.entity.LeaveAccrualLog;
import java.math.BigDecimal;

public interface LeaveAccrualService {
    LeaveAccrualLog processMonthlyAccrual(Long companyId, Long employeeId, String leaveType, BigDecimal hours, BigDecimal monetaryValue);
}
