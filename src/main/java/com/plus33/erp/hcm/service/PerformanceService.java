package com.plus33.erp.hcm.service;

import com.plus33.erp.hcm.entity.*;
import com.plus33.erp.hcm.repository.*;
import com.plus33.erp.hcm.event.HcmEventBus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDate;

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

    @Transactional
    public HcmCompetency createCompetency(String name, String desc) {
        HcmCompetency comp = new HcmCompetency();
        comp.setName(name);
        comp.setDescription(desc);
        competencyRepository.save(comp);
        return comp;
    }

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
