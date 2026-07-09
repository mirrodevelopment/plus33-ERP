/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Workforce Module
 * Package           : com.plus33.erp.workforce.service
 * File              : LeaveAccrualServiceImpl.java
 * Purpose           : Business logic service layer for Workforce Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: LeaveAccrualController
 * Related Service   : LeaveAccrualServiceImpl
 * Related Repository: LeaveAccrualLogRepository
 * Related Entity    : LeaveAccrual
 * Related DTO       : N/A
 * Related Mapper    : LeaveAccrualMapper
 * Related DB Table  : leave_accruals
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : LeaveAccrualController, LeaveAccrualServiceImplImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Workforce Module. Implements LeaveAccrualService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.workforce.service;

import com.plus33.erp.workforce.entity.LeaveAccrualLog;
import com.plus33.erp.workforce.repository.LeaveAccrualLogRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <b>PLUS33 Coffee ERP -- Workforce Module</b>
 *
 * <p><b>Class  :</b> {@code LeaveAccrualServiceImpl}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.workforce.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Workforce Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * LeaveAccrualController
 *   --> LeaveAccrualServiceImpl (this)
 *   --> Validate business rules
 *   --> LeaveAccrualRepository (read/write 'leave_accruals')
 *   --> LeaveAccrualMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code leave_accruals}</p>
 * <p><b>Module Deps      :</b> Workforce</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class LeaveAccrualServiceImpl implements LeaveAccrualService {

    private final LeaveAccrualLogRepository leaveAccrualLogRepository;

    public LeaveAccrualServiceImpl(LeaveAccrualLogRepository leaveAccrualLogRepository) {
        this.leaveAccrualLogRepository = leaveAccrualLogRepository;
    }

    /**
     * Processes the monthly accrual business workflow end-to-end.
     *
     * @param companyId owning company ID for multi-tenant data isolation
     * @param employeeId the employeeId input value
     * @param leaveType the leaveType input value
     * @param hours the hours input value
     * @param monetaryValue the monetaryValue input value
     * @return the LeaveAccrualLog result
     */
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