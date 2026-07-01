package com.plus33.erp.platform.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "platform_maintenance_window")
public class PlatformMaintenanceWindow {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Version
    @Column(nullable = false)
    private Integer version = 0;

    @Column(name = "start_time", nullable = false)
    @NotNull
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    @NotNull
    private LocalDateTime endTime;

    @Column(name = "affected_services", nullable = false)
    @NotNull
    @Size(max = 500)
    private String affectedServices;

    @Column(name = "notification_msg", nullable = false)
    @NotNull
    @Size(max = 500)
    private String notificationMsg;

    @Column(name = "allowed_users", nullable = false)
    @NotNull
    @Size(max = 500)
    private String allowedUsers;

    @Column(nullable = false)
    @NotNull
    private Boolean active = true;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Integer getVersion() { return version; }
    public void setVersion(Integer version) { this.version = version; }
    public LocalDateTime getStartTime() { return startTime; }
    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }
    public LocalDateTime getEndTime() { return endTime; }
    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }
    public String getAffectedServices() { return affectedServices; }
    public void setAffectedServices(String affectedServices) { this.affectedServices = affectedServices; }
    public String getNotificationMsg() { return notificationMsg; }
    public void setNotificationMsg(String notificationMsg) { this.notificationMsg = notificationMsg; }
    public String getAllowedUsers() { return allowedUsers; }
    public void setAllowedUsers(String allowedUsers) { this.allowedUsers = allowedUsers; }
    public Boolean getActive() { return active; }
    public void setActive(Boolean active) { this.active = active; }
}