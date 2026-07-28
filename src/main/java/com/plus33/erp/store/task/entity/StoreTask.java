package com.plus33.erp.store.task.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "store_tasks")
public class StoreTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "parent_task_id")
    private Long parentTaskId;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String category = "GENERAL";

    @Column(nullable = false)
    private String priority = "COMMON";

    @Column(nullable = false)
    private String status = "PENDING";

    @Column(name = "delegation_mode", nullable = false)
    private String delegationMode = "DIRECT_EMPLOYEE";

    @Column(name = "is_preemptive_immediate", nullable = false)
    private boolean isPreemptiveImmediate = false;

    @Column(name = "paused_task_id")
    private Long pausedTaskId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "due_date")
    private ZonedDateTime dueDate;

    @Column(name = "extension_status", nullable = false)
    private String extensionStatus = "NONE";

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "requested_due_date")
    private ZonedDateTime requestedDueDate;

    @Column(name = "extension_reason", columnDefinition = "TEXT")
    private String extensionReason;

    @Column(name = "extension_review_notes", columnDefinition = "TEXT")
    private String extensionReviewNotes;

    @Column(name = "assigned_employee_id")
    private Long assignedEmployeeId;

    @Column(name = "assigned_employee_name")
    private String assignedEmployeeName;

    @Column(name = "assigned_employee_email")
    private String assignedEmployeeEmail;

    @Column(name = "created_by_user_id")
    private Long createdByUserId;

    @Column(name = "created_by_name")
    private String createdByName;

    @Column(name = "creator_role")
    private String creatorRole;

    @Column(name = "store_id")
    private Long storeId;

    @Column(name = "shift_id")
    private String shiftId;

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt = ZonedDateTime.now();

    @JsonFormat(shape = JsonFormat.Shape.STRING)
    @Column(name = "updated_at", nullable = false)
    private ZonedDateTime updatedAt = ZonedDateTime.now();

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = ZonedDateTime.now();
    }

    public StoreTask() {}

    // Getters and Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Long getParentTaskId() { return parentTaskId; }
    public void setParentTaskId(Long parentTaskId) { this.parentTaskId = parentTaskId; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }

    public String getPriority() { return priority; }
    public void setPriority(String priority) { this.priority = priority; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getDelegationMode() { return delegationMode; }
    public void setDelegationMode(String delegationMode) { this.delegationMode = delegationMode; }

    public boolean isPreemptiveImmediate() { return isPreemptiveImmediate; }
    public void setPreemptiveImmediate(boolean preemptiveImmediate) { isPreemptiveImmediate = preemptiveImmediate; }

    public Long getPausedTaskId() { return pausedTaskId; }
    public void setPausedTaskId(Long pausedTaskId) { this.pausedTaskId = pausedTaskId; }

    public ZonedDateTime getDueDate() { return dueDate; }
    public void setDueDate(ZonedDateTime dueDate) { this.dueDate = dueDate; }

    public String getExtensionStatus() { return extensionStatus; }
    public void setExtensionStatus(String extensionStatus) { this.extensionStatus = extensionStatus; }

    public ZonedDateTime getRequestedDueDate() { return requestedDueDate; }
    public void setRequestedDueDate(ZonedDateTime requestedDueDate) { this.requestedDueDate = requestedDueDate; }

    public String getExtensionReason() { return extensionReason; }
    public void setExtensionReason(String extensionReason) { this.extensionReason = extensionReason; }

    public String getExtensionReviewNotes() { return extensionReviewNotes; }
    public void setExtensionReviewNotes(String extensionReviewNotes) { this.extensionReviewNotes = extensionReviewNotes; }

    public Long getAssignedEmployeeId() { return assignedEmployeeId; }
    public void setAssignedEmployeeId(Long assignedEmployeeId) { this.assignedEmployeeId = assignedEmployeeId; }

    public String getAssignedEmployeeName() { return assignedEmployeeName; }
    public void setAssignedEmployeeName(String assignedEmployeeName) { this.assignedEmployeeName = assignedEmployeeName; }

    public String getAssignedEmployeeEmail() { return assignedEmployeeEmail; }
    public void setAssignedEmployeeEmail(String assignedEmployeeEmail) { this.assignedEmployeeEmail = assignedEmployeeEmail; }

    public Long getCreatedByUserId() { return createdByUserId; }
    public void setCreatedByUserId(Long createdByUserId) { this.createdByUserId = createdByUserId; }

    public String getCreatedByName() { return createdByName; }
    public void setCreatedByName(String createdByName) { this.createdByName = createdByName; }

    public String getCreatorRole() { return creatorRole; }
    public void setCreatorRole(String creatorRole) { this.creatorRole = creatorRole; }

    public Long getStoreId() { return storeId; }
    public void setStoreId(Long storeId) { this.storeId = storeId; }

    public String getShiftId() { return shiftId; }
    public void setShiftId(String shiftId) { this.shiftId = shiftId; }

    public ZonedDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(ZonedDateTime createdAt) { this.createdAt = createdAt; }

    public ZonedDateTime getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(ZonedDateTime updatedAt) { this.updatedAt = updatedAt; }
}
