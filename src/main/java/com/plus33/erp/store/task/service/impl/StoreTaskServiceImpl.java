package com.plus33.erp.store.task.service.impl;

import com.plus33.erp.security.entity.User;
import com.plus33.erp.security.repository.UserRepository;
import com.plus33.erp.store.task.entity.StoreTask;
import com.plus33.erp.store.task.entity.StoreTaskReport;
import com.plus33.erp.store.task.repository.StoreTaskReportRepository;
import com.plus33.erp.store.task.repository.StoreTaskRepository;
import com.plus33.erp.store.task.service.StoreTaskService;
import com.plus33.erp.workforce.entity.Employee;
import com.plus33.erp.workforce.entity.UserStore;
import com.plus33.erp.workforce.repository.EmployeeRepository;
import com.plus33.erp.workforce.repository.UserStoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

@Service
@Transactional
public class StoreTaskServiceImpl implements StoreTaskService {

    private final StoreTaskRepository taskRepository;
    private final StoreTaskReportRepository reportRepository;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final UserStoreRepository userStoreRepository;

    @Autowired
    public StoreTaskServiceImpl(StoreTaskRepository taskRepository,
                                 StoreTaskReportRepository reportRepository,
                                 EmployeeRepository employeeRepository,
                                 UserRepository userRepository,
                                 UserStoreRepository userStoreRepository) {
        this.taskRepository = taskRepository;
        this.reportRepository = reportRepository;
        this.employeeRepository = employeeRepository;
        this.userRepository = userRepository;
        this.userStoreRepository = userStoreRepository;
    }

    @Override
    public StoreTask createTask(StoreTask task) {
        // Force ID to null for new creations so JPA performs SQL INSERT instead of UPDATE
        task.setId(null);
        if (task.getCreatedAt() == null) task.setCreatedAt(ZonedDateTime.now());
        task.setUpdatedAt(ZonedDateTime.now());

        // Null Safety Defaults
        if (task.getCategory() == null || task.getCategory().trim().isEmpty()) {
            task.setCategory("GENERAL");
        }
        if (task.getPriority() == null || task.getPriority().trim().isEmpty()) {
            task.setPriority("COMMON");
        }
        if (task.getStatus() == null || task.getStatus().trim().isEmpty()) {
            task.setStatus("ASSIGNED");
        }
        if (task.getDelegationMode() == null || task.getDelegationMode().trim().isEmpty()) {
            task.setDelegationMode("DIRECT_EMPLOYEE");
        }
        if (task.getExtensionStatus() == null || task.getExtensionStatus().trim().isEmpty()) {
            task.setExtensionStatus("NONE");
        }
        if (task.getDueDate() == null) {
            task.setDueDate(ZonedDateTime.now().plusDays(1));
        }

        if (task.getAssignedEmployeeEmail() != null) {
            task.setAssignedEmployeeEmail(task.getAssignedEmployeeEmail().trim().toLowerCase());
        }

        // Handle Immediate Task Preemption
        if ("IMMEDIATE".equalsIgnoreCase(task.getPriority())) {
            task.setPreemptiveImmediate(true);
            if (task.getAssignedEmployeeEmail() != null) {
                List<String> activeStatuses = Arrays.asList("STARTED", "IN_PROGRESS");
                List<StoreTask> activeTasks = taskRepository.findByAssignedEmployeeEmailAndStatusIn(
                        task.getAssignedEmployeeEmail().toLowerCase(), activeStatuses);
                if (!activeTasks.isEmpty()) {
                    StoreTask currentActive = activeTasks.get(0);
                    currentActive.setStatus("PAUSED_FOR_IMMEDIATE");
                    taskRepository.save(currentActive);
                    task.setPausedTaskId(currentActive.getId());
                }
            }
        }

        return taskRepository.save(task);
    }

    @Override
    public List<StoreTask> splitTask(Long parentTaskId, List<StoreTask> subTasks) {
        StoreTask parent = taskRepository.findById(parentTaskId)
                .orElseThrow(() -> new IllegalArgumentException("Parent task not found: " + parentTaskId));

        parent.setStatus("SPLIT");
        taskRepository.save(parent);

        List<StoreTask> savedSubTasks = new ArrayList<>();
        for (StoreTask sub : subTasks) {
            sub.setParentTaskId(parent.getId());
            sub.setStoreId(parent.getStoreId());
            sub.setShiftId(parent.getShiftId());
            sub.setDelegationMode("SPLIT_SUBTASK");
            if (sub.getCategory() == null) sub.setCategory(parent.getCategory());
            if (sub.getPriority() == null) sub.setPriority(parent.getPriority());
            savedSubTasks.add(createTask(sub));
        }
        return savedSubTasks;
    }

    @Override
    public List<StoreTask> getMyTasks(String employeeEmail) {
        return getStoreTasks(null);
    }

