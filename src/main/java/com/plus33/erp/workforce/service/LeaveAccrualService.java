/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.service
 * File              : LeaveAccrualService.java
 * Purpose           : Service interface contract defining the API for Workforce Module
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: LeaveAccrualController
 * Related Service   : LeaveAccrualService, LeaveAccrualServiceImpl
 * Related Repository: LeaveAccrualRepository
 * Related Entity    : LeaveAccrual
 * Related DTO       : N/A
 * Related Mapper    : LeaveAccrualMapper
 * Related DB Table  : leave_accruals
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : Workforce Module components
 *
 * Description
 * ---------------------------------------------------------------------------
 * Component of Workforce Module within the PLUS33 Coffee ERP platform.
 ******************************************************************************/
package com.plus33.erp.workforce.service;

import com.plus33.erp.workforce.entity.LeaveAccrualLog;
import java.math.BigDecimal;

public interface LeaveAccrualService {
    LeaveAccrualLog processMonthlyAccrual(Long companyId, Long employeeId, String leaveType, BigDecimal hours, BigDecimal monetaryValue);
}
