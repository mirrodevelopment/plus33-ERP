package com.plus33.erp.support.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "support_tickets")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupportTicket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "ticket_code", nullable = false, unique = true, length = 30)
    private String ticketCode;

    @Column(nullable = false, length = 50)
    private String category; // TECH_SUPPORT, HR_PAYROLL, GENERAL_FEEDBACK

    @Column(length = 100)
    private String subcategory;

    @Column(nullable = false, length = 20)
    @Builder.Default
    private String priority = "MEDIUM"; // LOW, MEDIUM, HIGH, URGENT

    @Column(nullable = false, length = 30)
    @Builder.Default
    private String status = "OPEN"; // OPEN, IN_PROGRESS, RESOLVED, CLOSED

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "reporter_id")
    private Long reporterId;

    @Column(name = "reporter_name", length = 100)
    private String reporterName;

    @Column(name = "reporter_role", length = 50)
    private String reporterRole;

    @Column(name = "store_id")
    private Long storeId;

    @Column(name = "region_id")
    private Long regionId;

    @Column(name = "assigned_to")
    private Long assignedTo;

    @Column(name = "admin_response", columnDefinition = "TEXT")
    private String adminResponse;

    @Column(name = "resolved_at")
    private LocalDateTime resolvedAt;

    @Column(name = "target_role", length = 50)
    private String targetRole; // STORE_ADMIN, REGIONAL_ADMIN, NATIONAL_ADMIN, ULTIMATE_ADMIN

    @Column(name = "custom_category", length = 150)
    private String customCategory;

    @Column(name = "escalation_level")
    @Builder.Default
    private Integer escalationLevel = 0;

    @Column(name = "is_escalated")
    @Builder.Default
    private Boolean isEscalated = false;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (updatedAt == null) updatedAt = LocalDateTime.now();
        if (status == null) status = "OPEN";
        if (priority == null) priority = "MEDIUM";
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
