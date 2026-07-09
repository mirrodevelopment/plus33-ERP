/******************************************************************************
 * Project           : PLUS33 Coffee ERP
 * Developed By      : Haulo
 * Developed For     : PLUS33 Coffee
 * Developer         : Sivasurya
 *
 * Module            : Hcm Module
 * Package           : com.plus33.erp.hcm.service
 * File              : PerformanceService.java
 * Purpose           : Business logic service layer for Hcm Module operations
 * Version           : 0.0.1-SNAPSHOT
 *
 * Related Controller: PerformanceController
 * Related Service   : PerformanceService
 * Related Repository: HcmGoalRepository, HcmCompetencyRepository, EmployeeCompetencyRepository
 * Related Entity    : Performance
 * Related DTO       : N/A
 * Related Mapper    : PerformanceMapper
 * Related DB Table  : performances
 * Related REST APIs : N/A
 * Depends On        : None
 * Used By           : PerformanceController, PerformanceServiceImpl
 *
 * Description
 * ---------------------------------------------------------------------------
 * Business service for Hcm Module. Implements PerformanceService. Encapsulates business rules, @Transactional operations, validations, and event publishing.
 ******************************************************************************/
package com.plus33.erp.hcm.service;

import com.plus33.erp.hcm.entity.*;
import com.plus33.erp.hcm.repository.*;
import com.plus33.erp.hcm.event.HcmEventBus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * <b>PLUS33 Coffee ERP -- Hcm Module</b>
 *
 * <p><b>Class  :</b> {@code PerformanceService}</p>
 * <p><b>Package:</b> {@code com.plus33.erp.hcm.service}</p>
 * <p><b>Layer  :</b> Business Service: core logic, validation, and @Transactional operations for Hcm Module.</p>
 *
 * <p><b>Service Flow:</b></p>
 * <pre>
 * PerformanceController
 *   --> PerformanceService (this)
 *   --> Validate business rules
 *   --> PerformanceRepository (read/write 'performances')
 *   --> PerformanceMapper (Entity to DTO conversion)
 *   --> Publish domain event (analytics refresh)
 *   --> Return DTO response to Controller
 * </pre>
 *
 * <p><b>Database Table   :</b> {@code performances}</p>
 * <p><b>Module Deps      :</b> Hcm</p>
 *
 * @author Sivasurya (Developed for PLUS33 Coffee by Haulo)
 * @version 0.0.1-SNAPSHOT
 */
@Service
public class PerformanceService {

    private final HcmGoalRepository goalRepository;
    private final HcmCompetencyRepository competencyRepository;
    private final EmployeeCompetencyRepository employeeCompetencyRepository;
    private final HcmEventBus eventBus;

    public PerformanceService(HcmGoalRepository goalRepository,
                              HcmCompetencyRepository competencyRepository,
                              EmployeeCompetencyRepository employeeCompetencyRepository,
                              HcmEventBus eventBus) {
        this.goalRepository = goalRepository;
        this.competencyRepository = competencyRepository;
        this.employeeCompetencyRepository = employeeCompetencyRepository;
        this.eventBus = eventBus;
    }

    /**
     * Creates a new goal and persists it to the database.
     *
     * @param employeeId the employeeId input value
     * @param description the description input value
     * @param targetDate the targetDate input value
     * @return the HcmGoal result
     * @throws BusinessException if a business rule is violated
     */
    @Transactional
    public HcmGoal addGoal(Long employeeId, String description, LocalDate targetDate) {
        HcmGoal goal = new HcmGoal();
        goal.setEmployeeId(employeeId);
        goal.setDescription(description);
        goal.setTargetDate(targetDate);
        goal.setProgressPercentage(BigDecimal.ZERO);
        goal.setStatus("IN_PROGRESS");
        goalRepository.save(goal);
        return goal;
    }

    /**
     * Updates an existing goal progress record in the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param goalId the goalId input value
     * @param progress the progress input value
     * @throws BusinessException if a business rule is violated
     */
    @Transactional
    public void updateGoalProgress(Long goalId, BigDecimal progress) {
        HcmGoal goal = goalRepository.findById(goalId)
                .orElseThrow(() -> new IllegalArgumentException("Goal not found"));

        goal.setProgressPercentage(progress);
        if (progress.compareTo(new BigDecimal("100.00")) >= 0) {
            goal.setStatus("COMPLETED");
            eventBus.publish("GoalCompleted", 1L, goal.getEmployeeId(), "Goal completed: " + goal.getDescription());
        }
        goalRepository.save(goal);
    }

    /**
     * Creates a new competency and persists it to the database.
     *
     * <p><em>@Transactional: rolled back on exception. Publishes domain event on success.</em></p>
     *
     * @param name the name input value
     * @param desc the desc input value
     * @return the HcmCompetency result
     * @throws BusinessException if a business rule is violated
     */
    @Transactional
    public HcmCompetency createCompetency(String name, String desc) {
        HcmCompetency comp = new HcmCompetency();
        comp.setName(name);
        comp.setDescription(desc);
        competencyRepository.save(comp);
        return comp;
    }

    /**
     * Performs the rateEmployeeCompetency operation in this module.
     *
     * @param employeeId the employeeId input value
     * @param competencyId the competencyId input value
     * @param rating the rating input value
     */
    @Transactional
    public void rateEmployeeCompetency(Long employeeId, Long competencyId, BigDecimal rating) {
        EmployeeCompetency ec = new EmployeeCompetency();
        ec.setEmployeeId(employeeId);
        ec.setCompetencyId(competencyId);
        ec.setRating(rating);
        employeeCompetencyRepository.save(ec);

        eventBus.publish("PerformanceCompleted", 1L, employeeId, "Competency evaluation submitted");
    }
}