    @Override
    public List<StoreTask> getStoreTasks(Long storeId) {
        List<StoreTask> list = taskRepository.findAll();
        list.sort((a, b) -> {
            if (b.getCreatedAt() != null && a.getCreatedAt() != null) {
                return b.getCreatedAt().compareTo(a.getCreatedAt());
            }
            return Long.compare(b.getId() != null ? b.getId() : 0, a.getId() != null ? a.getId() : 0);
        });
        return list;
    }

    @Override
    public List<Map<String, Object>> getStoreEmployees(String userEmail) {
        List<Map<String, Object>> result = new ArrayList<>();
        Set<String> addedEmails = new HashSet<>();

        String cleanEmail = userEmail != null ? userEmail.trim().toLowerCase() : "";

        // Extract store code token from user email if present (e.g. st_fr_reg_1_01)
        String storeCodeToken = "";
        if (cleanEmail.contains("st_")) {
            int idx = cleanEmail.indexOf("st_");
            storeCodeToken = cleanEmail.substring(idx);
            if (storeCodeToken.contains("@")) {
                storeCodeToken = storeCodeToken.split("@")[0];
            }
        }

        Long targetStoreId = null;
        if (!cleanEmail.isEmpty()) {
            Optional<User> adminOpt = userRepository.findByEmail(cleanEmail);
            if (!adminOpt.isPresent()) {
                String prefix = cleanEmail.contains("@") ? cleanEmail.split("@")[0] : cleanEmail;
                List<User> userList = userRepository.findAll();
                adminOpt = userList.stream()
                        .filter(u -> u.getEmail() != null && u.getEmail().toLowerCase().contains(prefix))
                        .findFirst();
            }

            if (adminOpt.isPresent()) {
                List<UserStore> userStores = userStoreRepository.findByIdUserId(adminOpt.get().getId());
                if (!userStores.isEmpty()) {
                    targetStoreId = userStores.get(0).getId().getStoreId();
                }
            }
        }

        Set<Long> storeUserIds = new HashSet<>();
        if (targetStoreId != null) {
            List<UserStore> storeMappings = userStoreRepository.findByIdStoreId(targetStoreId);
            for (UserStore us : storeMappings) {
                storeUserIds.add(us.getId().getUserId());
            }
        }

        // Load from employees table
        try {
            List<Employee> allEmps = employeeRepository.findAll();
            for (Employee emp : allEmps) {
                String email = emp.getEmail() != null ? emp.getEmail().toLowerCase().trim() : "";
                String desig = emp.getDesignation() != null ? emp.getDesignation().toLowerCase() : "";

                // Exclude Ultimate Admin and Regional Admin roles
                if (desig.contains("ultimate") || desig.contains("regional admin") || desig.contains("director") || email.contains("ultimate") || email.contains("regional_")) {
                    continue;
                }

                boolean isStoreMember = false;
                if (!storeUserIds.isEmpty() && emp.getUser() != null && storeUserIds.contains(emp.getUser().getId())) {
                    isStoreMember = true;
                } else if (!storeCodeToken.isEmpty() && email.contains(storeCodeToken)) {
                    isStoreMember = true;
                } else if (storeUserIds.isEmpty() && storeCodeToken.isEmpty()) {
                    isStoreMember = !email.contains("ultimate") && !email.contains("regional_");
                }

                if (isStoreMember && !email.isEmpty() && !addedEmails.contains(email)) {
                    addedEmails.add(email);
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", emp.getId());
                    map.put("employeeCode", emp.getEmployeeCode());
                    String fullName = (emp.getFirstName() + " " + (emp.getLastName() != null ? emp.getLastName() : "")).trim();
                    map.put("name", fullName);
                    map.put("email", email);
                    map.put("designation", emp.getDesignation() != null ? emp.getDesignation() : "Store Staff");
                    result.add(map);
                }
            }
        } catch (Exception e) {
            // Fallback
        }

        // Load from users table for store employees
        try {
            List<User> allUsers = userRepository.findAll();
            for (User u : allUsers) {
                String email = u.getEmail() != null ? u.getEmail().toLowerCase().trim() : "";
                String name = (u.getFirstName() + " " + (u.getLastName() != null ? u.getLastName() : "")).trim();

                // Exclude Ultimate Admin and Regional Admin roles
                if (name.toLowerCase().contains("ultimate") || name.toLowerCase().contains("regional admin") || email.contains("ultimate") || email.contains("regional_")) {
                    continue;
                }

                boolean isStoreMember = false;
                if (!storeUserIds.isEmpty() && storeUserIds.contains(u.getId())) {
                    isStoreMember = true;
                } else if (!storeCodeToken.isEmpty() && email.contains(storeCodeToken)) {
                    isStoreMember = true;
                } else if (storeUserIds.isEmpty() && storeCodeToken.isEmpty() && !email.contains("ultimate") && !email.contains("regional_")) {
                    isStoreMember = true;
                }

                if (isStoreMember && !email.isEmpty() && !addedEmails.contains(email) && Boolean.TRUE.equals(u.getActive())) {
                    addedEmails.add(email);
                    Map<String, Object> map = new HashMap<>();
                    map.put("id", u.getId());
                    map.put("employeeCode", "USR-" + u.getId());
                    map.put("name", name.isEmpty() ? email : name);
                    map.put("email", email);
                    map.put("designation", "Store Staff");
                    result.add(map);
                }
            }
        } catch (Exception e) {
            // Fallback
        }

        result.sort((a, b) -> String.CASE_INSENSITIVE_ORDER.compare((String) a.get("name"), (String) b.get("name")));
        return result;
    }

