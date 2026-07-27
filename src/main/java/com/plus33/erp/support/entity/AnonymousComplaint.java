package com.plus33.erp.support.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "anonymous_complaints")
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AnonymousComplaint {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tracking_key", nullable = false, unique = true, length = 50)
    private String trackingKey; // e.g. TK-ANO-7F9C2B

    @Column(nullable = false, length = 50)
    @Builder.Default
    private String category = "WORKPLACE_COMPLAINT"; // PAY_ISSUE, FAVORITISM, HARASSMENT, SAFETY_HAZARD, MANAGEMENT_FRAUD, OTHER

    @Column(length = 100)
    private String subcategory;

    @Column(nullable = false, length = 20)
    @Builder.Default
    private String severity = "HIGH"; // LOW, MEDIUM, HIGH, CRITICAL

    @Column(nullable = false, length = 30)
    @Builder.Default
    private String status = "OPEN"; // OPEN, UNDER_INVESTIGATION, ACTION_TAKEN, RESOLVED, CLOSED

    @Column(nullable = false)
    private String subject;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(name = "store_id")
    private Long storeId;

    @Column(name = "region_id")
    private Long regionId;

    @Column(name = "compliance_response", columnDefinition = "TEXT")
    private String complianceResponse;

    @Column(name = "response_published_at")
    private LocalDateTime responsePublishedAt;

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

    @Transient
    private Long reporterId;

    @Transient
    private String reporterName;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) createdAt = LocalDateTime.now();
        if (updatedAt == null) updatedAt = LocalDateTime.now();
        if (status == null) status = "OPEN";
        if (severity == null) severity = "HIGH";
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
