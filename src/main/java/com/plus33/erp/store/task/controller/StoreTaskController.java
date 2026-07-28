package com.plus33.erp.store.task.controller;

import com.plus33.erp.store.task.entity.StoreTask;
import com.plus33.erp.store.task.entity.StoreTaskReport;
import com.plus33.erp.store.task.service.StoreTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/store-tasks")
@CrossOrigin(origins = "*")
public class StoreTaskController {

    private final StoreTaskService taskService;

    @Autowired
    public StoreTaskController(StoreTaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/kpis")
    public ResponseEntity<?> getKpis(@RequestParam(value = "employeeEmail", required = false) String employeeEmail) {
        Map<String, Object> kpis = taskService.getKpis(employeeEmail);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", kpis);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/my-tasks")
    public ResponseEntity<?> getMyTasks(@RequestParam(value = "employeeEmail", required = false) String employeeEmail) {
        List<StoreTask> tasks = taskService.getMyTasks(employeeEmail);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", tasks);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/store-tasks")
    public ResponseEntity<?> getStoreTasks(@RequestParam(value = "storeId", required = false) Long storeId) {
        List<StoreTask> tasks = taskService.getStoreTasks(storeId);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", tasks);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/employees")
    public ResponseEntity<?> getStoreEmployees(@RequestParam(value = "userEmail", required = false) String userEmail) {
        List<Map<String, Object>> employees = taskService.getStoreEmployees(userEmail);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", employees);
        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<?> createTask(@RequestBody StoreTask task) {
        StoreTask created = taskService.createTask(task);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", created);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/split")
    public ResponseEntity<?> splitTask(@PathVariable("id") Long parentTaskId, @RequestBody List<StoreTask> subTasks) {
        List<StoreTask> createdSubTasks = taskService.splitTask(parentTaskId, subTasks);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", createdSubTasks);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateTaskStatus(
            @PathVariable("id") Long taskId,
            @RequestParam("status") String status,
            @RequestParam(value = "userEmail", required = false) String userEmail) {
        StoreTask updated = taskService.updateTaskStatus(taskId, status, userEmail);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", updated);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/extension-request")
    public ResponseEntity<?> requestExtension(
            @PathVariable("id") Long taskId,
            @RequestBody Map<String, Object> payload,
            @RequestParam(value = "userEmail", required = false) String userEmail) {
        String requestedDateStr = (String) payload.get("requestedDueDate");
        String reason = (String) payload.get("reason");
        ZonedDateTime requestedDueDate = requestedDateStr != null ? ZonedDateTime.parse(requestedDateStr) : ZonedDateTime.now().plusDays(1);

        StoreTask updated = taskService.requestDeadlineExtension(taskId, requestedDueDate, reason, userEmail);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", updated);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}/extension-review")
    public ResponseEntity<?> reviewExtension(
            @PathVariable("id") Long taskId,
            @RequestBody Map<String, Object> payload,
            @RequestParam(value = "userEmail", required = false) String userEmail) {
        Boolean approved = (Boolean) payload.getOrDefault("approved", false);
        String reviewNotes = (String) payload.get("reviewNotes");

        StoreTask updated = taskService.reviewDeadlineExtension(taskId, approved, reviewNotes, userEmail);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", updated);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{id}/reports")
    public ResponseEntity<?> submitReport(
            @PathVariable("id") Long taskId,
            @RequestBody StoreTaskReport reportReq) {
        StoreTaskReport created = taskService.submitTaskReport(
                taskId,
                reportReq.getStatus(),
                reportReq.getCompletionPercentage(),
                reportReq.getProgressNotes(),
                reportReq.getAttachmentUrl(),
                reportReq.getReporterName(),
                reportReq.getReporterUserId()
        );
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("data", created);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteTask(@PathVariable("id") Long taskId) {
        taskService.deleteTask(taskId);
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "Task deleted successfully");
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/purge-all")
    public ResponseEntity<?> purgeAllTasks() {
        taskService.purgeAllTasks();
        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("message", "All tasks purged successfully across store database");
        return ResponseEntity.ok(response);
    }
}
