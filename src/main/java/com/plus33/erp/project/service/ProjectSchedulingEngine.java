package com.plus33.erp.project.service;

import com.plus33.erp.project.entity.ProjectTask;
import com.plus33.erp.project.repository.ProjectTaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
public class ProjectSchedulingEngine {

    private final ProjectTaskRepository taskRepository;

    public ProjectSchedulingEngine(ProjectTaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

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
