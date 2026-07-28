package com.plus33.erp.store.task.service;

import com.plus33.erp.store.task.entity.StoreTask;
import com.plus33.erp.store.task.entity.StoreTaskReport;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

public interface StoreTaskService {

    StoreTask createTask(StoreTask task);

    List<StoreTask> splitTask(Long parentTaskId, List<StoreTask> subTasks);

    List<StoreTask> getMyTasks(String employeeEmail);

    List<StoreTask> getStoreTasks(Long storeId);

    List<Map<String, Object>> getStoreEmployees(String userEmail);

    StoreTask updateTaskStatus(Long taskId, String status, String userEmail);

    StoreTask requestDeadlineExtension(Long taskId, ZonedDateTime requestedDueDate, String reason, String employeeEmail);

    StoreTask reviewDeadlineExtension(Long taskId, boolean approved, String reviewNotes, String reviewerEmail);

    StoreTaskReport submitTaskReport(Long taskId, String status, Integer completionPercentage, String progressNotes, String attachmentUrl, String reporterName, Long reporterUserId);

    Map<String, Object> getKpis(String employeeEmail);

    void deleteTask(Long taskId);

    void purgeAllTasks();
}
