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
