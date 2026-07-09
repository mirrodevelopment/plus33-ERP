package com.plus33.erp.workforce.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter @Setter @Entity @Table(name = "leave_blackout_dates")
@NoArgsConstructor @AllArgsConstructor
public class LeaveBlackoutDate {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_id", nullable = false)
    private Long companyId = 1L;

    @Column(name = "name", nullable = false, length = 150)
    private String name;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    /** If TRUE, applies to all leave types */
    @Column(name = "applies_to_all_types")
    private Boolean appliesToAllTypes = true;

    @Column(name = "reason", columnDefinition = "TEXT")
    private String reason;

    @Column(name = "active")
    private Boolean active = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist protected void onCreate() { createdAt = updatedAt = LocalDateTime.now(); }
    @PreUpdate  protected void onUpdate() { updatedAt = LocalDateTime.now(); }
}