    @Override
    public StoreTask updateTaskStatus(Long taskId, String status, String userEmail) {
        StoreTask task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found: " + taskId));

        task.setStatus(status.toUpperCase());
        task.setUpdatedAt(ZonedDateTime.now());

        // Resume paused task if immediate task is completed
        if ("COMPLETED".equalsIgnoreCase(status) && task.isPreemptiveImmediate() && task.getPausedTaskId() != null) {
            taskRepository.findById(task.getPausedTaskId()).ifPresent(paused -> {
                if ("PAUSED_FOR_IMMEDIATE".equals(paused.getStatus())) {
                    paused.setStatus("IN_PROGRESS");
                    taskRepository.save(paused);
                }
            });
        }

        return taskRepository.save(task);
    }

    @Override
    public StoreTask requestDeadlineExtension(Long taskId, ZonedDateTime requestedDueDate, String reason, String employeeEmail) {
        StoreTask task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found: " + taskId));

        task.setExtensionStatus("REQUESTED");
        task.setRequestedDueDate(requestedDueDate);
        task.setExtensionReason(reason);
        task.setUpdatedAt(ZonedDateTime.now());
        return taskRepository.save(task);
    }

    @Override
    public StoreTask reviewDeadlineExtension(Long taskId, boolean approved, String reviewNotes, String reviewerEmail) {
        StoreTask task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found: " + taskId));

        if (approved) {
            task.setExtensionStatus("APPROVED");
            if (task.getRequestedDueDate() != null) {
                task.setDueDate(task.getRequestedDueDate());
            }
        } else {
            task.setExtensionStatus("REJECTED");
        }
        task.setExtensionReviewNotes(reviewNotes);
        task.setUpdatedAt(ZonedDateTime.now());
        return taskRepository.save(task);
    }

    @Override
    public StoreTaskReport submitTaskReport(Long taskId, String status, Integer completionPercentage, String progressNotes, String attachmentUrl, String reporterName, Long reporterUserId) {
        StoreTask task = taskRepository.findById(taskId)
                .orElseThrow(() -> new IllegalArgumentException("Task not found: " + taskId));

        if (status != null) {
            updateTaskStatus(taskId, status, reporterName);
        }

        StoreTaskReport report = new StoreTaskReport();
        report.setTaskId(taskId);
        report.setStatus(status != null ? status.toUpperCase() : task.getStatus());
        report.setCompletionPercentage(completionPercentage != null ? completionPercentage : 100);
        report.setProgressNotes(progressNotes);
        report.setAttachmentUrl(attachmentUrl);
        report.setReporterName(reporterName);
        report.setReporterUserId(reporterUserId);
        report.setCreatedAt(ZonedDateTime.now());

        return reportRepository.save(report);
    }

    @Override
    public Map<String, Object> getKpis(String employeeEmail) {
        ZonedDateTime now = ZonedDateTime.now(ZoneId.systemDefault());
        ZonedDateTime startOfDay = now.toLocalDate().atStartOfDay(now.getZone());
        ZonedDateTime endOfDay = startOfDay.plusDays(1).minusNanos(1);

        ZonedDateTime startOfMonth = now.withDayOfMonth(1).toLocalDate().atStartOfDay(now.getZone());
        ZonedDateTime endOfMonth = startOfMonth.plusMonths(1).minusNanos(1);

        String filterEmail = (employeeEmail != null && !employeeEmail.trim().isEmpty()) ? employeeEmail.trim().toLowerCase() : null;

        long todayCount = taskRepository.countTodayTasks(filterEmail, startOfDay, endOfDay);
        long completedMonthCount = taskRepository.countMonthlyCompletedTasks(filterEmail, startOfMonth, endOfMonth);
        long uncompletedMonthCount = taskRepository.countMonthlyUncompletedTasks(filterEmail, startOfMonth, endOfMonth);

        Map<String, Object> map = new HashMap<>();
        map.put("todayTasks", todayCount);
        map.put("completedMonthTasks", completedMonthCount);
        map.put("uncompletedMonthTasks", uncompletedMonthCount);
        return map;
    }

    @Override
    public void deleteTask(Long taskId) {
        taskRepository.deleteById(taskId);
    }
}
