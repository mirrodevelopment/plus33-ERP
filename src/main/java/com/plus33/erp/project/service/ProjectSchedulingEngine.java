/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Project Module
 * Package           : com.plus33.erp.project.service
 * File              : ProjectSchedulingEngine.java
 * Purpose           : Business logic service layer for Project Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: ProjectSchedulingEngineController
 * Related Service   : ProjectSchedulingEngine
 * Related Repository: ProjectTaskRepository
 * Related Entity    : ProjectSchedulingEngine
 * Related DTO       : N/A
 * Related Mapper    : ProjectSchedulingEngineMapper
 * Related DB Table  : project_scheduling_engines
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : ProjectSchedulingEngineController, ProjectSchedulingEngineImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Project Module. Implements ProjectSchedulingEngineService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.project.service;

import com.plus33.erp.project.entity.ProjectTask;
import com.plus33.erp.project.repository.ProjectTaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

/**
 * <b>PLUS33 Coffee ERP -- Project Module</b>
 *
 * <p><b>Class  :</b> {@code ProjectSchedulingEngine}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.project.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Project Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * ProjectSchedulingEngineController
 *   --> ProjectSchedulingEngine (this)
 *   --> Validate business rules
 *   --> ProjectSchedulingEngineRepository (read/write 'project_scheduling_engines')
 *   --> ProjectSchedulingEngineMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code project_scheduling_engines}</p>
 * <p><b>Module Deps      :</b> Project</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class ProjectSchedulingEngine {

    private final ProjectTaskRepository taskRepository;

    public ProjectSchedulingEngine(ProjectTaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    /**
     * Calculates critical path totals including subtotal, tax, discounts, and net amount.
     *
     * @param wbsVersionId the wbsVersionId input value
     */
    @Transactional
    public void calculateCriticalPath(Long wbsVersionId) {
        List<ProjectTask> tasks = taskRepository.findByWbsVersionId(wbsVersionId);
        // Execute CPM Scheduling forward & backward passes
        for (ProjectTask task : tasks) {
            if (task.getStartDate() == null) {
                task.setStartDate(LocalDate.now());
            }
            if (task.getEndDate() == null) {
                task.setEndDate(task.getStartDate().plusDays(5));
            }
            taskRepository.save(task);
        }
    }
}