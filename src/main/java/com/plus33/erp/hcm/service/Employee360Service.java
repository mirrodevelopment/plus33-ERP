/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Hcm Module
 * Package           : com.plus33.erp.hcm.service
 * File              : Employee360Service.java
 * Purpose           : Business logic service layer for Hcm Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: Employee360Controller
 * Related Service   : Employee360Service
 * Related Repository: EmployeeRepository, EmployeeLifecycleRepository, HcmGoalRepository
 * Related Entity    : Employee360
 * Related DTO       : N/A
 * Related Mapper    : Employee360Mapper
 * Related DB Table  : employee360s
 * Related REST APIs : N/A
 * Depends On        : Workforce Module
 * Used By           : Employee360Controller, Employee360ServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Hcm Module. Implements Employee360Service. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.hcm.service;

import com.plus33.erp.workforce.entity.Employee;
import com.plus33.erp.workforce.repository.EmployeeRepository;
import com.plus33.erp.hcm.entity.EmployeeLifecycle;
import com.plus33.erp.hcm.entity.HcmGoal;
import com.plus33.erp.hcm.repository.EmployeeLifecycleRepository;
import com.plus33.erp.hcm.repository.HcmGoalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <b>PLUS33 Coffee ERP -- Hcm Module</b>
 *
 * <p><b>Class  :</b> {@code Employee360Service}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.hcm.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Hcm Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * Employee360Controller
 *   --> Employee360Service (this)
 *   --> Validate business rules
 *   --> Employee360Repository (read/write 'employee360s')
 *   --> Employee360Mapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code employee360s}</p>
 * <p><b>Module Deps      :</b> Workforce, Hcm</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class Employee360Service {

    private final EmployeeRepository employeeRepository;
    private final EmployeeLifecycleRepository lifecycleRepository;
    private final HcmGoalRepository goalRepository;

    public Employee360Service(EmployeeRepository employeeRepository,
                              EmployeeLifecycleRepository lifecycleRepository,
                              HcmGoalRepository goalRepository) {
        this.employeeRepository = employeeRepository;
        this.lifecycleRepository = lifecycleRepository;
        this.goalRepository = goalRepository;
    }

    /**
     * Performs the compileProfileSnapshot operation in this module.
     *
     * @param employeeId the employeeId input value
     * @return the result string value
     */
    @Transactional(readOnly = true)
    public Map<String, Object> compileProfileSnapshot(Long employeeId) {
        Employee emp = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new IllegalArgumentException("Employee profile not found"));

        EmployeeLifecycle lifecycle = lifecycleRepository.findByEmployeeId(employeeId).orElse(null);
        List<HcmGoal> goals = goalRepository.findByEmployeeId(employeeId);

        Map<String, Object> profile = new HashMap<>();
        profile.put("code", emp.getEmployeeCode());
        profile.put("name", emp.getFirstName() + " " + emp.getLastName());
        profile.put("status", lifecycle != null ? lifecycle.getLifecycleStatus() : "UNKNOWN");
        profile.put("goalsCount", goals.size());
        profile.put("designation", emp.getDesignation());

        return profile;
    }
}